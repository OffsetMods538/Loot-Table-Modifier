package top.offsetmonkey538.loottablemodifier.resource.action;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.mixin.ItemEntryAccessor;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;
import top.offsetmonkey538.loottablemodifier.mixin.LootTableAccessor;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;

import java.util.List;

import static top.offsetmonkey538.loottablemodifier.resource.LootModifierContext.MODIFIED_ENTRY;
import static top.offsetmonkey538.loottablemodifier.resource.LootModifierContext.MODIFIED_NONE;

public record SetItemAction(RegistryEntry<Item> item, boolean canReplaceEntry) implements LootModifierAction {
    public static final MapCodec<SetItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Item.ENTRY_CODEC.fieldOf("name").forGetter(SetItemAction::item),
            Codec.BOOL.optionalFieldOf("canReplaceEntry", false).forGetter(SetItemAction::canReplaceEntry)
    ).apply(instance, SetItemAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.SET_ITEM;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPoolEntry entry = context.entry();

        if (entry instanceof ItemEntry itemEntry) {
            ((ItemEntryAccessor) itemEntry).setItem(item);
            return MODIFIED_ENTRY;
        }
        // Matched entry is not an ItemEntry, check if entry replacing is on
        if (!canReplaceEntry) return MODIFIED_NONE;

        final LootPool pool = context.pool();

        final ImmutableList.Builder<LootPoolEntry> newEntriesBuilder = ImmutableList.builder();

        for (LootPoolEntry originalEntry : pool.entries) {
            if (originalEntry == entry) continue; // I think we do want '==' here as the references should be the same? TODO: test
            newEntriesBuilder.add(originalEntry);
        }
        ((LootPoolAccessor) context.pool()).setEntries(newEntriesBuilder.build());

        return MODIFIED_ENTRY;
    }

    public static SetItemAction.Builder builder(@NotNull ItemConvertible item) {
        return new SetItemAction.Builder(item);
    }

    public static class Builder implements LootModifierAction.Builder {
        private final RegistryEntry<Item> item;
        private boolean canReplaceEntry;

        private Builder(@NotNull ItemConvertible item) {
            this.item = Registries.ITEM.getEntry(item.asItem());
        }

        public SetItemAction.Builder setCanReplaceEntry(boolean canReplaceEntry) {
            this.canReplaceEntry = canReplaceEntry;
            return this;
        }

        @Override
        public SetItemAction build() {
            return new SetItemAction(item, canReplaceEntry);
        }
    }
}
