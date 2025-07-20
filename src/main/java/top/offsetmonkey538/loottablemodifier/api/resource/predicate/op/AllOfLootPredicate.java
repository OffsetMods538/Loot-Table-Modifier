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
 * Matches when all the provided predicates match
 */
public class AllOfLootPredicate extends TermsLootPredicate {
    /**
     * The codec
     */
    public static final MapCodec<AllOfLootPredicate> CODEC = createCodec(AllOfLootPredicate::new);

    private AllOfLootPredicate(final List<LootModifierPredicate> terms) {
        super(terms, Util.allOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ALL_OF;
    }

    /**
     * Creates a builder for {@link AllOfLootPredicate}
     *
     * @return a new {@link AllOfLootPredicate.Builder}
     */
    public static AllOfLootPredicate.Builder builder() {
        return new AllOfLootPredicate.Builder();
    }

    /**
     * Builder for {@link AllOfLootPredicate}
     */
    public static class Builder extends TermsLootPredicate.Builder {
        private Builder() {

        }

        @Override
        @Contract("_->this")
        public AllOfLootPredicate.Builder and(LootModifierPredicate.@NotNull Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AllOfLootPredicate build(List<LootModifierPredicate> terms) {
            return new AllOfLootPredicate(terms);
        }
    }
}
