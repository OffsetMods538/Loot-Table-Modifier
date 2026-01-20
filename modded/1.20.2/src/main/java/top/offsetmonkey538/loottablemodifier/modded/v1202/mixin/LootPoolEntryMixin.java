package top.offsetmonkey538.loottablemodifier.modded.v1202.mixin;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import top.offsetmonkey538.loottablemodifier.common.util.PredicateUtils;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootElementWithConditions;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LootPoolEntryContainer.class)
public abstract class LootPoolEntryMixin implements LootElementWithConditions {

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
        this.compositeCondition = PredicateUtils.allOf(conditions);
    }

    @Override
    public List<LootItemCondition> loot_table_modifier$getConditions() {
        return this.conditions;
    }
}
