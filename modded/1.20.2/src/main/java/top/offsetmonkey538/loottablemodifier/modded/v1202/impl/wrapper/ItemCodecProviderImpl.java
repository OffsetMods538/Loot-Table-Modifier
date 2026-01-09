package top.offsetmonkey538.loottablemodifier.modded.v1202.impl.wrapper;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;

public final class ItemCodecProviderImpl implements Item.CodecProvider {
    @Override
    public Codec<Item> get() {
        return BuiltInRegistries.ITEM.holderByNameCodec().xmap(ItemWrapper::new, wrappedItem -> ((ItemWrapper) wrappedItem).vanillaItem());
    }
}
