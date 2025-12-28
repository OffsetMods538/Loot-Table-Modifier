package top.offsetmonkey538.loottablemodifier.fabric.platform;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.LootTableModifierCommon;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.platform.PlatformMain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.LOGGER;
import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.MOD_ID;

public class FabricPlatformMain implements PlatformMain, ModInitializer {
    @Override
    public void onInitialize() {
        LootTableModifierCommon.initialize();
    }

    @Override
    public void registerExamplePackImpl() {
        ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Component.nullToEmpty("Example Pack"), ResourcePackActivationType.NORMAL);
    }

    @Override
    public void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException {
        GsonHelper.writeValue(jsonWriter, json, DataProvider.KEY_COMPARATOR);
    }

    @Override
    public Identifier idImpl(String path) {
        return new IdentifierWrapper(id(path));
    }

    @Override
    public <T> Predicate<T> allOfImpl(List<? extends Predicate<? super T>> predicates) {
        return Util.allOf(predicates);
    }

    @Override
    public <T> Predicate<T> anyOfImpl(List<? extends Predicate<? super T>> predicates) {
        return Util.anyOf(predicates);
    }

    public static void runModification(ResourceManager resourceManager, Registry<LootTable> lootRegistry, RegistryOps<JsonElement> registryOps) {
        LOGGER.info("Gathering loot tables...");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final Map<Identifier, top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootTable> tables = getRegistryAsWrapper(lootRegistry)
                .listElements()
                .collect(Collectors.toMap(
                        registryEntry -> new IdentifierWrapper(registryEntry.key().location()),
                        registryEntry -> new LootTableWrapper(lootRegistry.getValue(registryEntry.key()))
                ));

        LOGGER.info("Gathered %s loot tables in %s", tables.size(), stopwatch.stop());

        LootTableModifierCommon.runModification(loadModifiers(resourceManager, registryOps), tables);
    }

    private static Map<top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier, LootModifier> loadModifiers(ResourceManager resourceManager, RegistryOps<JsonElement> registryOps) {
        LOGGER.info("Loading loot table modifiers...");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final Map<top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier, LootModifier> result = new HashMap<>();

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

    public static net.minecraft.resources.ResourceLocation id(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /* TODO: separate modules implementing this?
    In 1.21.4, the 'Registry' class extends 'RegistryWrapper' and inherits the 'streamEntries' method from it.
    In 1.20.5, the 'Registry' class *doesn't* extend the 'RegistryWrapper' and implements its own 'streamEntries' method.
    Compiling on both versions works, because the names of the methods are the same, but they compile to different intermediary names, thus a jar compiled for 1.20.5 doesn't work on 1.21.4 and vice versa.
    Solution: Turn the 'Registry' into a 'RegistryWrapper' as its 'streamEntries' retains the same intermediary on both versions.
    If 'Registry' implements 'RegistryWrapper': cast it
    Else: call 'getReadOnlyWrapper' on the registry (doesn't exist on 1.21.4, otherwise would've used 'registry.getReadOnlyWrapper().streamEntries()')
     */
    private static <T> HolderLookup<T> getRegistryAsWrapper(@NotNull Registry<T> registry) {
        //noinspection ConstantValue,RedundantSuppression: On lower versions, Registry doesn't extend RegistryWrapper and thus the 'isAssignableFrom' check can be false. The redundant supression is for the unchecked cast below.
        if (HolderLookup.class.isAssignableFrom(registry.getClass()))
            //noinspection unchecked,RedundantCast: I swear it casts 🤞
            return (HolderLookup<T>) registry;

        try {
            //noinspection unchecked: Seriously I swear 🤞🤞
            return (HolderLookup<T>) registry.getClass().getDeclaredMethod(FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_2378", "method_46771", "()Lnet/minecraft/class_7225$class_7226;")).invoke(registry);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
