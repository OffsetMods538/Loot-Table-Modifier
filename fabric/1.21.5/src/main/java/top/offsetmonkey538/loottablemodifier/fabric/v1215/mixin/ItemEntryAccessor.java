package top.offsetmonkey538.loottablemodifier.fabric.v1215.mixin;

import net.minecraft.item.Item;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("MissingJavadoc")
@Mixin(ItemEntry.class)
public interface ItemEntryAccessor {
    @Accessor
    RegistryEntry<Item> getItem();

    @Mutable
    @Accessor
    void setItem(RegistryEntry<Item> item);
}
