package top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionType;

import java.util.ArrayList;

/**
 * Removes the matched pools from their tables
 */
public record PoolRemoveAction() implements LootModifierAction {
    public static final MapCodec<PoolRemoveAction> CODEC = Codec.of(Encoder.empty(), Decoder.unit(PoolRemoveAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.POOL_REMOVE;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootTable table = context.table();
        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        final ArrayList<LootPool> tablePools = table.getPools();
        tablePools.remove(pool);
        table.setPools(tablePools);

        return MODIFIED_POOL;
    }

    /**
     * Creates a builder for {@link PoolRemoveAction}
     *
     * @return a new {@link PoolRemoveAction.Builder}
     */
    @Contract("->new")
    public static PoolRemoveAction.Builder builder() {
        return PoolRemoveAction::new;
    }
}
