package top.offsetmonkey538.loottablemodifier.impl.wrapper;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Item;

public record ItemWrapper(RegistryEntry<net.minecraft.item.Item> vanillaItem) implements Item {

    @Override
    public String getId() {
        return Registries.ITEM.getId(vanillaItem.value()).toString();
    }

    public static final class CodecProviderImpl implements CodecProvider {
        @Override
        public Codec<Item> get() {
            return net.minecraft.item.Item.ENTRY_CODEC.xmap(ItemWrapper::new, wrappedItem -> ((ItemWrapper) wrappedItem).vanillaItem());
        }
    }
}

