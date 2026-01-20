package top.offsetmonkey538.loottablemodifier.fabric.platform;

import net.fabricmc.api.ModInitializer;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;

public class FabricPlatformMain implements ModInitializer {
    @Override
    public void onInitialize() {
        LootTableModifierCommon.initialize();
        // TODO: FIXME: if (IS_DEV) ResourceManagerHelper.registerBuiltinResourcePack(id("example_pack"), FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), Component.nullToEmpty("Example Pack"), ResourcePackActivationType.NORMAL);
    }
}
