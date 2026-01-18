package top.offsetmonkey538.loottablemodifier.modded.v1212.impl;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformCommandUtils;
import top.offsetmonkey538.monkeylib538.modded.api.resource.ResourceKeyApi;

public final class TableGetterById implements ModdedPlatformCommandUtils.TableGetterById {
    @Override
    public LootTable getTableForIdImpl(CommandSourceStack source, Identifier id) {
        return new LootTableWrapper(source.getServer().reloadableRegistries().getLootTable(ResourceKeyApi.create(Registries.LOOT_TABLE, id)));
    }
}
