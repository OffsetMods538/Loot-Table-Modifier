package top.offsetmonkey538.loottablemodifier.api.wrapper.loot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.ServiceLoader.load;

public interface LootCondition {
    Supplier<Codec<LootCondition>> CODEC_PROVIDER = load(CodecProvider.class);

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootCondition>> {

    }
}

