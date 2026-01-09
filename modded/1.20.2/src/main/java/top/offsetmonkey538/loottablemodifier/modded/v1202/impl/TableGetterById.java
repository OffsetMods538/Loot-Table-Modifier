package top.offsetmonkey538.loottablemodifier.modded.v1202.impl;

import net.minecraft.commands.CommandSourceStack;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformCommandUtils;

public final class TableGetterById implements ModdedPlatformCommandUtils.TableGetterById {
    @Override
    public LootTable getTableForIdImpl(CommandSourceStack source, Identifier id) {
        return new LootTableWrapper(source.getServer().getLootData().getLootTable(((IdentifierWrapper) id).vanillaIdentifier()));
    }
}
