package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.entry;

import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.Item;

public interface ItemEntry extends LootPoolEntry {
    void setItem(Item item);
    String getId();
}
