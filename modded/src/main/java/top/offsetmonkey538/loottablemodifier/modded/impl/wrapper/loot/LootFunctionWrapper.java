package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot;

import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootFunction;

public record LootFunctionWrapper(net.minecraft.world.level.storage.loot.functions.LootItemFunction vanillaFunction) implements LootFunction {

}
