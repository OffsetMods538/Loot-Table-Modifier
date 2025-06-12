package top.offsetmonkey538.loottablemodifier;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	@Override
	public void onInitialize() {
		LootModifierActionTypes.register();
		LootModifierPredicateTypes.register();

		if (IS_DEV)
			ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Text.of("Example Pack"), ResourcePackActivationType.NORMAL);
	}

	public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
		final Map<Identifier, LootModifier> modifiers = loadModifiers(resourceManager, registryOps);
		final Map<Identifier, LootModifier> failedModifiers = new HashMap<>(0);

		int amountModified = 0;
		LOGGER.info("Modifying loot tables...");
		final Stopwatch stopwatch = Stopwatch.createStarted();
		// TODO: Would looping through all tables here be faster than doing that for each modifier? To test I guess


		// TODO: so about that never-nesting............
		// fixme: don't think modifying for example tables without any pools or entries would work with this.....
		// TODO: increment "amountModified" somewhere...
		// todo. just fucking rewrite this when your brain actually works and can think
		for (Iterator<RegistryEntry.Reference<LootTable>> it = getRegistryAsWrapper(lootRegistry).streamEntries().iterator(); it.hasNext(); ) {
			final RegistryEntry.Reference<LootTable> registryEntry = it.next();
			final RegistryKey<LootTable> key = registryEntry.registryKey();

			final LootTable table = lootRegistry.get(key);
			if (table == null) throw new IllegalStateException("Loot table with id '%s' is null!".formatted(key));


			for (LootPool pool : table.pools) {
				for (LootPoolEntry entry : pool.entries) {
					final LootModifierContext context = new LootModifierContext(table, key.getValue(), pool, entry);
					for (Map.Entry<Identifier, LootModifier> modifierEntry : modifiers.entrySet()) {
						final LootModifier modifier = modifierEntry.getValue();
						if (!modifier.testModifies(context)) continue;
						if (IS_DEV) LOGGER.error("Modifier {} can modify table {}", modifierEntry.getKey(), key.getValue());
						modifier.apply(context); // FIXME: stuff modifying table or pool may apply multiple times
					}
				}
			}

			//for (Map.Entry<Identifier, LootModifier> modifierEntry : modifiers.entrySet()) {
			//	final LootModifier modifier = modifierEntry.getValue();

			//	for (LootModifierPredicate modifiesPredicate : modifier.modifies()) {
			//		if (modifiesPredicate.requiredContext() == LootModifierContext.REQUIRES_TABLE && !modifiesPredicate.test(tableContext)) continue;

			//		for (LootPool pool : table.pools) {
			//			final LootModifierContext poolContext = new LootModifierContext(table, key.getValue(), pool);

			//			if (modifiesPredicate.requiredContext() == LootModifierContext.REQUIRES_POOL && !modifiesPredicate.test(poolContext)) continue;

			//			for (LootPoolEntry entry : pool.entries) {
			//				final LootModifierContext entryContext = new LootModifierContext(table, key.getValue(), pool, entry);

			//				if (modifiesPredicate.requiredContext() == LootModifierContext.REQUIRES_POOL && !modifiesPredicate.test(entryContext)) continue;

			//				for (LootModifierAction action : modifier.actions()) {
			//					action.apply(entryContext);
			//				}
			//			}
			//		}
			//	}
			//}


			// todo: modified += apply(table) ? 1 : 0;
		}


		//for (Map.Entry<Identifier, LootModifier> modifierEntry : modifiers.entrySet()) {
		//	final LootModifier modifier = modifierEntry.getValue();

		//	amountModified += modifier.apply(lootRegistry);

		//	// TODO: try to figure smt out abt this: if (!modifier.modifies().isEmpty()) failedModifiers.put(modifierEntry.getKey(), modifier);
		//}


		LOGGER.info("Applied {} modifiers and modified {} loot tables in {}!", modifiers.size(), amountModified, stopwatch);
		modifiersApplied(failedModifiers);
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

		LOGGER.info("Loaded {} loot modifiers in {}!", result.size(), stopwatch);

		return result;
	}

	private static void modifiersApplied(Map<Identifier, LootModifier> failedModifiers) {
		if (failedModifiers.isEmpty()) return;

		LOGGER.warn("There were unused modifiers:");
		//for (Map.Entry<Identifier, LootModifier> entry : failedModifiers.entrySet()) {
		//	LOGGER.warn("\tModifier '{}' failed to modify loot table for predicates: ", entry.getKey());
		//	for (LootTablePredicate predicate : entry.getValue().modifies()) {
		//		LOGGER.warn("\t\t- {}", predicate);
		//	}
		//}
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	/*
    In 1.21.4, the 'Registry' class extends 'RegistryWrapper' and inherits the 'streamEntries' method from *it*.
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
