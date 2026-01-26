package top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface LootPool {
    Supplier<Codec<LootPool>> CODEC_PROVIDER = load(CodecProvider.class);

    ArrayList<LootPoolEntry> getEntries();
    void setEntries(List<LootPoolEntry> entries);

    ArrayList<LootCondition> getConditions();
    void setConditions(List<LootCondition> conditions);

    ArrayList<LootFunction> getFunctions();
    void setFunctions(List<LootFunction> functions);

    @Internal
    interface CodecProvider extends Supplier<Codec<LootPool>> {

    }
}

