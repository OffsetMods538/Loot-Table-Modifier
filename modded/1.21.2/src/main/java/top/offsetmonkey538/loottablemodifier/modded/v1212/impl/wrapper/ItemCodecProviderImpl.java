package top.offsetmonkey538.loottablemodifier.modded.v1212.impl.wrapper;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;

public final class ItemCodecProviderImpl implements Item.CodecProvider {
    @Override
    public Codec<Item> get() {
        return net.minecraft.world.item.Item.CODEC.xmap(ItemWrapper::new, wrappedItem -> ((ItemWrapper) wrappedItem).vanillaItem());
    }
}
