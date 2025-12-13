package top.offsetmonkey538.loottablemodifier.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.ServiceLoader.load;

public interface Identifier {
    Supplier<Codec<Identifier>> CODEC_PROVIDER = load(CodecProvider.class);

    String asString();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Identifier>> {

    }
}

