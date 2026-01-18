package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.RegistryOps;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformCommandUtils;
import top.offsetmonkey538.monkeylib538.modded.api.command.ModdedCommandAbstractionApi;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

public class ModdedPlatformCommandUtils implements PlatformCommandUtils {
    @Override
    public DynamicOps<JsonElement> getRegistryOpsImpl(CommandContext<Object> context) {
        return RegistryOps.create(JsonOps.INSTANCE, ModdedCommandAbstractionApi.get(context).getServer().registryAccess());
    }

    @Override
    public LootTable getTableForIdImpl(CommandContext<Object> context, Identifier id) {
        return TableGetterById.INSTANCE.getTableForIdImpl(ModdedCommandAbstractionApi.get(context), id);
    }

    public interface TableGetterById {
        TableGetterById INSTANCE = load(TableGetterById.class);
        LootTable getTableForIdImpl(CommandSourceStack source, Identifier id);
    }
}
