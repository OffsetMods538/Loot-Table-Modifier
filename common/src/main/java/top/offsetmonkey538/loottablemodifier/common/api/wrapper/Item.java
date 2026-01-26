package top.offsetmonkey538.loottablemodifier.common.api.wrapper;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface Item {
    Supplier<Codec<Item>> CODEC_PROVIDER = load(CodecProvider.class);

    String getId();

    @Internal
    interface CodecProvider extends Supplier<Codec<Item>> {

    }
}

