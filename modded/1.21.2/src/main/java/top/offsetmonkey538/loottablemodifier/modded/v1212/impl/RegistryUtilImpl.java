package top.offsetmonkey538.loottablemodifier.modded.v1212.impl;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.modded.platform.FabricPlatformMain;

public final class RegistryUtilImpl implements FabricPlatformMain.RegistryUtil {
    @Override
    public <T> HolderLookup<T> getRegistryAsLookupImpl(@NotNull Registry<T> registry) {
        return registry;
    }

    @Override
    public <T> T getValueImpl(@NotNull Registry<T> registry, @NotNull ResourceKey<T> key) {
        return registry.getValue(key);
    }
}
