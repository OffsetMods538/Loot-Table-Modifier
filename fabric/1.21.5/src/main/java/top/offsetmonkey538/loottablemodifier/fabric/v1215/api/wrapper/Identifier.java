package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.IdentifierWrapper;

import java.util.function.Supplier;

public interface Identifier {
    Supplier<Codec<Identifier>> CODEC_PROVIDER = new IdentifierWrapper.CodecProviderImpl();

    String asString();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Identifier>> {

    }
}

