package top.offsetmonkey538.loottablemodifier.common.api.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface Identifier {
    Supplier<Codec<Identifier>> CODEC_PROVIDER = load(CodecProvider.class);
    @ApiStatus.Internal
    Function<String, Identifier> INSTANTIATOR = load(Instantiator.class);

    static Identifier of(String path) {
        return INSTANTIATOR.apply(path);
    }
    String asString();
    String getNamespace();
    String getPath();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<Identifier>> {

    }

    @ApiStatus.Internal
    interface Instantiator extends Function<String, Identifier> {

    }
}

