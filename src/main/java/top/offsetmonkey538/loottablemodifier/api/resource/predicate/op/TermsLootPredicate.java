package top.offsetmonkey538.loottablemodifier.api.resource.predicate.op;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class TermsLootPredicate implements LootModifierPredicate {
    protected final List<LootModifierPredicate> terms;
    private final Predicate<LootModifierContext> builtPredicate;

    protected TermsLootPredicate(final List<LootModifierPredicate> terms, final Predicate<LootModifierContext> builtPredicate) {
        this.terms = terms;
        this.builtPredicate = builtPredicate;
    }

    protected static <T extends TermsLootPredicate> MapCodec<T> createCodec(final Function<List<LootModifierPredicate>, T> constructor) {
        return RecordCodecBuilder.mapCodec(
                instance -> instance.group(Codec.either(LootModifierPredicate.CODEC, LootModifierPredicate.CODEC.listOf()).fieldOf("terms").forGetter(TermsLootPredicate::eitherTerms)).apply(instance, eitherToFunction(constructor))
        );
    }

    private static <T extends TermsLootPredicate> Function<Either<LootModifierPredicate, List<LootModifierPredicate>>, T> eitherToFunction(final Function<List<LootModifierPredicate>, T> constructor) {
        return eitherTerms -> constructor.apply(eitherTerms.right().orElseGet(() -> List.of(eitherTerms.left().orElseThrow())));
    }

    private Either<LootModifierPredicate, List<LootModifierPredicate>> eitherTerms() {
        if (terms.size() == 1) return Either.left(terms.get(0));
        return Either.right(terms);
    }

    @Override
    public boolean test(final @NotNull LootModifierContext context) {
        return builtPredicate.test(context);
    }

    public abstract static class Builder implements LootModifierPredicate.Builder {
        private final ImmutableList.Builder<LootModifierPredicate> terms = ImmutableList.builder();

        protected Builder(LootModifierPredicate.Builder... terms) {
            for (LootModifierPredicate.Builder builder : terms) {
                this.terms.add(builder.build());
            }
        }

        public void add(LootModifierPredicate.Builder builder) {
            this.terms.add(builder.build());
        }

        @Override
        public LootModifierPredicate build() {
            return this.build(this.terms.build());
        }

        protected abstract TermsLootPredicate build(List<LootModifierPredicate> terms);
    }
}
