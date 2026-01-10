package top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;

/**
 * Matches when the provided predicate doesn't
 *
 * @param term the predicate to invert
 */
public record InvertedPredicate(LootModifierPredicate term) implements LootModifierPredicate {
    public static final MapCodec<InvertedPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(LootModifierPredicate.CODEC.fieldOf("term").forGetter(InvertedPredicate::term)).apply(instance, InvertedPredicate::new)
    );

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.INVERTED;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        return !term.test(context);
    }

    /**
     * Creates a builder for {@link InvertedPredicate}
     *
     * @param term the predicate to invert
     * @return a new {@link InvertedPredicate.Builder} containing the provided predicate
     */
    public static InvertedPredicate.Builder builder(LootModifierPredicate.Builder term) {
        return () -> new InvertedPredicate(term.build());
    }
}
