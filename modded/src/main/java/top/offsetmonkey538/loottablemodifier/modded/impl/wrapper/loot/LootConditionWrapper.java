package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot;

import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootCondition;

public record LootConditionWrapper(net.minecraft.world.level.storage.loot.predicates.LootItemCondition vanillaCondition) implements LootCondition {

}

