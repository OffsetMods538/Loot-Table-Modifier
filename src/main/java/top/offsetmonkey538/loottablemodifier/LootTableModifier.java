package top.offsetmonkey538.loottablemodifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.loottablemodifier.registry.LootModifier;
import top.offsetmonkey538.loottablemodifier.registry.ModRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LootTableModifier implements ModInitializer {
	public static final String MOD_ID = "loot-table-modifier";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final Map<LootModifier, List<Identifier>> modifierCache = new HashMap<>();
	private int amountModified = 0;
	private boolean isModifying = false;

	@Override
	public void onInitialize() {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Text.of("Example Pack"), ResourcePackActivationType.NORMAL);


		ModRegistries.register();

		//ServerLifecycleEvents.SERVER_STARTED.register(server -> {
		//	System.out.println(server.getRegistryManager().get(ModRegistries.LOOT_MODIFIER).stream().toList());
		//});
		//ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
		//	System.out.println(server.getRegistryManager().get(ModRegistries.LOOT_MODIFIER).stream().toList());
		//});

		LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
			if (!isModifying) {
				LOGGER.info("Starting loot table modification...");
				isModifying = true;
				amountModified = 0;
				modifierCache.clear();
				System.out.println(wrapperLookup.getWrapperOrThrow(ModRegistries.LOOT_MODIFIER).streamEntries().toList());
				modifierCache.putAll(wrapperLookup.getWrapperOrThrow(ModRegistries.LOOT_MODIFIER).streamEntries().map(LootModifier::copyOfEntry).collect(Collectors.toMap(k->k, entry -> new ArrayList<>(entry.modifies()))));
			}

			final Identifier currentId = registryKey.getValue();
			final List<LootModifier> usable = modifierCache.keySet().stream().filter(entry -> entry.modifies().contains(currentId)).toList();
			if(usable.isEmpty()) LOGGER.debug("No usable modifiers found for '{}'!", currentId);

			for (LootModifier modifier : usable) {
				final LootTable added = modifier.lootTable();

				added.pools.forEach(builder::pool);

				amountModified++;
				modifierCache.get(modifier).remove(currentId);
				if (modifierCache.get(modifier).isEmpty()) modifierCache.remove(modifier);
			}
		});

		LootTableEvents.ALL_LOADED.register((resourceManager, lootRegistry) -> {
			LOGGER.info("Loot table modification finished with {} modifiers!", amountModified);

			if (!modifierCache.isEmpty()) {
				LOGGER.warn("There were unused modifiers:");
				for (Map.Entry<LootModifier, List<Identifier>> entry : modifierCache.entrySet()) {
					LOGGER.warn("\tModifier '{}' failed to modify loot table for entities: ", entry.getKey().name());
					for (Identifier id : entry.getValue()) {
						LOGGER.warn("\t\t- {}", id);
					}
				}
			}

			modifierCache.clear();
			isModifying = false;
			amountModified = 0;
		});
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
