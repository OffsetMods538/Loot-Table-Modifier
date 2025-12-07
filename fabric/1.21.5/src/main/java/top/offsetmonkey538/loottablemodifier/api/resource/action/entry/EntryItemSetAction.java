package top.offsetmonkey538.loottablemodifier.api.resource.action.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.ItemEntry;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.IS_DEV;
import static top.offsetmonkey538.loottablemodifier.LootTableModifier.LOGGER;

/**
 * Sets the item in matched item entries
 *
 * @param item the new item to replace the existing one with
 */
public record EntryItemSetAction(Item item) implements LootModifierAction {
    public static final MapCodec<EntryItemSetAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Item.CODEC_PROVIDER.get().fieldOf("name").forGetter(EntryItemSetAction::item)
    ).apply(instance, EntryItemSetAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_ITEM_SET;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPoolEntry entry = context.entry();
        if (entry == null) return MODIFIED_NONE;

        if (entry instanceof ItemEntry itemEntry) {
            itemEntry.setItem(item);
            return MODIFIED_ENTRY;
        }

        // Matched entry is not an ItemEntry
        LOGGER.error("loot-table-modifier:entry_item_set action matched for non-item entry!");
        if (IS_DEV) throw new IllegalStateException("loot-table-modifier:entry_item_set action matched for non-item entry!");

        return MODIFIED_NONE;
    }

    /**
     * Creates a builder for {@link EntryItemSetAction}
     *
     * @param item the new item to replace the existing one with
     * @return a new {@link EntryItemSetAction.Builder}
     */
    @Contract("_->new")
    public static EntryItemSetAction.Builder builder(@NotNull Item item) {
        return () -> new EntryItemSetAction(item);
    }
}
