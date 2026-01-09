package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.ItemEntry;
import top.offsetmonkey538.loottablemodifier.modded.duck.ItemEntryDuck;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;

public final class ItemEntryWrapper extends LootPoolEntryWrapper implements ItemEntry {
    private final net.minecraft.world.level.storage.loot.entries.LootItem vanillaEntry;

    ItemEntryWrapper(net.minecraft.world.level.storage.loot.entries.LootItem vanillaEntry) {
        super(vanillaEntry);
        this.vanillaEntry = vanillaEntry;
    }

    @Override
    public void setItem(Item item) {
        ((ItemWrapper) item).vanillaItem().value();
        ((ItemEntryDuck) this.vanillaEntry).loot_table_modifier$setItem(((ItemWrapper) item).vanillaItem());
    }

    @Override
    public String getId() {
        return ((ItemEntryDuck) this.vanillaEntry).loot_table_modifier$getId();
    }
}

