package top.offsetmonkey538.loottablemodifier.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

import java.util.List;

/**
 * Matches when any of the provided predicates match
 */
public class AnyOfLootPredicate extends TermsLootPredicate {
    /**
     * The codec
     */
    public static final MapCodec<AnyOfLootPredicate> CODEC = createCodec(AnyOfLootPredicate::new);

    private AnyOfLootPredicate(final List<LootModifierPredicate> terms) {
        super(terms, Util.anyOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ANY_OF;
    }

    /**
     * Creates a builder for {@link AnyOfLootPredicate}
     *
     * @return a new {@link AnyOfLootPredicate.Builder}
     */
    @Contract("->new")
    public static AnyOfLootPredicate.Builder builder() {
        return new AnyOfLootPredicate.Builder();
    }

    /**
     * Builder for {@link AnyOfLootPredicate}
     */
    public static class Builder extends TermsLootPredicate.Builder {
        private Builder() {

        }

        @Override
        @Contract("_->this")
        public AnyOfLootPredicate.Builder or(@NotNull LootModifierPredicate.Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AnyOfLootPredicate build(List<LootModifierPredicate> terms) {
            return new AnyOfLootPredicate(terms);
        }
    }
}
