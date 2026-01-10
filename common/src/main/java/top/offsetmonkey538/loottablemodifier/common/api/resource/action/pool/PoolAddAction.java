package top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds the provided pools to matched tables
 *
 * @param pools the pools to add
 */
public record PoolAddAction(List<LootPool> pools) implements LootModifierAction {
    public static final MapCodec<PoolAddAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootPool.CODEC_PROVIDER.get().listOf().fieldOf("pools").forGetter(PoolAddAction::pools)
    ).apply(instance, PoolAddAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.POOL_ADD;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        if (context.tableAlreadyModified()) return MODIFIED_NONE;

        final ArrayList<LootPool> tablePools = context.table().getPools();
        tablePools.addAll(pools);
        context.table().setPools(tablePools);

        return MODIFIED_POOL;
    }

    /**
     * Creates a builder for {@link PoolAddAction}
     *
     * @return a new {@link PoolAddAction.Builder}
     */
    @Contract("->new")
    public static PoolAddAction.Builder builder() {
        return new PoolAddAction.Builder();
    }

    /**
     * Builder for {@link PoolAddAction}
     */
    public static class Builder implements LootModifierAction.Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();

        /**
         * Adds a pool
         *
         * @param poolBuilder The pool to add
         * @return this
         */
        @Contract("_->this")
        public PoolAddAction.Builder pool(LootPool poolBuilder) {
            this.pools.add(poolBuilder);
            return this;
        }

        @Override
        public PoolAddAction build() {
            return new PoolAddAction(pools.build());
        }
    }
}
