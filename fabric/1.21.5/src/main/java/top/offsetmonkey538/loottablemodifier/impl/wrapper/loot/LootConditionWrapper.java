package top.offsetmonkey538.loottablemodifier.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;

public record LootConditionWrapper(net.minecraft.loot.condition.LootCondition vanillaCondition) implements LootCondition {

    public static final class CodecProviderImpl implements LootCondition.CodecProvider {
        @Override
        public Codec<LootCondition> get() {
            return net.minecraft.loot.condition.LootCondition.CODEC.xmap(LootConditionWrapper::new, wrappedCondition -> ((LootConditionWrapper) wrappedCondition).vanillaCondition());
        }
    }
}

