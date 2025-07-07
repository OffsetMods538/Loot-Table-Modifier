package top.offsetmonkey538.loottablemodifier.api.resource.action.entry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;

import java.util.List;

public record AddEntryAction(List<LootPoolEntry> entries) implements LootModifierAction {
    public static final MapCodec<AddEntryAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootPoolEntryTypes.CODEC.listOf().fieldOf("entries").forGetter(AddEntryAction::entries)
    ).apply(instance, AddEntryAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_ADD;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        if (context.poolAlreadyModified()) return MODIFIED_NONE;

        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        final List<LootPoolEntry> newEntries = ImmutableList.<LootPoolEntry>builder()
                .addAll(pool.entries)
                .addAll(this.entries)
                .build();

        ((LootPoolAccessor) pool).setEntries(newEntries);

        return MODIFIED_ENTRY;
    }

    public static AddEntryAction.Builder builder() {
        return new AddEntryAction.Builder();
    }

    public static class Builder implements LootModifierAction.Builder {
        private final ImmutableList.Builder<LootPoolEntry> entries = ImmutableList.builder();

        public AddEntryAction.Builder entry(LootPoolEntry.Builder<?> poolBuilder) {
            this.entries.add(poolBuilder.build());
            return this;
        }

        @Override
        public AddEntryAction build() {
            return new AddEntryAction(entries.build());
        }
    }
}
