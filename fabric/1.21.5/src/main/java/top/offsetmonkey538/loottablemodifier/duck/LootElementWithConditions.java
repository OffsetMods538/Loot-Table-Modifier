package top.offsetmonkey538.loottablemodifier.duck;

import net.minecraft.loot.condition.LootCondition;

import java.util.List;

public interface LootElementWithConditions {
    void loot_table_modifier$setConditions(List<LootCondition> conditions);
    List<LootCondition> loot_table_modifier$getConditions();
}