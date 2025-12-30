package top.offsetmonkey538.loottablemodifier.modded.v1205.impl;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.modded.platform.FabricPlatformMain;

//TODO: move into monkeylib? no idea what version this actually changed in so maybe some version is currenly using the wrong stuff
public final class RegistryUtilImpl implements FabricPlatformMain.RegistryUtil {
    @Override
    public <T> HolderLookup<T> getRegistryAsLookupImpl(@NotNull Registry<T> registry) {
        return registry.asLookup();
    }

    @Override
    public <T> T getValueImpl(@NotNull Registry<T> registry, @NotNull ResourceKey<T> key) {
        return registry.get(key);
    }
}
