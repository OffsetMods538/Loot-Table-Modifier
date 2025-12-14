package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.loot;

import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;

public record LootConditionWrapper(net.minecraft.loot.condition.LootCondition vanillaCondition) implements LootCondition {

}

