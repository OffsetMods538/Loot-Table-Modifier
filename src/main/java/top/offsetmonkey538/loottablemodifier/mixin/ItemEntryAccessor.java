package top.offsetmonkey538.loottablemodifier.mixin;

import net.minecraft.item.Item;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntry.class)
public interface ItemEntryAccessor {

    @Mutable
    @Accessor
    void setItem(RegistryEntry<Item> item);
}
