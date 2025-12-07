package top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;

public interface ItemEntry extends LootPoolEntry {
    void setItem(Item item);
    String getId();
}
