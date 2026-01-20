package top.offsetmonkey538.loottablemodifier.modded.duck;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface LootElementWithConditions {
    void loot_table_modifier$setConditions(List<LootItemCondition> conditions);
    List<LootItemCondition> loot_table_modifier$getConditions();
}