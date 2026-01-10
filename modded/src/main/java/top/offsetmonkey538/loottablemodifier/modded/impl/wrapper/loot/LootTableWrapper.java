package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot;

import com.google.common.collect.ImmutableList;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootFunction;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootTableDuck;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public record LootTableWrapper(net.minecraft.world.level.storage.loot.LootTable vanillaTable) implements LootTable {

    @Override
    public String getType() {
        return TypeGetter.INSTANCE.apply(vanillaTable);
    }

    @Override
    public ArrayList<LootPool> getPools() {
        return ((LootTableDuck) vanillaTable).loot_table_modifier$getPools().stream().map(LootPoolWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setPools(List<LootPool> entries) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.LootPool> newPools = ImmutableList.builder();

        newPools.addAll(entries.stream().map(wrapperEntry -> ((LootPoolWrapper) wrapperEntry).vanillaPool()).toList());

        ((LootTableDuck) vanillaTable).loot_table_modifier$setPools(newPools.build());
    }

    @Override
    public ArrayList<LootFunction> getFunctions() {
        return ((LootTableDuck) vanillaTable).loot_table_modifier$getFunctions().stream().map(LootFunctionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setFunctions(List<LootFunction> functions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.functions.LootItemFunction> newFunctions = ImmutableList.builder();

        newFunctions.addAll(functions.stream().map(wrapperEntry -> ((LootFunctionWrapper) wrapperEntry).vanillaFunction()).toList());

        ((LootTableDuck) vanillaTable).loot_table_modifier$setFunctions(newFunctions.build());
    }

    public interface TypeGetter extends Function<net.minecraft.world.level.storage.loot.LootTable, String> {
        TypeGetter INSTANCE = load(TypeGetter.class);
    }
}

