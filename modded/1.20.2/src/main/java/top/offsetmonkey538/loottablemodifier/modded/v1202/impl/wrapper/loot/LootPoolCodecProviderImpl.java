package top.offsetmonkey538.loottablemodifier.modded.v1202.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.Deserializers;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootPoolWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v1202.codec.GSONCodec;

public final class LootPoolCodecProviderImpl implements LootPool.CodecProvider {
    private static final Codec<LootPool> CODEC = new GSONCodec<>(Deserializers.createLootTableSerializer(), net.minecraft.world.level.storage.loot.LootPool.class)
            .xmap(LootPoolWrapper::new, wrappedPool -> ((LootPoolWrapper) wrappedPool).vanillaPool());

    @Override
    public Codec<LootPool> get() {
        return CODEC;
    }
}
