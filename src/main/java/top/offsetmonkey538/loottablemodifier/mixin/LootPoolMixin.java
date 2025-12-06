package top.offsetmonkey538.loottablemodifier.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import top.offsetmonkey538.loottablemodifier.duck.LootElementWithConditions;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LootPool.class)
public class LootPoolMixin implements LootElementWithConditions {

    @Shadow
    @Final
    @Mutable
    public List<LootCondition> conditions;

    @Shadow
    @Final
    @Mutable
    private Predicate<LootContext> predicate;

    @Override
    @Unique
    public void loot_table_modifier$setConditions(List<LootCondition> conditions) {
        this.conditions = conditions;
        this.predicate = Util.allOf(conditions);
    }

    @Override
    public List<LootCondition> loot_table_modifier$getConditions() {
        return this.conditions;
    }
}
