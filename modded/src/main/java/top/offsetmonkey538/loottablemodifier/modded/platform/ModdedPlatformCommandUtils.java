package top.offsetmonkey538.loottablemodifier.modded.platform;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformCommandUtils;
import top.offsetmonkey538.monkeylib538.modded.api.command.ModdedCommandAbstractionApi;

public class ModdedPlatformCommandUtils implements PlatformCommandUtils {
    @Override
    public DynamicOps<JsonElement> getRegistryOpsImpl(CommandContext<Object> context) {
        return RegistryOps.create(JsonOps.INSTANCE, ModdedCommandAbstractionApi.get(context).getServer().registryAccess());
    }

    @Override
    public LootTable getTableForIdImpl(CommandContext<Object> context, Identifier id) {
        return new LootTableWrapper(ModdedCommandAbstractionApi.get(context).getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ((IdentifierWrapper) id).vanillaIdentifier())));
    }
}
