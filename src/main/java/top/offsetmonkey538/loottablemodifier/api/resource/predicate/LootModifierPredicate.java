package top.offsetmonkey538.loottablemodifier.api.resource.predicate;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AllOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AnyOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.InvertedLootPredicate;

import java.util.function.Predicate;

public interface LootModifierPredicate extends Predicate<LootModifierContext> {
    Codec<LootModifierPredicate> CODEC = LootModifierPredicateType.REGISTRY.getCodec().dispatch(LootModifierPredicate::getType, LootModifierPredicateType::codec);

    LootModifierPredicateType getType();

    boolean test(final @NotNull LootModifierContext context);

    @FunctionalInterface
    interface Builder {
        LootModifierPredicate build();

        default Builder invert() {
            return InvertedLootPredicate.builder(this);
        }

        default LootModifierPredicate.Builder or(LootModifierPredicate.Builder otherPredicate) {
            return AnyOfLootPredicate.builder().or(this).or(otherPredicate);
        }

        default LootModifierPredicate.Builder and(LootModifierPredicate.Builder otherPredicate) {
            return AllOfLootPredicate.builder().and(this).and(otherPredicate);
        }
    }
}
