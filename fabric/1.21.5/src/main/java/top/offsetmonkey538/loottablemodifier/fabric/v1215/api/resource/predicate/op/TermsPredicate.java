package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.op;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicate;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract predicate for predicates that take other predicates as terms.
 */
abstract class TermsPredicate implements LootModifierPredicate {
    /**
     * The terms of this predicate
     */
    protected final List<LootModifierPredicate> terms;
    private final Predicate<LootModifierContext> builtPredicate;

    protected TermsPredicate(final List<LootModifierPredicate> terms, final Predicate<LootModifierContext> builtPredicate) {
        this.terms = terms;
        this.builtPredicate = builtPredicate;
    }

    /**
     * Creates a codec for the terms predicate using the provided constructor.
     *
     * @param constructor the constructor of the predicate.
     * @return a codec for the terms predicate using the provided constructor
     * @param <T> the terms predicate
     */
    protected static <T extends TermsPredicate> MapCodec<T> createCodec(final Function<List<LootModifierPredicate>, T> constructor) {
        return RecordCodecBuilder.mapCodec(
                instance -> instance.group(LootModifierPredicate.CODEC.listOf().fieldOf("terms").forGetter(TermsPredicate::getTerms)).apply(instance, constructor)
        );
    }

    private List<LootModifierPredicate> getTerms() {
        return terms;
    }

    @Override
    public boolean test(final @NotNull LootModifierContext context) {
        return builtPredicate.test(context);
    }

    /**
     * Abstract builder for {@link TermsPredicate}
     */
    public abstract static class Builder implements LootModifierPredicate.Builder {
        private final ImmutableList.Builder<LootModifierPredicate> terms = ImmutableList.builder();

        protected void add(LootModifierPredicate.Builder builder) {
            this.terms.add(builder.build());
        }

        @Override
        public LootModifierPredicate build() {
            return this.build(this.terms.build());
        }

        /**
         * Builds the predicate using the provided terms.
         *
         * @param terms the terms to build the predicate with
         * @return a built predicate
         */
        protected abstract LootModifierPredicate build(List<LootModifierPredicate> terms);
    }
}
