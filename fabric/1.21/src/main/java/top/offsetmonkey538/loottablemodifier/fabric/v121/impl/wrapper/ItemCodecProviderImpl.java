package top.offsetmonkey538.loottablemodifier.fabric.v121.impl.wrapper;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.ItemWrapper;

public final class ItemCodecProviderImpl implements Item.CodecProvider {
    @Override
    public Codec<Item> get() {
        return Registries.ITEM.getEntryCodec().xmap(ItemWrapper::new, wrappedItem -> ((ItemWrapper) wrappedItem).vanillaItem());
    }
}
