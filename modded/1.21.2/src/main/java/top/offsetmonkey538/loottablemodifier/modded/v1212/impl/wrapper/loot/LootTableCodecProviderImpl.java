package top.offsetmonkey538.loottablemodifier.modded.v1212.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootPoolWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;

public final class LootTableCodecProviderImpl implements LootTable.CodecProvider {
    @Override
    public Codec<LootTable> get() {
        return net.minecraft.world.level.storage.loot.LootTable.DIRECT_CODEC.xmap(LootTableWrapper::new, wrappedPool -> ((LootTableWrapper) wrappedPool).vanillaTable());
    }
}
