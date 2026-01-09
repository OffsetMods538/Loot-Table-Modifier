package top.offsetmonkey538.loottablemodifier.modded.duck;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface LootTableDuck {
    void loot_table_modifier$setPools(List<LootPool> pools);
    List<LootPool> loot_table_modifier$getPools();

    void loot_table_modifier$setFunctions(List<LootItemFunction> functions);
    List<LootItemFunction> loot_table_modifier$getFunctions();
}