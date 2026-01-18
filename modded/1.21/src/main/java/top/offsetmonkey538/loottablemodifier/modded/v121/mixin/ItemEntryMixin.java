package top.offsetmonkey538.loottablemodifier.modded.v121.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import top.offsetmonkey538.loottablemodifier.modded.duck.ItemEntryDuck;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.monkeylib538.modded.api.resource.ResourceKeyApi;

@Mixin(LootItem.class)
public abstract class ItemEntryMixin implements ItemEntryDuck {
    @Shadow
    @Mutable
    @Final
    private Holder<Item> item;

    @Override
    public void loot_table_modifier$setItem(Holder<Item> itemHolder) {
        this.item = itemHolder;
    }

    @Override
    public String loot_table_modifier$getId() {
        return this.item.unwrapKey().map(ResourceKeyApi::getLocation).map(Identifier::toString).orElse("[unregistered]");
    }
}
