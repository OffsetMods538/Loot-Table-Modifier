package top.offsetmonkey538.loottablemodifier.api.wrapper.loot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public interface LootPool {
    Supplier<Codec<LootPool>> CODEC_PROVIDER = load(CodecProvider.class);

    ArrayList<LootPoolEntry> getEntries();
    void setEntries(List<LootPoolEntry> entries);

    ArrayList<LootCondition> getConditions();
    void setConditions(List<LootCondition> conditions);

    ArrayList<LootFunction> getFunctions();
    void setFunctions(List<LootFunction> functions);

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootPool>> {

    }
}

