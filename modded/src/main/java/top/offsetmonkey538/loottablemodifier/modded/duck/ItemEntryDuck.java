package top.offsetmonkey538.loottablemodifier.modded.duck;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

public interface ItemEntryDuck {
    void loot_table_modifier$setItem(Holder<Item> itemHolder);
    String loot_table_modifier$getId();
}