package top.offsetmonkey538.loottablemodifier.api.wrapper.loot;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public interface LootTable {
    Supplier<Codec<LootTable>> CODEC_PROVIDER = load(CodecProvider.class);

    String getType();

    ArrayList<LootPool> getPools();
    void setPools(List<LootPool> entries);

    ArrayList<LootFunction> getFunctions();
    void setFunctions(List<LootFunction> functions);

    @ApiStatus.Internal
    interface CodecProvider extends Supplier<Codec<LootTable>> {

    }
}
