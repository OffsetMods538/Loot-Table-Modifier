package top.offsetmonkey538.loottablemodifier.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.ServiceLoader.load;

public interface Item {
    Supplier<Codec<Item>> CODEC_PROVIDER = load(CodecProvider.class);

    String getId();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Item>> {

    }
}

