package top.offsetmonkey538.loottablemodifier.modded.v1212.impl.wrapper.loot;

import net.minecraft.world.level.storage.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v1212.mixin.LootContextTypesAccessor;

public final class LootTableTypeGetterImpl implements LootTableWrapper.TypeGetter {
    @Override
    public String getType(LootTable table) {
        return LootContextTypesAccessor.getMAP().inverse().get(table.getParamSet()).toString();
    }
}
