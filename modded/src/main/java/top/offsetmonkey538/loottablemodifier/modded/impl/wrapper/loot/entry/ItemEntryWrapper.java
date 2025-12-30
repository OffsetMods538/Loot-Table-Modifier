package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.ItemEntry;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;
import top.offsetmonkey538.loottablemodifier.modded.mixin.ItemEntryAccessor;

public final class ItemEntryWrapper extends LootPoolEntryWrapper implements ItemEntry {
    private final net.minecraft.world.level.storage.loot.entries.LootItem vanillaEntry;

    ItemEntryWrapper(net.minecraft.world.level.storage.loot.entries.LootItem vanillaEntry) {
        super(vanillaEntry);
        this.vanillaEntry = vanillaEntry;
    }

    @Override
    public void setItem(Item item) {
        ((ItemEntryAccessor) this.vanillaEntry).setItem(((ItemWrapper) item).vanillaItem());
    }

    @Override
    public String getId() {
        return ((ItemEntryAccessor) this.vanillaEntry).getItem().getRegisteredName();
    }
}

