package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootFunction;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootElementWithConditions;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;
import top.offsetmonkey538.loottablemodifier.modded.mixin.LootPoolAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record LootPoolWrapper(net.minecraft.world.level.storage.loot.LootPool vanillaPool) implements LootPool {
    @Override
    public ArrayList<LootPoolEntry> getEntries() {
        return ((LootPoolAccessor) vanillaPool).getEntries().stream().map(LootPoolEntryWrapper::create).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setEntries(List<LootPoolEntry> entries) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer> newEntries = ImmutableList.builder();

        newEntries.addAll(entries.stream().map(wrapperEntry -> ((LootPoolEntryWrapper) wrapperEntry).vanillaEntry).toList());

        ((LootPoolAccessor) vanillaPool).setEntries(newEntries.build());
    }

    @Override
    public ArrayList<LootCondition> getConditions() {
        return ((LootElementWithConditions) vanillaPool).loot_table_modifier$getConditions().stream().map(LootConditionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConditions(List<LootCondition> conditions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> newConditions = ImmutableList.builder();

        newConditions.addAll(conditions.stream().map(wrapperEntry -> ((LootConditionWrapper) wrapperEntry).vanillaCondition()).toList());

        ((LootElementWithConditions) vanillaPool).loot_table_modifier$setConditions(newConditions.build());
    }

    @Override
    public ArrayList<LootFunction> getFunctions() {
        return ((LootPoolAccessor) vanillaPool).getFunctions().stream().map(LootFunctionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setFunctions(List<LootFunction> functions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.functions.LootItemFunction> newFunctions = ImmutableList.builder();

        newFunctions.addAll(functions.stream().map(wrapperEntry -> ((LootFunctionWrapper) wrapperEntry).vanillaFunction()).toList());

        ((LootPoolAccessor) vanillaPool).setFunctions(newFunctions.build());
    }
}

