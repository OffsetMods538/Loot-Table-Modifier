package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain;

import java.io.IOException;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.*;

public class ModdedPlatformMain implements PlatformMain {

    @Override
    public void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException {
        GsonHelper.writeValue(jsonWriter, json, DataProvider.KEY_COMPARATOR);
    }

    @Override
    public Identifier idImpl(String path) {
        return new IdentifierWrapper(id(path));
    }

    public static ResourceLocation id(String path) {
        return ((IdentifierWrapper) Identifier.of(MOD_ID + ":" + path)).vanillaIdentifier();
    }
}
