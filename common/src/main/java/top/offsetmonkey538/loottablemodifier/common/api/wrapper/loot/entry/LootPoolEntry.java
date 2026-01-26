package top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface LootPoolEntry {
    Supplier<Codec<LootPoolEntry>> CODEC_PROVIDER = load(CodecProvider.class);

    ArrayList<LootCondition> getConditions();
    void setConditions(List<LootCondition> conditions);

    @Internal
    interface CodecProvider extends Supplier<Codec<LootPoolEntry>> {

    }
}

