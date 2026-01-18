package top.offsetmonkey538.loottablemodifier.common;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.Pair;
import org.apache.commons.io.file.PathUtils;
import org.jetbrains.annotations.Unmodifiable;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.common.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.LootModifierContext;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.ResourceManager;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformCommandUtils;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain;
import top.offsetmonkey538.monkeylib538.common.api.command.CommandRegistrationApi;
import top.offsetmonkey538.monkeylib538.common.api.log.MonkeyLibLogger;
import top.offsetmonkey538.monkeylib538.common.api.platform.LoaderUtil;
import top.offsetmonkey538.monkeylib538.common.api.telemetry.TelemetryRegistry;
import top.offsetmonkey538.monkeylib538.common.api.text.MonkeyLibStyle;
import top.offsetmonkey538.monkeylib538.common.api.text.MonkeyLibText;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static top.offsetmonkey538.monkeylib538.common.api.command.CommandAbstractionApi.*;

public final class LootTableModifierCommon {
	public static final String MOD_ID = "loot-table-modifier";
	public static final MonkeyLibLogger LOGGER = MonkeyLibLogger.create(MOD_ID);

	public static final boolean IS_DEV;
	static {
		final String isDev = System.getProperty("lootTableModifierDev", "");
		if (isDev.equalsIgnoreCase("true")) IS_DEV = true;
		else if (isDev.equalsIgnoreCase("false")) IS_DEV = false; // This way it can be disabled in devenv too.
		else IS_DEV = LoaderUtil.isDevelopmentEnvironment();
	}

	// Only used when IS_DEV is true
	private static final List<Identifier> MODIFIED_TABLE_IDs;
	static {
		if (IS_DEV) MODIFIED_TABLE_IDs = Collections.synchronizedList(new ArrayList<>(0));
		else MODIFIED_TABLE_IDs = null;
	}

	public static void initialize() {
		TelemetryRegistry.register(MOD_ID);

		LootModifierActionTypes.register();
		LootModifierPredicateTypes.register();

		if (IS_DEV) enableDebug();
	}

	public static void runModification(ResourceManager resourceManager, Stream<Pair<Identifier, LootTable>> lootRegistry, DynamicOps<JsonElement> registryOps) {
		LOGGER.info("Gathering loot tables...");
		final Stopwatch stopwatch = Stopwatch.createStarted();

		// Streams are lazy so calling toList here means all the intermediary steps will only now be executed and counted between the stopwatch.
		final List<Pair<Identifier, LootTable>> tables = lootRegistry.toList();

		LOGGER.info("Gathered %s loot tables in %s", tables.size(), stopwatch.stop());

		LootTableModifierCommon.runModification(loadModifiers(resourceManager, registryOps), tables);
	}

	private static Map<Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, DynamicOps<JsonElement> registryOps) {
		LOGGER.info("Loading loot table modifiers...");
		final Stopwatch stopwatch = Stopwatch.createStarted();

		final Map<Identifier, LootModifier> result = new HashMap<>();

		resourceManager.listResources(MOD_ID + "/loot_modifier", path -> path.endsWith(".json")).forEach(resource -> {
			final Identifier id = resource.left();

			try {
				LOGGER.debug("Loading load loot table modifier from '%s'", id);
				result.put(
						id,
						// Can't just use orElseThrow cause 1.20.1 don't have that
						LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(resource.right().get())).map(com.mojang.datafixers.util.Pair::getFirst).resultOrPartial(error -> {throw new RuntimeException(error);}).orElseThrow()
				);
			} catch (Exception e) {
				LOGGER.error("Failed to load loot table modifier from '%s'!", e, id);
			}
		});

		LOGGER.info("Loaded %s loot modifiers in %s!", result.size(), stopwatch.stop());

		return result;
	}

	//public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
	private static void runModification(Map<Identifier, LootModifier> modifiers, @Unmodifiable List<Pair<Identifier, LootTable>> tables) {
		final List<Identifier> modifiedTableIds = new ArrayList<>(); // Used for exporting modified ones
		int poolsModified = 0, entriesModified = 0;
		boolean tableModified, poolModified;

		LOGGER.info("Modifying loot tables...");
		final Stopwatch stopwatch = Stopwatch.createStarted();

		for (Pair<Identifier, LootTable> tableEntry : tables) {
			final Identifier tableId = tableEntry.left();
			final LootTable table = tableEntry.right();

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

						if (IS_DEV) LOGGER.warn("Modifier %s can modify table %s", modifierEntry.getKey(), tableId);


						int result = modifier.apply(context);

						if (IS_DEV && result != LootModifierAction.MODIFIED_NONE) LOGGER.warn("Modifier %s modified table %s with modified mask %s", modifierEntry.getKey(), tableId, Integer.toUnsignedString(result, 2));

						if ((result & LootModifierAction.MODIFIED_TABLE) == LootModifierAction.MODIFIED_TABLE) tableModified = true;
						if ((result & LootModifierAction.MODIFIED_POOL) == LootModifierAction.MODIFIED_POOL) poolModified = true;
						if ((result & LootModifierAction.MODIFIED_ENTRY) == LootModifierAction.MODIFIED_ENTRY) entriesModified++;
					}
				}

				poolsModified += poolModified ? 1 : 0;
			}
			if (tableModified) modifiedTableIds.add(tableId);
		}


		LOGGER.info("Applied %s modifiers and modified %s entries, %s pools and %s loot tables in %s!", modifiers.size(), entriesModified, poolsModified, modifiedTableIds.size(), stopwatch.stop());

		if (!IS_DEV) return;

		LOGGER.warn("Dev mode enabled, modified loot tables can be exported using the '/loot-table-modifier debug export' command");
		synchronized (MODIFIED_TABLE_IDs) {
			MODIFIED_TABLE_IDs.clear();
			MODIFIED_TABLE_IDs.addAll(modifiedTableIds);
		}
    }

	private static void enableDebug() {
        CommandRegistrationApi.registerCommand(
                literal(MOD_ID)
                        .then(
                                literal("debug")
                                        .then(
                                                literal("export")
                                                        .executes(
                                                                LootTableModifierCommon::executeExportCommand
                                                        )
                                        )
                        )
        );
	}

	private static int executeExportCommand(CommandContext<Object> context) {
		synchronized (MODIFIED_TABLE_IDs) {
			final DynamicOps<JsonElement> ops = PlatformCommandUtils.getRegistryOps(context);

			try {
				final Path exportDir = LoaderUtil.getGameDir().resolve(".loot-table-modifier").resolve("export");
				if (Files.exists(exportDir)) PathUtils.deleteDirectory(exportDir);

				sendText(
						context,
						MonkeyLibText
								.of("Exporting modified tables to ")
								.append(MonkeyLibText.of(exportDir.toString()).setStyle(
										MonkeyLibStyle.empty()
												.withUnderline(true)
												.withColor(MonkeyLibStyle.Color.WHITE)
												.withShowText(MonkeyLibText.of("Click to copy"))
												.withCopyToClipboard(exportDir.toAbsolutePath().toString())
								))
						);
				final Stopwatch stopwatch = Stopwatch.createStarted();

				for (Identifier id : MODIFIED_TABLE_IDs) {
					final LootTable table = PlatformCommandUtils.getTableForId(context, id);
					final Path file = exportDir.resolve(id.getNamespace()).resolve(id.getPath() + ".json");
					Files.createDirectories(file.getParent());

					LOGGER.warn("Exporting loot table to %s", file);
					DataResult<JsonElement> dataResult = LootTable.CODEC_PROVIDER.get().encodeStart(ops, table);
					final Optional<JsonElement> optionalResult = dataResult.resultOrPartial(LOGGER::error);
					final JsonElement result = optionalResult.orElseThrow();

					LOGGER.warn("Writing loot table to %s", file);

					try (JsonWriter jsonWriter = new JsonWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {
						jsonWriter.setSerializeNulls(false);
						jsonWriter.setIndent("  ");
						PlatformMain.writeSorted(jsonWriter, result);
					}
				}
				sendMessage(context, "Exported %s modified tables in %s".formatted(MODIFIED_TABLE_IDs.size(), stopwatch.stop()));
			} catch (IOException e) {
				throw new RuntimeException("Failed to export modified tables!", e);
			}

			return 1;
		}
	}

    public static <T> T load(Class<T> clazz) {
        return java.util.ServiceLoader.load(clazz, LootTableModifierCommon.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
