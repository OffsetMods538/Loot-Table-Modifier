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

import java.util.List;

public record AddPoolAction(List<LootPool> pools) implements LootModifierAction {
    public static final MapCodec<AddPoolAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootPool.CODEC.listOf().fieldOf("pools").forGetter(AddPoolAction::pools)
    ).apply(instance, AddPoolAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ADD_ENTRY;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final List<LootPool> newPools = ImmutableList.<LootPool>builder()
                .addAll(context.table().pools)
                .addAll(this.pools)
                .build();

        ((LootTableAccessor) context.table()).setPools(newPools);

        return LootModifierContext.MODIFIED_TABLE;
    }

    public static AddPoolAction.Builder builder(@NotNull LootPool.Builder poolBuilder) {
        return new AddPoolAction.Builder(poolBuilder);
    }

    public static class Builder implements LootModifierAction.Builder {
        private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();

        private Builder(@NotNull LootPool.Builder poolBuilder) {
            pools.add(poolBuilder.build());
        }

        public AddPoolAction.Builder pool(LootPool.Builder poolBuilder) {
            this.pools.add(poolBuilder.build());
            return this;
        }

        @Override
        public AddPoolAction build() {
            return new AddPoolAction(pools.build());
        }
    }
}
