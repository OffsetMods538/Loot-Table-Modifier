package top.offsetmonkey538.loottablemodifier.modded.v1202.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.world.level.storage.loot.Deserializers;

public final class GSONCodec<C> implements Codec<C> {
    private final Gson gson;
    private final Class<C> clazz;

    public GSONCodec(final GsonBuilder gsonBuilder, final Class<C> clazz) {
        this.gson = gsonBuilder.create();
        this.clazz = clazz;
    }

    @Override
    public <T> DataResult<Pair<C, T>> decode(DynamicOps<T> ops, T input) {
        try {
            return DataResult.success(Pair.of(
                    gson.fromJson(ops.convertTo(JsonOps.INSTANCE, input), clazz),
                    input
            ));
        } catch (JsonSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }

    @Override
    public <T> DataResult<T> encode(C input, DynamicOps<T> ops, T prefix) {
        try {
            return ops.getMap(JsonOps.INSTANCE.convertTo(ops, gson.toJsonTree(input)))
                    .flatMap(map -> ops.mergeToMap(prefix, map));
        } catch (JsonSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }
}
