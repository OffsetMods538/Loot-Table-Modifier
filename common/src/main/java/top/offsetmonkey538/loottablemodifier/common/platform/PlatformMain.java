package top.offsetmonkey538.loottablemodifier.common.platform;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface PlatformMain {
    @ApiStatus.Internal
    PlatformMain INSTANCE = load(PlatformMain.class);

    static void writeSorted(JsonWriter jsonWriter, JsonElement json) throws IOException {
        INSTANCE.writeSortedImpl(jsonWriter, json);
    }

    static Identifier id(String path) {
        return INSTANCE.idImpl(path);
    }

    void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException;
    Identifier idImpl(String path);
}
