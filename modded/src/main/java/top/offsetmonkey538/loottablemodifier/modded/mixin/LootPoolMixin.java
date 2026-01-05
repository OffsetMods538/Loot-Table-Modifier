package top.offsetmonkey538.loottablemodifier.modded.mixin;

import org.spongepowered.asm.mixin.*;
import top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootElementWithConditions;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@Mixin(LootPool.class)
public class LootPoolMixin implements LootElementWithConditions {

    @Shadow
    @Final
    @Mutable
    public List<LootItemCondition> conditions;

    @Shadow
    @Final
    @Mutable
    private Predicate<LootContext> compositeCondition;

    @Override
    @Unique
    public void loot_table_modifier$setConditions(List<LootItemCondition> conditions) {
        this.conditions = conditions;
        this.compositeCondition = PlatformMain.allOf(conditions);
    }

    @Override
    public List<LootItemCondition> loot_table_modifier$getConditions() {
        return this.conditions;
    }
}
