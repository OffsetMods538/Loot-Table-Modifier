package top.offsetmonkey538.loottablemodifier.api.resource.action.entry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.mixin.ItemEntryAccessor;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;

/**
 * Sets the item in matched item entries
 *
 * @param item the new item to replace the existing one with
 * @param canReplaceEntry if other types of entries can be replaced with a basic item entry containing the target item
 */
public record SetItemAction(RegistryEntry<Item> item, boolean canReplaceEntry) implements LootModifierAction {
    public static final MapCodec<SetItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Item.ENTRY_CODEC.fieldOf("name").forGetter(SetItemAction::item),
            Codec.BOOL.optionalFieldOf("canReplaceEntry", false).forGetter(SetItemAction::canReplaceEntry)
    ).apply(instance, SetItemAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_ITEM_SET;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPoolEntry entry = context.entry();
        if (entry == null) return MODIFIED_NONE;

        if (entry instanceof ItemEntry itemEntry) {
            ((ItemEntryAccessor) itemEntry).setItem(item);
            return MODIFIED_ENTRY;
        }
        // Matched entry is not an ItemEntry, check if entry replacing is on
        if (!canReplaceEntry) return MODIFIED_NONE;

        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        final ImmutableList.Builder<LootPoolEntry> newEntriesBuilder = ImmutableList.builder();

        for (LootPoolEntry originalEntry : pool.entries) {
            if (originalEntry == entry) continue; // I think we do want '==' here as the references should be the same?
            newEntriesBuilder.add(originalEntry);
        }
        ((LootPoolAccessor) pool).setEntries(newEntriesBuilder.build());

        return MODIFIED_ENTRY;
    }

    /**
     * Creates a builder for {@link SetItemAction}
     *
     * @param item the new item to replace the existing one with
     * @return a new {@link SetItemAction.Builder}
     */
    @Contract("_->new")
    public static SetItemAction.Builder builder(@NotNull ItemConvertible item) {
        return new SetItemAction.Builder(item);
    }

    /**
     * Builder for {@link SetItemAction}
     */
    public static class Builder implements LootModifierAction.Builder {
        private final RegistryEntry<Item> item;
        private boolean canReplaceEntry;

        private Builder(@NotNull ItemConvertible item) {
            this.item = Registries.ITEM.getEntry(item.asItem());
        }

        /**
         * Sets if other types of entries can be replaced with a basic item entry containing the target item
         *
         * @param canReplaceEntry if other types of entries can be replaced with a basic item entry containing the target item
         * @return this
         */
        @Contract("_->this")
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
