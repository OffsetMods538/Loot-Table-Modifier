package top.offsetmonkey538.loottablemodifier.common.platform;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.io.IOException;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface PlatformMain {
    @Internal
    PlatformMain INSTANCE = load(PlatformMain.class);

    static void writeSorted(JsonWriter jsonWriter, JsonElement json) throws IOException {
        INSTANCE.writeSortedImpl(jsonWriter, json);
    }

    void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException;
}
