package top.offsetmonkey538.loottablemodifier.modded.duck;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.List;

public interface LootPoolDuck extends LootElementWithConditions {
    void loot_table_modifier$setEntries(List<LootPoolEntryContainer> entries);
    List<LootPoolEntryContainer> loot_table_modifier$getEntries();

    void loot_table_modifier$setFunctions(List<LootItemFunction> functions);
    List<LootItemFunction> loot_table_modifier$getFunctions();
}