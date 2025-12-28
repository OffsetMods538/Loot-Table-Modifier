package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;

public record ItemWrapper(Holder<net.minecraft.world.item.Item> vanillaItem) implements Item {

    @Override
    public String getId() {
        return BuiltInRegistries.ITEM.getKey(vanillaItem.value()).toString();
    }
}

