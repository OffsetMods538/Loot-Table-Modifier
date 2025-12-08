package top.offsetmonkey538.loottablemodifier.fabric.v1215.mixin;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.duck.LootElementWithConditions;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LootPoolEntry.class)
public class LootPoolEntryMixin implements LootElementWithConditions {

    @Shadow
    @Final
    @Mutable
    protected List<LootCondition> conditions;

    @Shadow
    @Final
    @Mutable
    private Predicate<LootContext> conditionPredicate;

    @Override
    @Unique
    public void loot_table_modifier$setConditions(List<LootCondition> conditions) {
        this.conditions = conditions;
        this.conditionPredicate = Util.allOf(conditions);
    }

    @Override
    public List<LootCondition> loot_table_modifier$getConditions() {
        return this.conditions;
    }
}
