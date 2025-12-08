package top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.loot.entry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntryTypes;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.duck.LootElementWithConditions;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.loot.LootConditionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Don't initialize using canonical constructor. Use {@link #create(net.minecraft.loot.entry.LootPoolEntry)} instead
 */
public class LootPoolEntryWrapper implements LootPoolEntry {
    public final net.minecraft.loot.entry.LootPoolEntry vanillaEntry;

    protected LootPoolEntryWrapper(net.minecraft.loot.entry.LootPoolEntry vanillaEntry) {
        this.vanillaEntry = vanillaEntry;
    }

    public static LootPoolEntryWrapper create(net.minecraft.loot.entry.LootPoolEntry vanillaEntry) {
        if (vanillaEntry instanceof ItemEntry itemEntry) return new ItemEntryWrapper(itemEntry);
        return new LootPoolEntryWrapper(vanillaEntry);
    }

    @Override
    public ArrayList<LootCondition> getConditions() {
        return ((LootElementWithConditions) vanillaEntry).loot_table_modifier$getConditions().stream().map(LootConditionWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConditions(List<LootCondition> conditions) {
        final ImmutableList.Builder<net.minecraft.loot.condition.LootCondition> newConditions = ImmutableList.builder();

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

    public static final class CodecProviderImpl implements LootPoolEntry.CodecProvider {
        @Override
        public Codec<LootPoolEntry> get() {
            return LootPoolEntryTypes.CODEC.xmap(LootPoolEntryWrapper::new, wrappedEntry -> ((LootPoolEntryWrapper) wrappedEntry).vanillaEntry);
        }
    }
}

