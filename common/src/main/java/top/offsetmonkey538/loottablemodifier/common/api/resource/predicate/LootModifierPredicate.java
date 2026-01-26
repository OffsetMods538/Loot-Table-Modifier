package top.offsetmonkey538.loottablemodifier.common.api.resource.predicate;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.AllOfPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.AnyOfPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.InvertedPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.LootModifierContext;

import java.util.function.Predicate;

/**
 * A loot modifier predicate
 */
public interface LootModifierPredicate extends Predicate<LootModifierContext> {
    /**
     * Codec containing the type id
     */
    Codec<LootModifierPredicate> CODEC = LootModifierPredicateType.CODEC_PROVIDER.get();

    /**
     * Returns the type of this action.
     *
     * @return the {@link LootModifierPredicateType type} of this action.
     */
    LootModifierPredicateType getType();

    /**
     * Tests this predicate against the provided context.
     *
     * @param context the context to match against
     * @return if this predicate matched the provided context
     */
    boolean test(final LootModifierContext context);

    /**
     * A builder for loot modifier predicates.
     */
    @FunctionalInterface
    interface Builder {
        /**
         * Builds the predicate
         *
         * @return a built {@link LootModifierPredicate}
         */
        LootModifierPredicate build();

        /**
         * Inverts this builder.
         * <br />
         * Wraps this builder in an {@link InvertedPredicate}
         *
         * @return An inverted version of this builder.
         */
        default Builder invert() {
            return InvertedPredicate.builder(this);
        }

        /**
         * Adds another predicate builder in an {@code OR} relationship.
         * <br />
         * Wraps this and the provided builder in an {@link AnyOfPredicate}
         *
         * @param otherPredicate The other predicate
         * @return A builder matching when this builder or the provided other builder match.
         */
        default LootModifierPredicate.Builder or(LootModifierPredicate.Builder otherPredicate) {
            return AnyOfPredicate.builder().or(this).or(otherPredicate);
        }

        /**
         * Adds another predicate builder in an {@code AND} relationship.
         * <br />
         * Wraps this and the provided builder in an {@link AllOfPredicate}
         *
         * @param otherPredicate The other predicate
         * @return A builder matching when this builder and the provided other builder match.
         */
        default LootModifierPredicate.Builder and(LootModifierPredicate.Builder otherPredicate) {
            return AllOfPredicate.builder().and(this).and(otherPredicate);
        }
    }
}
