package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot;

import top.offsetmonkey538.loottablemodifier.fabric.api.wrapper.loot.LootFunction;

public record LootFunctionWrapper(net.minecraft.loot.function.LootFunction vanillaFunction) implements LootFunction {

}

