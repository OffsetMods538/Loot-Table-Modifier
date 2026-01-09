package top.offsetmonkey538.loottablemodifier.modded.v1202.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.Deserializers;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v1202.codec.GSONCodec;

public final class LootTableCodecProviderImpl implements LootTable.CodecProvider {
    private static final Codec<LootTable> CODEC = new GSONCodec<>(Deserializers.createLootTableSerializer(), net.minecraft.world.level.storage.loot.LootTable.class)
            .xmap(LootTableWrapper::new, wrappedPool -> ((LootTableWrapper) wrappedPool).vanillaTable());

    @Override
    public Codec<LootTable> get() {
        return CODEC;
    }
}
