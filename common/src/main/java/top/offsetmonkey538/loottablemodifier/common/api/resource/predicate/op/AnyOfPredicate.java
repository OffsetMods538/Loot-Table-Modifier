package top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.common.util.PredicateUtils;

import java.util.List;

/**
 * Matches when any of the provided predicates match
 */
public class AnyOfPredicate extends TermsPredicate {
    /**
     * The codec
     */
    public static final MapCodec<AnyOfPredicate> CODEC = createCodec(AnyOfPredicate::new);

    private AnyOfPredicate(final List<LootModifierPredicate> terms) {
        super(terms, PredicateUtils.anyOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ANY_OF;
    }

    /**
     * Creates a builder for {@link AnyOfPredicate}
     *
     * @return a new {@link AnyOfPredicate.Builder}
     */
    @Contract("->new")
    public static AnyOfPredicate.Builder builder() {
        return new AnyOfPredicate.Builder();
    }

    /**
     * Builder for {@link AnyOfPredicate}
     */
    public static class Builder extends TermsPredicate.Builder {
        private Builder() {

        }

        @Override
        @Contract("_->this")
        public AnyOfPredicate.Builder or(@NotNull LootModifierPredicate.Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AnyOfPredicate build(List<LootModifierPredicate> terms) {
            return new AnyOfPredicate(terms);
        }
    }
}
