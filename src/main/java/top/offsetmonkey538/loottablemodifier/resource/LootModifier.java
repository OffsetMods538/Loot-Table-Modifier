package top.offsetmonkey538.loottablemodifier.resource;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import top.offsetmonkey538.loottablemodifier.resource.action.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;

import java.util.*;
import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.resource.LootModifierContext.MODIFIED_NONE;

// Using ArrayList as I want it to be modifiable because I empty it when applying, so I can check for things that weren't applied TODO: I don't think I do this anymore? TODO: make sure that changing it to a normal List is fine
public record LootModifier(@NotNull @UnmodifiableView List<LootModifierAction> actions, @NotNull @UnmodifiableView List<LootModifierPredicate> predicates) implements Predicate<LootModifierContext> {
//public record LootModifier(@NotNull @UnmodifiableView ArrayList<LootModifierAction> actions) {
    private static final Codec<LootModifier> LEGACY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // TODO: this should use the table predicate, once that exists
            Codec.either(LootModifierPredicate.CODEC, LootModifierPredicate.CODEC.listOf()).fieldOf("modifies").forGetter(modifier -> {
                if (modifier.predicates.size() == 1) return Either.left(modifier.predicates.get(0));
                return Either.right(modifier.predicates);
            }),
            LootPool.CODEC.listOf().optionalFieldOf("pools").forGetter(lootModifier -> Optional.empty()),
            LootPool.CODEC.listOf().optionalFieldOf("loot_pools").forGetter(lootModifier -> Optional.empty())
    ).apply(instance, LootModifier::fromLegacyCodec));

    private static final Codec<LootModifier> CURRENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(
                    LootModifierAction.CODEC,
                    LootModifierAction.CODEC.listOf()
            ).fieldOf("actions").forGetter(modifier -> {
                if (modifier.actions.size() == 1) return Either.left(modifier.actions.get(0));
                return Either.right(modifier.actions);
            }),
            Codec.either(
                    LootModifierPredicate.CODEC,
                    LootModifierPredicate.CODEC.listOf()
            ).fieldOf("predicates").forGetter(modifier -> {
                if (modifier.predicates.size() == 1) return Either.left(modifier.predicates.get(0));
                return Either.right(modifier.predicates);
            })
    ).apply(instance, LootModifier::fromCurrentCodec));

    public static final Codec<LootModifier> CODEC = Codec.either(
            LEGACY_CODEC,
            CURRENT_CODEC
    ).xmap(either -> either.map(it -> it, it -> it), Either::right); // Always encode as current codec, which is on the right.

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // From codec soo yeah
    private static LootModifier fromLegacyCodec(@NotNull Either<LootModifierPredicate, List<LootModifierPredicate>> modifiesEither, @NotNull Optional<List<LootPool>> pools, @NotNull Optional<List<LootPool>> lootPools) {
        List<LootModifierAction> actions = null;

        if (pools.isPresent() && lootPools.isPresent()) throw new IllegalStateException("Both \"pools\" and \"loot_pools\" present in loot modifier!");

        if (pools.isPresent()) actions = List.of(new AddPoolAction(pools.get()));
        if (lootPools.isPresent()) actions = List.of(new AddPoolAction(lootPools.get()));

        if (actions == null) throw new IllegalStateException("Neither \"pools\" nor \"loot_pools\" present in loot modifier!");


        return new LootModifier(
                actions,
                new ArrayList<>(modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow())))
        );
    }

    private static LootModifier fromCurrentCodec(Either<LootModifierAction, List<LootModifierAction>> actionsEither, Either<LootModifierPredicate, List<LootModifierPredicate>> predicatesEither) {
        return new LootModifier(
                actionsEither.map(List::of, it -> it),
                new ArrayList<>(predicatesEither.map(List::of, it -> it))
        );
    }

    /**
     * @param context context to modify
     * @return highest modification level from applied actions. {@link LootModifierContext#MODIFIED_NONE} when no action was applied.
     * @see LootModifierContext#MODIFIED_NONE
     * @see LootModifierContext#MODIFIED_TABLE
     * @see LootModifierContext#MODIFIED_POOL
     * @see LootModifierContext#MODIFIED_ENTRY
     */
    public int apply(final @NotNull LootModifierContext context) {
        int result = MODIFIED_NONE;
        for (LootModifierAction action : actions) {
            result = result | action.apply(context);
        }
        return result;
    }

    public boolean test(final @NotNull LootModifierContext context) {
        for (LootModifierPredicate predicate : predicates) {
            if (!predicate.test(context)) return false;
        }
        return true;
    }

    public static LootModifier.Builder builder() {
        return new LootModifier.Builder();
    }

    public static class Builder {
        private final ImmutableList.Builder<LootModifierAction> actions = ImmutableList.builder();
        private final ImmutableList.Builder<LootModifierPredicate> predicates = ImmutableList.builder();

        public LootModifier.Builder action(@NotNull LootModifierAction.Builder action) {
            this.actions.add(action.build());
            return this;
        }

        public LootModifier.Builder conditionally(@NotNull LootModifierPredicate.Builder predicate) {
            this.predicates.add(predicate.build());
            return this;
        }

        public LootModifier build() {
            return new LootModifier(this.actions.build(), this.predicates.build());
        }
    }
}
