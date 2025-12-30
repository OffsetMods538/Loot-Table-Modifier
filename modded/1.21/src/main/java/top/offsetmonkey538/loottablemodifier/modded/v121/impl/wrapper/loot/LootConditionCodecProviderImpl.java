package top.offsetmonkey538.loottablemodifier.modded.v121.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;

public final class LootConditionCodecProviderImpl implements LootCondition.CodecProvider {
    @Override
    public Codec<LootCondition> get() {
        return net.minecraft.world.level.storage.loot.predicates.LootItemCondition.DIRECT_CODEC.xmap(LootConditionWrapper::new, wrappedCondition -> ((LootConditionWrapper) wrappedCondition).vanillaCondition());
    }
}
