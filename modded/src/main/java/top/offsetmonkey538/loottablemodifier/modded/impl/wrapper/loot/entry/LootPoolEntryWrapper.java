package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry;

import com.google.common.collect.ImmutableList;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootElementWithConditions;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.storage.loot.entries.LootItem;

/**
 * Don't initialize using canonical constructor. Use {@link #create(net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer)} instead
 */
public class LootPoolEntryWrapper implements LootPoolEntry {
    public final net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer vanillaEntry;

    protected LootPoolEntryWrapper(net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer vanillaEntry) {
        this.vanillaEntry = vanillaEntry;
    }

    public static LootPoolEntryWrapper create(net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer vanillaEntry) {
        if (vanillaEntry instanceof LootItem itemEntry) return new ItemEntryWrapper(itemEntry);
        return new LootPoolEntryWrapper(vanillaEntry);
    }

    @Override
    public ArrayList<LootCondition> getConditions() {
        return ((LootElementWithConditions) vanillaEntry).loot_table_modifier$getConditions().stream().map(LootConditionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConditions(List<LootCondition> conditions) {
        final ImmutableList.Builder<net.minecraft.world.level.storage.loot.predicates.LootItemCondition> newConditions = ImmutableList.builder();

        newConditions.addAll(conditions.stream().map(wrapperCondition -> ((LootConditionWrapper) wrapperCondition).vanillaCondition()).toList());

        ((LootElementWithConditions) vanillaEntry).loot_table_modifier$setConditions(newConditions.build());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof LootPoolEntryWrapper that)) return false;

        return vanillaEntry.equals(that.vanillaEntry);
    }

    @Override
    public int hashCode() {
        return vanillaEntry.hashCode();
    }
}

