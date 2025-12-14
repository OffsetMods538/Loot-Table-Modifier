package top.offsetmonkey538.loottablemodifier.fabric.platform;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.platform.PlatformCommandUtils;
import top.offsetmonkey538.monkeylib538.fabric.api.command.FabricCommandAbstractionApi;

public class FabricPlatformCommandUtils implements PlatformCommandUtils {
    @Override
    public DynamicOps<JsonElement> getRegistryOpsImpl(CommandContext<Object> context) {
        return RegistryOps.of(JsonOps.INSTANCE, FabricCommandAbstractionApi.get(context).getServer().getRegistryManager());
    }

    @Override
    public LootTable getTableForIdImpl(CommandContext<Object> context, Identifier id) {
        return new LootTableWrapper(FabricCommandAbstractionApi.get(context).getServer().getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, ((IdentifierWrapper) id).vanillaIdentifier())));
    }
}
