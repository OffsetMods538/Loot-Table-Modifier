package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.fabric.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.fabric.api.wrapper.loot.entry.ItemEntry;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.ItemWrapper;
import top.offsetmonkey538.loottablemodifier.fabric.mixin.ItemEntryAccessor;

public final class ItemEntryWrapper extends LootPoolEntryWrapper implements ItemEntry {
    private final net.minecraft.loot.entry.ItemEntry vanillaEntry;

    ItemEntryWrapper(net.minecraft.loot.entry.ItemEntry vanillaEntry) {
        super(vanillaEntry);
        this.vanillaEntry = vanillaEntry;
    }

    @Override
    public void setItem(Item item) {
        ((ItemEntryAccessor) this.vanillaEntry).setItem(((ItemWrapper) item).vanillaItem());
    }

    @Override
    public String getId() {
        return ((ItemEntryAccessor) this.vanillaEntry).getItem().getIdAsString();
    }
}

