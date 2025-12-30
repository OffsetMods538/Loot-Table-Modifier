package top.offsetmonkey538.loottablemodifier.fabric.platform;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.IS_DEV;
import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.MOD_ID;
import static top.offsetmonkey538.loottablemodifier.modded.platform.FabricPlatformMain.id;

public class FabricPlatformMain implements ModInitializer {
    @Override
    public void onInitialize() {
        LootTableModifierCommon.initialize();
        if (IS_DEV) ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Component.nullToEmpty("Example Pack"), ResourcePackActivationType.NORMAL);
    }
}
