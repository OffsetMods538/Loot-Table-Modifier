package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot;

import com.google.common.collect.ImmutableList;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootFunction;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootPoolDuck;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record LootPoolWrapper(net.minecraft.world.level.storage.loot.LootPool vanillaPool) implements LootPool {
    @Override
    public ArrayList<LootPoolEntry> getEntries() {
        return ((LootPoolDuck) vanillaPool).loot_table_modifier$getEntries().stream().map(LootPoolEntryWrapper::create).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setEntries(List<LootPoolEntry> entries) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer> newEntries = ImmutableList.builder();

        newEntries.addAll(entries.stream().map(wrapperEntry -> ((LootPoolEntryWrapper) wrapperEntry).vanillaEntry).toList());

        ((LootPoolDuck) vanillaPool).loot_table_modifier$setEntries(newEntries.build());
    }

    @Override
    public ArrayList<LootCondition> getConditions() {
        return ((LootPoolDuck) vanillaPool).loot_table_modifier$getConditions().stream().map(LootConditionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConditions(List<LootCondition> conditions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> newConditions = ImmutableList.builder();

        newConditions.addAll(conditions.stream().map(wrapperEntry -> ((LootConditionWrapper) wrapperEntry).vanillaCondition()).toList());

        ((LootPoolDuck) vanillaPool).loot_table_modifier$setConditions(newConditions.build());
    }

    @Override
    public ArrayList<LootFunction> getFunctions() {
        return ((LootPoolDuck) vanillaPool).loot_table_modifier$getFunctions().stream().map(LootFunctionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setFunctions(List<LootFunction> functions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.functions.LootItemFunction> newFunctions = ImmutableList.builder();

        newFunctions.addAll(functions.stream().map(wrapperEntry -> ((LootFunctionWrapper) wrapperEntry).vanillaFunction()).toList());

        ((LootPoolDuck) vanillaPool).loot_table_modifier$setFunctions(newFunctions.build());
    }
}

