package top.offsetmonkey538.loottablemodifier.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.IdentifierWrapper;

import java.util.function.Supplier;

public interface Identifier {
    Supplier<Codec<Identifier>> CODEC_PROVIDER = new IdentifierWrapper.CodecProviderImpl();

    String asString();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Identifier>> {

    }
}

