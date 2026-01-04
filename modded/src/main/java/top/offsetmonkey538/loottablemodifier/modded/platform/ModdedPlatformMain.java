package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
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

    @Override
    public <T> Predicate<T> allOfImpl(List<? extends Predicate<T>> predicates) {
        return Util.allOf(predicates);
        // TODO: 1.20.1 through 1.20.4:
        // 1.20.1: LootItemConditions.andConditions(FUCKASS ARRAY);
        // 1.20.2 - 1.20.4: LootItemConditions.andConditions(LIST);
        // 1.20.5+: Util.allOf(LIST)
    }

    @Override
    public <T> Predicate<T> anyOfImpl(List<? extends Predicate<T>> predicates) {
        return Util.anyOf(predicates);
    }

    public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
        LOGGER.info("Gathering loot tables...");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final Map<Identifier, top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable> tables = RegistryUtil.getRegistryAsLookup(lootRegistry)
                .listElements()
                .collect(Collectors.toMap(
                        registryEntry -> new IdentifierWrapper(registryEntry.key().location()),
                        registryEntry -> new LootTableWrapper(RegistryUtil.getValue(lootRegistry, registryEntry.key()))
                ));

        LOGGER.info("Gathered %s loot tables in %s", tables.size(), stopwatch.stop());

        LootTableModifierCommon.runModification(loadModifiers(resourceManager, registryOps), tables);
    }

    private static Map<top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, RegistryOps<JsonElement> registryOps) {
        LOGGER.info("Loading loot table modifiers...");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final Map<top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier, LootModifier> result = new HashMap<>();

        for (Map.Entry<net.minecraft.resources.ResourceLocation, Resource> entry : resourceManager.listResources(MOD_ID + "/loot_modifier", path -> path.toString().endsWith(".json")).entrySet()) {
            final net.minecraft.resources.ResourceLocation id = entry.getKey();

            try {
                LOGGER.debug("Loading load loot table modifier from '%s'", id);
                result.put(
                        new IdentifierWrapper(id),
                        LootModifier.CODEC.decode(registryOps, JsonParser.parseReader(entry.getValue().openAsReader())).getOrThrow().getFirst()
                );
            } catch (Exception e) {
                LOGGER.error("Failed to load loot table modifier from '%s'!", e, id);
            }
        }

        LOGGER.info("Loaded %s loot modifiers in %s!", result.size(), stopwatch.stop());

        return result;
    }

    public static ResourceLocation id(String path) {
        return ((IdentifierWrapper) Identifier.of(MOD_ID + ":" + path)).vanillaIdentifier();
    }

    public interface RegistryUtil {
        RegistryUtil INSTANCE = load(RegistryUtil.class);

        static <T> HolderLookup<T> getRegistryAsLookup(@NotNull Registry<T> registry) {
            return INSTANCE.getRegistryAsLookupImpl(registry);
        }
        static <T> T getValue(@NotNull Registry<T> registry, @NotNull ResourceKey<T> key) {
            return INSTANCE.getValueImpl(registry, key);
        }

        <T> HolderLookup<T> getRegistryAsLookupImpl(@NotNull Registry<T> registry);
        <T> T getValueImpl(@NotNull Registry<T> registry, @NotNull ResourceKey<T> key);
    }
}
