package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.DataProvider;
import net.minecraft.util.GsonHelper;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain;

import java.io.IOException;

public class ModdedPlatformMain implements PlatformMain {

    @Override
    public void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException {
        GsonHelper.writeValue(jsonWriter, json, DataProvider.KEY_COMPARATOR);
    }
}
