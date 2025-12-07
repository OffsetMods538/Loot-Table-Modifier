package top.offsetmonkey538.loottablemodifier.api.resource.action.condition;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds the provided conditions to matched pools/entries
 *
 * @param conditions the conditions to add
 * @param includePools whether the conditions should be added to matched pools
 * @param includeEntries whether the conditions should be added to matched pools
 */
public record ConditionAddAction(List<LootCondition> conditions, boolean includePools, boolean includeEntries) implements LootModifierAction {
    public static final MapCodec<ConditionAddAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootCondition.CODEC_PROVIDER.get().listOf().fieldOf("conditions").forGetter(ConditionAddAction::conditions),
            Codec.BOOL.optionalFieldOf("includePools", true).forGetter(ConditionAddAction::includePools),
            Codec.BOOL.optionalFieldOf("includeEntries", true).forGetter(ConditionAddAction::includeEntries)
    ).apply(instance, ConditionAddAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.CONDITION_ADD;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        int returnValue = MODIFIED_NONE;

        if (includePools && !context.poolAlreadyModified()) {
            final ArrayList<LootCondition> poolConditions = pool.getConditions();
            poolConditions.addAll(conditions);
            pool.setConditions(poolConditions);

            returnValue |= MODIFIED_POOL;
        }


        if (!includeEntries) return returnValue;

        final LootPoolEntry entry = context.entry();
        if (entry == null) return returnValue | MODIFIED_NONE;

        final ArrayList<LootCondition> entryConditions = entry.getConditions();
        entryConditions.addAll(conditions);
        entry.setConditions(entryConditions);

        return MODIFIED_ENTRY;
    }

    /**
     * Creates a builder for {@link ConditionAddAction}
     *
     * @return a new {@link ConditionAddAction.Builder}
     */
    @Contract("->new")
    public static ConditionAddAction.Builder builder() {
        return new ConditionAddAction.Builder();
    }

    /**
     * Builder for {@link ConditionAddAction}
     */
    public static class Builder implements LootModifierAction.Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<LootCondition> conditions = ImmutableList.builder();
        private boolean includePools = true, includeEntries = true;

        /**
         * Adds a condition
         *
         * @param condition The condition to add
         * @return this
         */
        @Contract("_->this")
        public ConditionAddAction.Builder condition(LootCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        /**
         * The conditions will only be applied to matched pools
         *
         * @return this
         */
        @Contract("->this")
        public ConditionAddAction.Builder onlyPools() {
            includePools = true;
            includeEntries = false;
            return this;
        }

        /**
         * The conditions will only be applied to matched entries
         *
         * @return this
         */
        @Contract("->this")
        public ConditionAddAction.Builder onlyEntries() {
            includeEntries = true;
            includePools = false;
            return this;
        }

        @Override
        public ConditionAddAction build() {
            return new ConditionAddAction(conditions.build(), includePools, includeEntries);
        }
    }
}
