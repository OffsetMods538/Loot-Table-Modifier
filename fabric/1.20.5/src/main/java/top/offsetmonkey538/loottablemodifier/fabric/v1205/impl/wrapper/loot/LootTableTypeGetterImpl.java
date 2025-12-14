package top.offsetmonkey538.loottablemodifier.fabric.v1205.impl.wrapper.loot;

import net.minecraft.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.fabric.v1205.mixin.LootContextTypesAccessor;

public final class LootTableTypeGetterImpl implements LootTableWrapper.TypeGetter {
    @Override
    public String apply(LootTable lootTable) {
        return LootContextTypesAccessor.getMAP().inverse().get(lootTable.getType()).toString();
    }
}
