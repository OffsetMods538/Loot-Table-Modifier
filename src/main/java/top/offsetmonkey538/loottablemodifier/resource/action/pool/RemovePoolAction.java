package top.offsetmonkey538.loottablemodifier.resource.action.pool;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.mixin.LootTableAccessor;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierActionType;

import java.util.List;

import static top.offsetmonkey538.loottablemodifier.resource.LootModifierContext.*;

public record RemovePoolAction() implements LootModifierAction {
    public static final MapCodec<RemovePoolAction> CODEC = Codec.of(Encoder.empty(), Decoder.unit(RemovePoolAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.REMOVE_POOL;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        if (context.tableAlreadyModified()) return MODIFIED_NONE;

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

    public static RemovePoolAction.Builder builder() {
        return RemovePoolAction::new;
    }
}
