package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicateType;

import java.util.List;

/**
 * Matches when all the provided predicates match
 */
public class AllOfPredicate extends TermsPredicate {
    /**
     * The codec
     */
    public static final MapCodec<AllOfPredicate> CODEC = createCodec(AllOfPredicate::new);

    private AllOfPredicate(final List<LootModifierPredicate> terms) {
        super(terms, Util.allOf(terms));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ALL_OF;
    }

    /**
     * Creates a builder for {@link AllOfPredicate}
     *
     * @return a new {@link AllOfPredicate.Builder}
     */
    public static AllOfPredicate.Builder builder() {
        return new AllOfPredicate.Builder();
    }

    /**
     * Builder for {@link AllOfPredicate}
     */
    public static class Builder extends TermsPredicate.Builder {
        private Builder() {

        }

        @Override
        @Contract("_->this")
        public AllOfPredicate.Builder and(LootModifierPredicate.@NotNull Builder builder) {
            this.add(builder);
            return this;
        }

        @Override
        protected AllOfPredicate build(List<LootModifierPredicate> terms) {
            return new AllOfPredicate(terms);
        }
    }
}
