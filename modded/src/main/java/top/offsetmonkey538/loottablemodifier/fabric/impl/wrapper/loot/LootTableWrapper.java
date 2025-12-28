package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootFunction;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.fabric.mixin.LootTableAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public record LootTableWrapper(net.minecraft.world.level.storage.loot.LootTable vanillaTable) implements LootTable {

    @Override
    public String getType() {
        return TypeGetter.INSTANCE.apply(vanillaTable);
    }

    @Override
    public ArrayList<LootPool> getPools() {
        return ((LootTableAccessor) vanillaTable).getPools().stream().map(LootPoolWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setPools(List<LootPool> entries) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.LootPool> newPools = ImmutableList.builder();

        newPools.addAll(entries.stream().map(wrapperEntry -> ((LootPoolWrapper) wrapperEntry).vanillaPool()).toList());

        ((LootTableAccessor) vanillaTable).setPools(newPools.build());
    }

    @Override
    public ArrayList<LootFunction> getFunctions() {
        return ((LootTableAccessor) vanillaTable).getFunctions().stream().map(LootFunctionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setFunctions(List<LootFunction> functions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.functions.LootItemFunction> newFunctions = ImmutableList.builder();

        newFunctions.addAll(functions.stream().map(wrapperEntry -> ((LootFunctionWrapper) wrapperEntry).vanillaFunction()).toList());

        ((LootTableAccessor) vanillaTable).setFunctions(newFunctions.build());
    }

    public static final class CodecProviderImpl implements LootTable.CodecProvider {
        @Override
        public Codec<LootTable> get() {
            return net.minecraft.world.level.storage.loot.LootTable.DIRECT_CODEC.xmap(LootTableWrapper::new, wrappedPool -> ((LootTableWrapper) wrappedPool).vanillaTable());
        }
    }

    public interface TypeGetter extends Function<net.minecraft.world.level.storage.loot.LootTable, String> {
        TypeGetter INSTANCE = load(TypeGetter.class);
    }
}

