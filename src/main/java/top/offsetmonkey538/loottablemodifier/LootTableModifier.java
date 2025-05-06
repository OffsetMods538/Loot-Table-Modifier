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
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;
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
		LootModifierActionTypes.register();

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Text.of("Example Pack"), ResourcePackActivationType.NORMAL);
	}

	public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
		final Map<Identifier, LootModifier> modifiers = loadModifiers(resourceManager, registryOps);
		final Map<Identifier, LootModifier> failedModifiers = new HashMap<>(0);

		int amountModified = 0;
		LOGGER.info("Applying loot table modifiers...");
		for (Map.Entry<Identifier, LootModifier> modifierEntry : modifiers.entrySet()) {
			final LootModifier modifier = modifierEntry.getValue();

			amountModified += modifier.apply(lootRegistry);

			if (!modifier.modifies().isEmpty()) failedModifiers.put(modifierEntry.getKey(), modifier);
		}

		modifiersApplied(amountModified, failedModifiers);
	}

	private static Map<Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, RegistryOps<JsonElement> registryOps) {
		LOGGER.info("Loading loot table modifiers...");

		final Map<Identifier, LootModifier> result = new HashMap<>();

		for (Map.Entry<Identifier, Resource> entry : resourceManager.findResources(MOD_ID + "/loot_modifier", path -> path.toString().endsWith(".json")).entrySet()) {
			final Identifier id = entry.getKey();

			try {
				LOGGER.debug("Loading load loot table modifier from '%s'".formatted(id));
				result.put(
						id,
						LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(entry.getValue().getReader())).getOrThrow().getFirst()
				);
			} catch (Exception e) {
				//noinspection StringConcatenationArgumentToLogCall
				LOGGER.error("Failed to load loot table modifier from '%s'!".formatted(id), e);
			}
		}

		LOGGER.info("Loaded {} loot modifiers!", result.size());

		return result;
	}

	private static void modifiersApplied(int amountModified, Map<Identifier, LootModifier> failedModifiers) {
		LOGGER.info("Modified {} loot tables!", amountModified);

		if (failedModifiers.isEmpty()) return;

		LOGGER.warn("There were unused modifiers:");
		for (Map.Entry<Identifier, LootModifier> entry : failedModifiers.entrySet()) {
			LOGGER.warn("\tModifier '{}' failed to modify loot table for entities: ", entry.getKey());
			for (Identifier id : entry.getValue().modifies()) {
				LOGGER.warn("\t\t- {}", id);
			}
		}
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
