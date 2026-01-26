package top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface LootCondition {
    Supplier<Codec<LootCondition>> CODEC_PROVIDER = load(CodecProvider.class);

    @Internal
    interface CodecProvider extends Supplier<Codec<LootCondition>> {

    }
}

