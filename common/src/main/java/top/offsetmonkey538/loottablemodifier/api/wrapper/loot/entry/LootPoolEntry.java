package top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public interface LootPoolEntry {
    Supplier<Codec<LootPoolEntry>> CODEC_PROVIDER = load(CodecProvider.class);

    ArrayList<LootCondition> getConditions();
    void setConditions(List<LootCondition> conditions);

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootPoolEntry>> {

    }
}

