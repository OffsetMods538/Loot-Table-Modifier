package top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Item;

public interface ItemEntry extends LootPoolEntry {
    void setItem(Item item);
    String getId();
}
