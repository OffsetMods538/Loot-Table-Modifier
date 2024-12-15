package top.offsetmonkey538.loottablemodifier.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public final class ModRegistries {
    private ModRegistries() {

    }

    public static final RegistryKey<Registry<LootModifier>> LOOT_MODIFIER = register(RegistryKey.ofRegistry(id("loot_modifier")), LootModifier.CODEC);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Registry<O>, O> RegistryKey<T> register(RegistryKey<T> registryKey, Codec<O> codec) {
        DynamicRegistries.register(registryKey, codec);
        return registryKey;
    }

    public static void register() {
        // Registers registries by loading the class
    }
}
