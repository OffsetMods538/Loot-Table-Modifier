package top.offsetmonkey538.loottablemodifier.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Util;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;

import java.util.List;

public class AllOfLootPredicate extends TermsLootPredicate {
    public static final MapCodec<AllOfLootPredicate> CODEC = createCodec(AllOfLootPredicate::new);

    private AllOfLootPredicate(final List<LootModifierPredicate> terms) {
        super(terms, Util.allOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ALL_OF;
    }

    public static AllOfLootPredicate.Builder builder(LootModifierPredicate.Builder... terms) {
        return new AllOfLootPredicate.Builder(terms);
    }

    public static class Builder extends TermsLootPredicate.Builder {
        public Builder(LootModifierPredicate.Builder... builders) {
            super(builders);
        }

        @Override
        public LootModifierPredicate.Builder and(LootModifierPredicate.Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AllOfLootPredicate build(List<LootModifierPredicate> terms) {
            return new AllOfLootPredicate(terms);
        }
    }
}



