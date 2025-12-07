package top.offsetmonkey538.loottablemodifier.impl.wrapper.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootFunction;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.duck.LootElementWithConditions;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.loot.entry.LootPoolEntryWrapper;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record LootPoolWrapper(net.minecraft.loot.LootPool vanillaPool) implements LootPool {
    @Override
    public ArrayList<LootPoolEntry> getEntries() {
        return vanillaPool.entries.stream().map(LootPoolEntryWrapper::create).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setEntries(List<LootPoolEntry> entries) {
        final ImmutableList.Builder<net.minecraft.loot.entry.LootPoolEntry> newEntries = ImmutableList.builder();

        newEntries.addAll(entries.stream().map(wrapperEntry -> ((LootPoolEntryWrapper) wrapperEntry).vanillaEntry).toList());

        ((LootPoolAccessor) vanillaPool).setEntries(newEntries.build());
    }

    @Override
    public ArrayList<LootCondition> getConditions() {
        return vanillaPool.conditions.stream().map(LootConditionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConditions(List<LootCondition> conditions) {
        final ImmutableList.Builder<net.minecraft.loot.condition.LootCondition> newConditions = ImmutableList.builder();

        newConditions.addAll(conditions.stream().map(wrapperEntry -> ((LootConditionWrapper) wrapperEntry).vanillaCondition()).toList());

        ((LootElementWithConditions) vanillaPool).loot_table_modifier$setConditions(newConditions.build());
    }

    @Override
    public ArrayList<LootFunction> getFunctions() {
        return vanillaPool.functions.stream().map(LootFunctionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setFunctions(List<LootFunction> functions) {
        final ImmutableList.Builder<net.minecraft.loot.function.LootFunction> newFunctions = ImmutableList.builder();

        newFunctions.addAll(functions.stream().map(wrapperEntry -> ((LootFunctionWrapper) wrapperEntry).vanillaFunction()).toList());

        ((LootPoolAccessor) vanillaPool).setFunctions(newFunctions.build());
    }

    public static final class CodecProviderImpl implements LootPool.CodecProvider {
        @Override
        public Codec<LootPool> get() {
            return net.minecraft.loot.LootPool.CODEC.xmap(LootPoolWrapper::new, wrappedPool -> ((LootPoolWrapper) wrappedPool).vanillaPool());
        }
    }
}

