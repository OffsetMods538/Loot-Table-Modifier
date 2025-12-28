package top.offsetmonkey538.loottablemodifier.fabric.mixin;

import org.spongepowered.asm.mixin.*;
import top.offsetmonkey538.loottablemodifier.fabric.duck.LootElementWithConditions;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@Mixin(LootPoolEntryContainer.class)
public class LootPoolEntryMixin implements LootElementWithConditions {

    @Shadow
    @Final
    @Mutable
    protected List<LootItemCondition> conditions;

    @Shadow
    @Final
    @Mutable
    private Predicate<LootContext> compositeCondition;

    @Override
    @Unique
    public void loot_table_modifier$setConditions(List<LootItemCondition> conditions) {
        this.conditions = conditions;
        this.compositeCondition = Util.allOf(conditions);
    }

    @Override
    public List<LootItemCondition> loot_table_modifier$getConditions() {
        return this.conditions;
    }
}
