package top.offsetmonkey538.loottablemodifier.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.ItemWrapper;

import java.util.function.Supplier;

public interface Item {
    Supplier<Codec<Item>> CODEC_PROVIDER = new ItemWrapper.CodecProviderImpl();

    String getId();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Item>> {

    }
}

