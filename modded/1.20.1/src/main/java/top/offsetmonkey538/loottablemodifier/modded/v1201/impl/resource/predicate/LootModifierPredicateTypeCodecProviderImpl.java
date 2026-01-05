package top.offsetmonkey538.loottablemodifier.modded.v1201.impl.resource.predicate;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.modded.impl.resource.predicate.LootModifierPredicateTypeRegistryImpl;

public final class LootModifierPredicateTypeCodecProviderImpl implements LootModifierPredicateType.CodecProvider {
    private static final Codec<LootModifierPredicate> CODEC = LootModifierPredicateTypeRegistryImpl.REGISTRY.byNameCodec().dispatch(LootModifierPredicate::getType, lootModifierPredicateType -> lootModifierPredicateType.codec().codec());

    @Override
    public Codec<LootModifierPredicate> get() {
        return CODEC;
    }
}
