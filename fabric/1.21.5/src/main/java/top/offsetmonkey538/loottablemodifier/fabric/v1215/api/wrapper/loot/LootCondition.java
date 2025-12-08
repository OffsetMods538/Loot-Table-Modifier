package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.loot.LootConditionWrapper;

import java.util.function.Supplier;

public interface LootCondition {
    Supplier<Codec<LootCondition>> CODEC_PROVIDER = new LootConditionWrapper.CodecProviderImpl();

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootCondition>> {

    }
}

