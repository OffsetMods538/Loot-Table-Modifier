package top.offsetmonkey538.loottablemodifier.api.resource.predicate.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

public record InvertedLootPredicate(LootModifierPredicate term) implements LootModifierPredicate {
    public static final MapCodec<InvertedLootPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(LootModifierPredicate.CODEC.fieldOf("term").forGetter(InvertedLootPredicate::term)).apply(instance, InvertedLootPredicate::new)
    );

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.INVERTED;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        return !term.test(context);
    }

    public static LootModifierPredicate.Builder builder(LootModifierPredicate.Builder term) {
        return () -> new InvertedLootPredicate(term.build());
    }
}
