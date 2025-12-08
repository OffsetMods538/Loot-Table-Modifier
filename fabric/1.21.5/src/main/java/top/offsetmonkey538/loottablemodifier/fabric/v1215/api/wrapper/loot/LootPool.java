package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper.loot.LootPoolWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface LootPool {
    Supplier<Codec<LootPool>> CODEC_PROVIDER = new LootPoolWrapper.CodecProviderImpl();

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

