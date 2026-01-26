package top.offsetmonkey538.loottablemodifier.common.platform;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public interface PlatformCommandUtils {
    @Internal
    PlatformCommandUtils INSTANCE = load(PlatformCommandUtils.class);

    static DynamicOps<JsonElement> getRegistryOps(CommandContext<Object> context) {
        return INSTANCE.getRegistryOpsImpl(context);
    }

    static LootTable getTableForId(CommandContext<Object> context, Identifier id) {
        return INSTANCE.getTableForIdImpl(context, id);
    }

    DynamicOps<JsonElement> getRegistryOpsImpl(CommandContext<Object> context);
    LootTable getTableForIdImpl(CommandContext<Object> context, Identifier id);
}
