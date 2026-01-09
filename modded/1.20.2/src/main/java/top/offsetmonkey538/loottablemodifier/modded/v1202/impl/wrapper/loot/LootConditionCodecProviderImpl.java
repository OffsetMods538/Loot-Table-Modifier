package top.offsetmonkey538.loottablemodifier.modded.v1202.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;

public final class LootConditionCodecProviderImpl implements LootCondition.CodecProvider {
    @Override
    public Codec<LootCondition> get() {
        return LootItemConditions.CODEC.xmap(LootConditionWrapper::new, wrappedCondition -> ((LootConditionWrapper) wrappedCondition).vanillaCondition());
    }
}
