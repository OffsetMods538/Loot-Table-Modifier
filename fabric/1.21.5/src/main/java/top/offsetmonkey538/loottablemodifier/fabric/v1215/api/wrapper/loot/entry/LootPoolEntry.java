package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.entry;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.loot.entry.LootPoolEntryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface LootPoolEntry {
    Supplier<Codec<LootPoolEntry>> CODEC_PROVIDER = new LootPoolEntryWrapper.CodecProviderImpl();

    ArrayList<LootCondition> getConditions();
    void setConditions(List<LootCondition> conditions);

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootPoolEntry>> {

    }
}

