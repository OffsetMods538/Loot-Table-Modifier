package top.offsetmonkey538.loottablemodifier.resource.action;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.mixin.LootTableAccessor;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.predicate.pool.LootPoolPredicate;

import java.util.ArrayList;
import java.util.List;

public record RemovePoolAction(LootPoolPredicate poolPredicate) implements LootModifierAction {
    public static final MapCodec<RemovePoolAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootPoolPredicate.CODEC.fieldOf("poolPredicate").forGetter(RemovePoolAction::poolPredicate)
    ).apply(instance, RemovePoolAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ADD_ENTRY;
    }

    @Override
    public boolean apply(@NotNull final LootModifierContext context) {
        if (!poolPredicate.test(context)) return false;

        List<LootPool> newPools = new ArrayList<>(context.table().pools);
        newPools.remove(context.pool());
        newPools = ImmutableList.copyOf(newPools);

        ((LootTableAccessor) context.table()).setPools(newPools);

        return true;
    }

    public static LootModifierAction.Builder builder(@NotNull LootPoolPredicate.Builder poolBuilder) {
        return () -> new RemovePoolAction(poolBuilder.build());
    }
}
