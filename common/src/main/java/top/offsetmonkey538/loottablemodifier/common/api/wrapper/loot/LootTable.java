package top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface LootTable {
    Supplier<Codec<LootTable>> CODEC_PROVIDER = load(CodecProvider.class);

    String getType();

    ArrayList<LootPool> getPools();
    void setPools(List<LootPool> entries);

    ArrayList<LootFunction> getFunctions();
    void setFunctions(List<LootFunction> functions);

    @Internal
    interface CodecProvider extends Supplier<Codec<LootTable>> {

    }
}
