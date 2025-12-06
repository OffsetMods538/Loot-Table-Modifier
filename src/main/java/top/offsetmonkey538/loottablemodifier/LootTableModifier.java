package top.offsetmonkey538.loottablemodifier;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.DataProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.commons.io.file.PathUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.loot.LootTableWrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static net.minecraft.server.command.CommandManager.literal;
import static top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction.*;

public class LootTableModifier implements ModInitializer {
	public static final String MOD_ID = "loot-table-modifier";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final boolean IS_DEV;
	static {
		final String isDev = System.getProperty("lootTableModifierDev", "");
		if (isDev.equalsIgnoreCase("true")) IS_DEV = true;
		else if (isDev.equalsIgnoreCase("false")) IS_DEV = false; // This way it can be disabled in devenv too.
		else IS_DEV = FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	// Only used when IS_DEV is true
	private static final List<Identifier> MODIFIED_TABLE_IDs;
	static {
		if (IS_DEV) MODIFIED_TABLE_IDs = Collections.synchronizedList(new ArrayList<>(0));
		else MODIFIED_TABLE_IDs = null;
	}

	@Override
	public void onInitialize() {
		LootModifierActionTypes.register();
		LootModifierPredicateTypes.register();

		if (IS_DEV) enableDebug();
	}

	public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
		final Map<Identifier, LootModifier> modifiers = loadModifiers(resourceManager, registryOps);

		final List<Identifier> modifiedTableIds = new ArrayList<>(); // Used for exporting modified ones
		int poolsModified = 0, entriesModified = 0;
		boolean tableModified, poolModified;

		LOGGER.info("Modifying loot tables...");
		final Stopwatch stopwatch = Stopwatch.createStarted();

		for (Iterator<RegistryEntry.Reference<LootTable>> it = getRegistryAsWrapper(lootRegistry).streamEntries().iterator(); it.hasNext(); ) {
			final RegistryEntry.Reference<LootTable> registryEntry = it.next();
			final RegistryKey<LootTable> key = registryEntry.registryKey();

			final LootTable vanillaTable = lootRegistry.get(key);
			final Identifier tableId = key.getValue();
			if (vanillaTable == null) throw new IllegalStateException("Loot table with id '%s' is null!".formatted(key));
            final top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootTable table = new LootTableWrapper(lootRegistry.get(key));

			tableModified = false;

            final LootPool[] poolsCopy = table.getPools().toArray(LootPool[]::new);
			int poolsSize = Math.max(1, poolsCopy.length); // Run loop at least once
			for (int i = 0; i < poolsSize; i++) {
				final LootPool pool = poolsCopy.length == 0 ? null : poolsCopy[i];
				poolModified = false;

				final LootPoolEntry[] entriesCopy = pool == null ? new LootPoolEntry[]{} : pool.getEntries().toArray(LootPoolEntry[]::new);
				int entriesSize = Math.max(1, entriesCopy.length); // Run loop at least once
				for (int j = 0; j < entriesSize; j++) {
					final LootPoolEntry entry = entriesCopy.length == 0 ? null : entriesCopy[j];

					for (Map.Entry<Identifier, LootModifier> modifierEntry : modifiers.entrySet()) {
						final LootModifierContext context = new LootModifierContext(table, tableId, pool, entry, tableModified, poolModified);

						final LootModifier modifier = modifierEntry.getValue();
						if (!modifier.test(context)) continue;

						if (IS_DEV) LOGGER.warn("Modifier {} can modify table {}", modifierEntry.getKey(), tableId);


						int result = modifier.apply(context);

						if (IS_DEV && result != MODIFIED_NONE) LOGGER.warn("Modifier {} modified table {} with modified mask {}", modifierEntry.getKey(), tableId, Integer.toUnsignedString(result, 2));

						if ((result & MODIFIED_TABLE) == MODIFIED_TABLE) tableModified = true;
						if ((result & MODIFIED_POOL) == MODIFIED_POOL) poolModified = true;
						if ((result & MODIFIED_ENTRY) == MODIFIED_ENTRY) entriesModified++;
					}
				}

				poolsModified += poolModified ? 1 : 0;
			}
			if (tableModified) modifiedTableIds.add(tableId);
		}


		LOGGER.info("Applied {} modifiers and modified {} entries, {} pools and {} loot tables in {}!", modifiers.size(), entriesModified, poolsModified, modifiedTableIds.size(), stopwatch.stop());

		if (!IS_DEV) return;

		LOGGER.warn("Dev mode enabled, modified loot tables can be exported using the '/loot-table-modifier debug export' command");
		synchronized (MODIFIED_TABLE_IDs) {
			MODIFIED_TABLE_IDs.clear();
			MODIFIED_TABLE_IDs.addAll(modifiedTableIds);
		}
    }

	private static Map<Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, RegistryOps<JsonElement> registryOps) {
		LOGGER.info("Loading loot table modifiers...");
		final Stopwatch stopwatch = Stopwatch.createStarted();

		final Map<Identifier, LootModifier> result = new HashMap<>();

		for (Map.Entry<Identifier, Resource> entry : resourceManager.findResources(MOD_ID + "/loot_modifier", path -> path.toString().endsWith(".json")).entrySet()) {
			final Identifier id = entry.getKey();

			try {
				LOGGER.debug("Loading load loot table modifier from '{}'", id);
				result.put(
						id,
						LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(entry.getValue().getReader())).getOrThrow().getFirst()
				);
			} catch (Exception e) {
				//noinspection StringConcatenationArgumentToLogCall
				LOGGER.error("Failed to load loot table modifier from '%s'!".formatted(id), e);
			}
		}

		LOGGER.info("Loaded {} loot modifiers in {}!", result.size(), stopwatch.stop());

		return result;
	}

	private static void enableDebug() {
		ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Text.of("Example Pack"), ResourcePackActivationType.NORMAL);
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> dispatcher.register(
                literal(MOD_ID)
                        .then(
                                literal("debug")
                                        .then(
                                                literal("export")
                                                        .executes(
                                                                LootTableModifier::executeExportCommand
                                                        )
                                        )
                        )
        ));
	}

	private static int executeExportCommand(CommandContext<ServerCommandSource> context) {
		synchronized (MODIFIED_TABLE_IDs) {
			final ServerCommandSource source = context.getSource();
			final MinecraftServer server = source.getServer();

			final DynamicOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());
			try {
				final Path exportDir = FabricLoader.getInstance().getGameDir().resolve(".loot-table-modifier").resolve("export");
				if (Files.exists(exportDir)) PathUtils.deleteDirectory(exportDir);

				source.sendFeedback(() -> Text.literal("Exporting modified tables to ").append(Text.literal(exportDir.toString()).setStyle(Style.EMPTY.withUnderline(true).withColor(Formatting.WHITE).withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to copy"))).withClickEvent(new ClickEvent.CopyToClipboard(exportDir.toAbsolutePath().toString())))), true);
				final Stopwatch stopwatch = Stopwatch.createStarted();

				for (Identifier id : MODIFIED_TABLE_IDs) {
					final LootTable table = server.getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, id));
					final Path file = exportDir.resolve(id.getNamespace()).resolve(id.getPath() + ".json");
					Files.createDirectories(file.getParent());

					LOGGER.warn("Exporting loot table to {}", file);
					DataResult<JsonElement> dataResult = LootTable.CODEC.encodeStart(ops, table);
					final Optional<JsonElement> optionalResult = dataResult.resultOrPartial(LOGGER::error);
					final JsonElement result = optionalResult.orElseThrow();

					LOGGER.warn("Writing loot table to {}", file);

					try (JsonWriter jsonWriter = new JsonWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {
						jsonWriter.setSerializeNulls(false);
						jsonWriter.setIndent("  ");
						JsonHelper.writeSorted(jsonWriter, result, DataProvider.JSON_KEY_SORTING_COMPARATOR);
					}
				}
				source.sendFeedback(() -> Text.literal("Exported %s modified tables in %s".formatted(MODIFIED_TABLE_IDs.size(), stopwatch.stop())), true);
			} catch (IOException e) {
				throw new RuntimeException("Failed to export modified tables!", e);
			}

			return 1;
		}
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	/*
    In 1.21.4, the 'Registry' class extends 'RegistryWrapper' and inherits the 'streamEntries' method from it.
    In 1.20.5, the 'Registry' class *doesn't* extend the 'RegistryWrapper' and implements its own 'streamEntries' method.
    Compiling on both versions works, because the names of the methods are the same, but they compile to different intermediary names, thus a jar compiled for 1.20.5 doesn't work on 1.21.4 and vice versa.
    Solution: Turn the 'Registry' into a 'RegistryWrapper' as its 'streamEntries' retains the same intermediary on both versions.
    If 'Registry' implements 'RegistryWrapper': cast it
    Else: call 'getReadOnlyWrapper' on the registry (doesn't exist on 1.21.4, otherwise would've used 'registry.getReadOnlyWrapper().streamEntries()')
	 */
	private static <T> RegistryWrapper<T> getRegistryAsWrapper(@NotNull Registry<T> registry) {
		//noinspection ConstantValue,RedundantSuppression: On lower versions, Registry doesn't extend RegistryWrapper and thus the 'isAssignableFrom' check can be false. The redundant supression is for the unchecked cast below.
		if (RegistryWrapper.class.isAssignableFrom(registry.getClass()))
			//noinspection unchecked,RedundantCast: I swear it casts 🤞
			return (RegistryWrapper<T>) registry;

		try {
			//noinspection unchecked: Seriously I swear 🤞🤞
			return (RegistryWrapper<T>) registry.getClass().getDeclaredMethod(FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_2378", "method_46771", "()Lnet/minecraft/class_7225$class_7226;")).invoke(registry);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
