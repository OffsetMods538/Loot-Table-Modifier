package top.offsetmonkey538.loottablemodifier.fabric.v1212.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot.LootConditionWrapper;

public final class LootConditionCodecProviderImpl implements LootCondition.CodecProvider {
    @Override
    public Codec<LootCondition> get() {
        return net.minecraft.loot.condition.LootCondition.CODEC.xmap(LootConditionWrapper::new, wrappedCondition -> ((LootConditionWrapper) wrappedCondition).vanillaCondition());
    }
}
