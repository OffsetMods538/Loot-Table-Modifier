package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper;

import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;

public record ItemWrapper(RegistryEntry<net.minecraft.item.Item> vanillaItem) implements Item {

    @Override
    public String getId() {
        return Registries.ITEM.getId(vanillaItem.value()).toString();
    }
}

