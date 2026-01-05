package top.offsetmonkey538.loottablemodifier.modded.v1201.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootPoolWrapper;

public final class LootPoolCodecProviderImpl implements LootPool.CodecProvider {
    @Override
    public Codec<LootPool> get() {
        return net.minecraft.world.level.storage.loot.LootPool.CODEC.xmap(LootPoolWrapper::new, wrappedPool -> ((LootPoolWrapper) wrappedPool).vanillaPool());
    }
}
