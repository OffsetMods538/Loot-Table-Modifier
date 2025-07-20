package top.offsetmonkey538.loottablemodifier.api.resource.action.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.mixin.LootTableAccessor;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;

/**
 * Removes the matched pools from their tables
 */
public record RemovePoolAction() implements LootModifierAction {
    public static final MapCodec<RemovePoolAction> CODEC = Codec.of(Encoder.empty(), Decoder.unit(RemovePoolAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.POOL_REMOVE;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootTable table = context.table();
        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        final ImmutableList.Builder<LootPool> newPoolsBuilder = ImmutableList.builder();

        for (LootPool originalPool : table.pools) {
            if (originalPool == pool) continue; // I think we do want '==' here as the references should be the same?
            newPoolsBuilder.add(originalPool);
        }

        ((LootTableAccessor) table).setPools(newPoolsBuilder.build());

        return MODIFIED_POOL;
    }

    /**
     * Creates a builder for {@link RemovePoolAction}
     *
     * @return a new {@link RemovePoolAction.Builder}
     */
    @Contract("->new")
    public static RemovePoolAction.Builder builder() {
        return RemovePoolAction::new;
    }
}
