package top.offsetmonkey538.loottablemodifier.platform;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootTable;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public interface PlatformCommandUtils {
    @ApiStatus.Internal
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
