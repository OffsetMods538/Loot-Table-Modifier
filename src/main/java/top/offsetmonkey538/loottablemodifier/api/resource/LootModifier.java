package top.offsetmonkey538.loottablemodifier.api.resource;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.PoolAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.TablePredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;

import java.util.*;
import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.LOGGER;
import static top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction.MODIFIED_NONE;

/**
 * A loot modifier. Use the {@link LootModifier.Builder} for building it and the {@link LootModifierProvider} for generating the files.
 *
 * @param actions a list of actions to apply
 * @param predicate the predicate
 */
public record LootModifier(@NotNull @UnmodifiableView List<LootModifierAction> actions, @NotNull LootModifierPredicate predicate) implements Predicate<LootModifierContext> {
    private static final Codec<LootModifier> LEGACY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC_PROVIDER.get(), Identifier.CODEC_PROVIDER.get().listOf()).fieldOf("modifies").forGetter(modifier -> {
                throw new IllegalStateException("Tried using legacy loot table modifier codec for serialization for some reason!");
            }),
            LootPool.CODEC_PROVIDER.get().listOf().optionalFieldOf("pools").forGetter(lootModifier -> Optional.empty()),
            LootPool.CODEC_PROVIDER.get().listOf().optionalFieldOf("loot_pools").forGetter(lootModifier -> Optional.empty())
    ).apply(instance, (modifiesEither, pools, lootPools) -> new LootModifier(getActionsFromLegacyCodec(pools, lootPools), getPredicateFromLegacyCodec(modifiesEither))));

    private static final Codec<LootModifier> CURRENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LootModifierAction.CODEC.listOf().fieldOf("actions").forGetter(LootModifier::actions),
            LootModifierPredicate.CODEC.fieldOf("predicate").forGetter(LootModifier::predicate)
    ).apply(instance, LootModifier::new));

    public static final Codec<LootModifier> CODEC = Codec.either(
            CURRENT_CODEC,
            LEGACY_CODEC
    ).xmap(either -> either.map(it -> it, it -> it), Either::left); // Always encode as current codec, which is on the left.

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // from the codec
    private static @NotNull List<LootModifierAction> getActionsFromLegacyCodec(@NotNull Optional<List<LootPool>> pools, @NotNull Optional<List<LootPool>> lootPools) {
        List<LootModifierAction> actions = null;

        if (pools.isPresent() && lootPools.isPresent()) throw new IllegalStateException("Both \"pools\" and \"loot_pools\" present in legacy loot modifier!");

        if (pools.isPresent()) actions = List.of(new PoolAddAction(pools.get()));
        if (lootPools.isPresent()) actions = List.of(new PoolAddAction(lootPools.get()));

        if (actions == null) throw new IllegalStateException("Neither \"pools\" nor \"loot_pools\" present in legacy loot modifier!");
        return actions;
    }

    private static @NotNull LootModifierPredicate getPredicateFromLegacyCodec(@NotNull Either<Identifier, List<Identifier>> modifiesEither) {
        final TablePredicate.Builder predicateBuilder = TablePredicate.builder();
        for (final Identifier currentId : modifiesEither.map(List::of, it -> it)) {
            predicateBuilder.name(currentId.asString());
        }
        return predicateBuilder.build();
    }

    /**
     * Applied all the actions of this modifier using the provided context.
     *
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

    /**
     * Tests the predicate of this loot modifier against the provided context.
     *
     * @param context the context to match against
     * @return if the predicate of this loot modifier matched the provided context
     */
    public boolean test(final @NotNull LootModifierContext context) {
        return predicate.test(context);
    }

    /**
     * Creates a builder for {@link LootModifier}
     *
     * @return a new {@link LootModifier.Builder}
     */
    @Contract("->new")
    public static LootModifier.Builder builder() {
        return new LootModifier.Builder();
    }

    /**
     * Builder for {@link LootModifier}
     */
    public static class Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<LootModifierAction> actions = ImmutableList.builder();
        private LootModifierPredicate predicate;

        /**
         * Adds an action
         *
         * @param action the action to add
         * @return this
         */
        @Contract("_->this")
        public LootModifier.Builder action(@NotNull LootModifierAction.Builder action) {
            this.actions.add(action.build());
            return this;
        }

        /**
         * Sets the predicate
         * <br />
         * Loot modifier may only have one predicate and this may only be called once!
         *
         * @param predicate the predicate to use
         * @return this
         */
        @Contract("_->this")
        public LootModifier.Builder conditionally(@NotNull LootModifierPredicate.Builder predicate) {
            if (this.predicate != null) LOGGER.error("Predicate has already been set for this builder! The previously set predicate will be overwritten! Please use the 'LootModifierPredicate.Builder#and()' and 'LootModifierPredicate.Builder#or()' methods for adding multiple conditions!");
            this.predicate = predicate.build();
            return this;
        }

        /**
         * Builds the {@link LootModifier}
         *
         * @return a built {@link LootModifier}
         */
        public LootModifier build() {
            return new LootModifier(this.actions.build(), this.predicate);
        }
    }
}
