package top.offsetmonkey538.loottablemodifier.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Util;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

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

    public static AllOfLootPredicate.Builder builder() {
        return new AllOfLootPredicate.Builder();
    }

    public static class Builder extends TermsLootPredicate.Builder {
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



