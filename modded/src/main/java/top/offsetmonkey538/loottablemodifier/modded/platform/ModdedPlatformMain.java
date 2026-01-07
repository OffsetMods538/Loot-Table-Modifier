package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;
import top.offsetmonkey538.loottablemodifier.common.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.*;

public class ModdedPlatformMain implements PlatformMain {

    @Override
    public void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException {
        GsonHelper.writeValue(jsonWriter, json, DataProvider.KEY_COMPARATOR);
    }

    @Override
    public Identifier idImpl(String path) {
        return new IdentifierWrapper(id(path));
    }

    // TODO: delete: public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, DynamicOps<JsonElement> registryOps) {
    // TODO: delete:     LOGGER.info("Gathering loot tables...");
    // TODO: delete:     final Stopwatch stopwatch = Stopwatch.createStarted();

    // TODO: delete:     final Map<Identifier, top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable> tables = RegistryUtil.getRegistryAsLookup(lootRegistry)
    // TODO: delete:             .listElements()
    // TODO: delete:             .collect(Collectors.toMap(
    // TODO: delete:                     registryEntry -> new IdentifierWrapper(registryEntry.key().location()),
    // TODO: delete:                     registryEntry -> new LootTableWrapper(RegistryUtil.getValue(lootRegistry, registryEntry.key()))
    // TODO: delete:             ));

    // TODO: delete:     LOGGER.info("Gathered %s loot tables in %s", tables.size(), stopwatch.stop());

    // TODO: delete:     LootTableModifierCommon.runModification(loadModifiers(resourceManager, registryOps), tables);
    // TODO: delete: }

    // TODO: delete: private static Map<Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, DynamicOps<JsonElement> registryOps) {
    // TODO: delete:     LOGGER.info("Loading loot table modifiers...");
    // TODO: delete:     final Stopwatch stopwatch = Stopwatch.createStarted();

    // TODO: delete:     final Map<Identifier, LootModifier> result = new HashMap<>();

    // TODO: delete:     for (Map.Entry<net.minecraft.resources.ResourceLocation, Resource> entry : resourceManager.listResources(MOD_ID + "/loot_modifier", path -> path.toString().endsWith(".json")).entrySet()) {
    // TODO: delete:         final net.minecraft.resources.ResourceLocation id = entry.getKey();

    // TODO: delete:         try {
    // TODO: delete:             LOGGER.debug("Loading load loot table modifier from '%s'", id);
    // TODO: delete:             result.put(
    // TODO: delete:                     new IdentifierWrapper(id),
    // TODO: delete:                     // Can't just use orElseThrow cause 1.20.1 don't have that
    // TODO: delete:                     LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(entry.getValue().openAsReader())).map(Pair::getFirst).resultOrPartial(error -> {throw new RuntimeException(error);}).orElseThrow()
    // TODO: delete:             );
    // TODO: delete:         } catch (Exception e) {
    // TODO: delete:             LOGGER.error("Failed to load loot table modifier from '%s'!", e, id);
    // TODO: delete:         }
    // TODO: delete:     }

    // TODO: delete:     LOGGER.info("Loaded %s loot modifiers in %s!", result.size(), stopwatch.stop());

    // TODO: delete:     return result;
    // TODO: delete: }

    public static ResourceLocation id(String path) {
        return ((IdentifierWrapper) Identifier.of(MOD_ID + ":" + path)).vanillaIdentifier();
    }
}
