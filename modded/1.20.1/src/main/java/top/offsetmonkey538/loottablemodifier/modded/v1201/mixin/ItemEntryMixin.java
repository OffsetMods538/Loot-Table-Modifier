package top.offsetmonkey538.loottablemodifier.modded.v1201.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import top.offsetmonkey538.loottablemodifier.modded.duck.ItemEntryDuck;

@Mixin(LootItem.class)
public abstract class ItemEntryMixin implements ItemEntryDuck {
    @Shadow
    @Mutable
    @Final
    Item item;

    @Override
    public void loot_table_modifier$setItem(Holder<Item> itemHolder) {
        this.item = itemHolder.value();
    }

    @Override
    public String loot_table_modifier$getId() {
        return BuiltInRegistries.ITEM.getKey(this.item).toString();
    }
}
