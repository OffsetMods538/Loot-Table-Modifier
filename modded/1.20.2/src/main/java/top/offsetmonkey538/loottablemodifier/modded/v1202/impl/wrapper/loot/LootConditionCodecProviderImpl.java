package top.offsetmonkey538.loottablemodifier.modded.v1202.impl.wrapper.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v1202.codec.GSONCodec;

public final class LootConditionCodecProviderImpl implements LootCondition.CodecProvider {
    private static final Codec<LootCondition> CODEC = new GSONCodec<>(Deserializers.createConditionSerializer(), LootItemCondition.class)
            .xmap(LootConditionWrapper::new, wrappedCondition -> ((LootConditionWrapper) wrappedCondition).vanillaCondition());

    @Override
    public Codec<LootCondition> get() {
        return CODEC;
    }
}
