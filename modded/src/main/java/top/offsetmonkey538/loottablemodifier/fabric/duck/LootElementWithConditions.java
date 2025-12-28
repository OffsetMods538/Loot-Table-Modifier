package top.offsetmonkey538.loottablemodifier.fabric.duck;

import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface LootElementWithConditions {
    void loot_table_modifier$setConditions(List<LootItemCondition> conditions);
    List<LootItemCondition> loot_table_modifier$getConditions();
}