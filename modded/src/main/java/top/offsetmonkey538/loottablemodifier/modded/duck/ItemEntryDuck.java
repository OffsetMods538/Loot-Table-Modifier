package top.offsetmonkey538.loottablemodifier.modded.duck;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface ItemEntryDuck {
    void loot_table_modifier$setItem(Holder<Item> itemHolder);
    String loot_table_modifier$getId();
}