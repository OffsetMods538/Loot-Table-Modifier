package top.offsetmonkey538.loottablemodifier;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
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
import top.offsetmonkey538.loottablemodifier.mixin.LootTableAccessor;
import top.offsetmonkey538.loottablemodifier.resource.LootModifier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTableModifier implements ModInitializer {
	public static final String MOD_ID = "loot-table-modifier";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Text.of("Example Pack"), ResourcePackActivationType.NORMAL);
	}

	public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
		final Map<Identifier, LootModifier> modifiers = loadModifiers(resourceManager, registryOps);

		int amountModified = 0;
		LOGGER.info("Applying loot table modifiers...");
		for (RegistryEntry.Reference<LootTable> tableReference : getRegistryAsWrapper(lootRegistry).streamEntries().toList()) {
			final RegistryKey<LootTable> tableKey = tableReference.registryKey();

			amountModified += applyModifiers(lootRegistry.get(tableKey), tableKey, modifiers);
		}

		modifiersApplied(amountModified, modifiers);
	}

	private static Map<Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, RegistryOps<JsonElement> registryOps) {
		LOGGER.info("Loading loot table modifiers...");

		final Map<Identifier, LootModifier> result = new HashMap<>();

		for (Map.Entry<Identifier, Resource> entry : resourceManager.findResources(MOD_ID + "/loot_modifier", path -> path.toString().endsWith(".json")).entrySet()) {
			final Identifier id = entry.getKey();

			try {
				result.put(
						id,
						LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(entry.getValue().getReader())).getOrThrow().getFirst()
				);
			} catch (IOException e) {
				//noinspection StringConcatenationArgumentToLogCall
				LOGGER.error("Failed to load loot table modifier from '" + id + "'!", e);
			}
		}

		LOGGER.info("Loaded {} loot modifiers", result.size());

		return result;
	}

	// Returns: amount of loot tables modified
	private static int applyModifiers(LootTable table, RegistryKey<LootTable> tableKey, Map<Identifier, LootModifier> modifiers) {
		final Identifier currentId = tableKey.getValue();
		final List<Identifier> usable = modifiers.keySet().stream().filter(entry -> modifiers.get(entry).modifies().contains(currentId)).toList();

		if (usable.isEmpty()) return 0;

		final List<LootPool> newPools = ImmutableList.<LootPool>builder()
				.addAll(table.pools)
				.addAll(
						usable.stream()
								.map(modifiers::get)
								.map(LootModifier::pools)
								.flatMap(List::stream)
								.toList()
				)
				.build();

		((LootTableAccessor) table).setPools(newPools);

		for (Identifier modifierId : usable) {
			final LootModifier modifier = modifiers.get(modifierId);

			modifier.modifies().remove(currentId);

			if (modifier.modifies().isEmpty()) modifiers.remove(modifierId);
		}

		return usable.size();
	}

	private static void modifiersApplied(int amountModified, Map<Identifier, LootModifier> modifiers) {
		LOGGER.info("Modified {} loot tables!", amountModified);

		if (modifiers.isEmpty()) return;

		LOGGER.warn("There were unused modifiers:");
		for (Map.Entry<Identifier, LootModifier> entry : modifiers.entrySet()) {
			LOGGER.warn("\tModifier '{}' failed to modify loot table for entities: ", entry.getKey());
			for (Identifier id : entry.getValue().modifies()) {
				LOGGER.warn("\t\t- {}", id);
			}
		}
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

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
