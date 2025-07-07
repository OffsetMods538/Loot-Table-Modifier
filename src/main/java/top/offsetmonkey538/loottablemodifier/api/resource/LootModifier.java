package top.offsetmonkey538.loottablemodifier.api.resource;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AllOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.LootTablePredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;

import java.util.*;
import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.LOGGER;
import static top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction.MODIFIED_NONE;

public record LootModifier(@NotNull @UnmodifiableView List<LootModifierAction> actions, @NotNull LootModifierPredicate predicate) implements Predicate<LootModifierContext> {
    private static final Codec<LootModifier> LEGACY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(modifier -> {
                throw new IllegalStateException("Tried using legacy loot table modifier codec for serialization for some reason!");
            }),
            LootPool.CODEC.listOf().optionalFieldOf("pools").forGetter(lootModifier -> Optional.empty()),
            LootPool.CODEC.listOf().optionalFieldOf("loot_pools").forGetter(lootModifier -> Optional.empty())
    ).apply(instance, (modifiesEither, pools, lootPools) -> new LootModifier(getActionsFromLegacyCodec(pools, lootPools), getPredicateFromLegacyCodec(modifiesEither))));

    private static final Codec<LootModifier> CURRENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(
                    LootModifierAction.CODEC,
                    LootModifierAction.CODEC.listOf()
            ).fieldOf("actions").forGetter(modifier -> {
                if (modifier.actions.size() == 1) return Either.left(modifier.actions.get(0));
                return Either.right(modifier.actions);
            }),
            LootModifierPredicate.CODEC.fieldOf("predicate").forGetter(LootModifier::predicate)
    ).apply(instance, LootModifier::fromCurrentCodec));

    public static final Codec<LootModifier> CODEC = Codec.either(
            LEGACY_CODEC,
            CURRENT_CODEC
    ).xmap(either -> either.map(it -> it, it -> it), Either::right); // Always encode as current codec, which is on the right.

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // from the codec
    private static @NotNull List<LootModifierAction> getActionsFromLegacyCodec(@NotNull Optional<List<LootPool>> pools, @NotNull Optional<List<LootPool>> lootPools) {
        List<LootModifierAction> actions = null;

        if (pools.isPresent() && lootPools.isPresent()) throw new IllegalStateException("Both \"pools\" and \"loot_pools\" present in legacy loot modifier!");

        if (pools.isPresent()) actions = List.of(new AddPoolAction(pools.get()));
        if (lootPools.isPresent()) actions = List.of(new AddPoolAction(lootPools.get()));

        if (actions == null) throw new IllegalStateException("Neither \"pools\" nor \"loot_pools\" present in legacy loot modifier!");
        return actions;
    }

    private static @NotNull LootModifierPredicate getPredicateFromLegacyCodec(@NotNull Either<Identifier, List<Identifier>> modifiesEither) {
        if (modifiesEither.left().isPresent()) return LootTablePredicate.builder().name(modifiesEither.left().get()).build();


        final LootModifierPredicate.Builder predicateBuilder = AllOfLootPredicate.builder();
        for (final Identifier currentId : modifiesEither.right().orElseGet(List::of)) {
            predicateBuilder.and(LootTablePredicate.builder().name(currentId));
        }
        return predicateBuilder.build();
    }

    private static LootModifier fromCurrentCodec(Either<LootModifierAction, List<LootModifierAction>> actionsEither, LootModifierPredicate predicate) {
        return new LootModifier(
                actionsEither.map(List::of, it -> it),
                predicate
        );
    }

    /**
     * @param context context to modify
     * @return highest modification level from applied actions. {@link LootModifierAction#MODIFIED_NONE} when no action was applied.
     * @see LootModifierAction#MODIFIED_NONE
     * @see LootModifierAction#MODIFIED_TABLE
     * @see LootModifierAction#MODIFIED_POOL
     * @see LootModifierAction#MODIFIED_ENTRY
     */
    public int apply(final @NotNull LootModifierContext context) {
        int result = MODIFIED_NONE;
        for (LootModifierAction action : actions) {
            result = result | action.apply(context);
        }
        return result;
    }

    public boolean test(final @NotNull LootModifierContext context) {
        return predicate.test(context);
    }

    public static LootModifier.Builder builder() {
        return new LootModifier.Builder();
    }

    public static class Builder {
        private final ImmutableList.Builder<LootModifierAction> actions = ImmutableList.builder();
        private LootModifierPredicate predicate;

        public LootModifier.Builder action(@NotNull LootModifierAction.Builder action) {
            this.actions.add(action.build());
            return this;
        }

        public LootModifier.Builder conditionally(@NotNull LootModifierPredicate.Builder predicate) {
            if (this.predicate != null) LOGGER.error("Predicate has already been set for this builder! The previously set predicate will be overwritten! Please use the 'LootModifierPredicate.Builder#and()' and 'LootModifierPredicate.Builder#or()' methods for adding multiple conditions!");
            this.predicate = predicate.build();
            return this;
        }

        public LootModifier build() {
            return new LootModifier(this.actions.build(), this.predicate);
        }
    }
}
