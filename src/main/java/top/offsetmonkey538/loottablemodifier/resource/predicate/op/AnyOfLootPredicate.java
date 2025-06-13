package top.offsetmonkey538.loottablemodifier.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Util;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;

import java.util.List;

public class AnyOfLootPredicate extends TermsLootPredicate {
    public static final MapCodec<AnyOfLootPredicate> CODEC = createCodec(AnyOfLootPredicate::new);

    private AnyOfLootPredicate(final List<LootModifierPredicate> terms) {
        super(terms, Util.anyOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ANY_OF;
    }

    public static AnyOfLootPredicate.Builder builder(LootModifierPredicate.Builder... terms) {
        return new AnyOfLootPredicate.Builder(terms);
    }

    public static class Builder extends TermsLootPredicate.Builder {
        public Builder(LootModifierPredicate.Builder... builders) {
            super(builders);
        }

        @Override
        public LootModifierPredicate.Builder or(LootModifierPredicate.Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AnyOfLootPredicate build(List<LootModifierPredicate> terms) {
            return new AnyOfLootPredicate(terms);
        }
    }
}



