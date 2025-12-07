package top.offsetmonkey538.loottablemodifier.api.resource.predicate;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AllOfPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AnyOfPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.InvertedPredicate;

import java.util.function.Predicate;

/**
 * A loot modifier predicate
 */
public interface LootModifierPredicate extends Predicate<LootModifierContext> {
    /**
     * Codec containing the type id
     */
    Codec<LootModifierPredicate> CODEC = LootModifierPredicateType.REGISTRY.getCodec().dispatch(LootModifierPredicate::getType, LootModifierPredicateType::codec);

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
    boolean test(final @NotNull LootModifierContext context);

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
        @Contract("->new")
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
        @Contract("_->new")
        default LootModifierPredicate.Builder or(@NotNull LootModifierPredicate.Builder otherPredicate) {
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
        @Contract("_->new")
        default LootModifierPredicate.Builder and(@NotNull LootModifierPredicate.Builder otherPredicate) {
            return AllOfPredicate.builder().and(this).and(otherPredicate);
        }
    }
}
