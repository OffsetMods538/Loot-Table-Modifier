package top.offsetmonkey538.loottablemodifier.modded.v1201.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.*;
import top.offsetmonkey538.loottablemodifier.common.util.PredicateUtils;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootPoolDuck;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mixin(LootPool.class)
public class LootPoolMixin implements LootPoolDuck {

    @Shadow
    @Final
    @Mutable
    LootItemCondition[] conditions;

    @Shadow
    @Final
    @Mutable
    private Predicate<LootContext> compositeCondition;

    @Shadow
    @Mutable
    @Final
    LootPoolEntryContainer[] entries;

    @Shadow
    @Mutable
    @Final
    LootItemFunction[] functions;

    @Shadow
    @Mutable
    @Final
    private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    @Override
    @Unique
    public void loot_table_modifier$setConditions(List<LootItemCondition> conditions) {
        this.conditions = conditions.toArray(LootItemCondition[]::new);
        this.compositeCondition = PredicateUtils.allOf(conditions);
    }

    @Override
    public List<LootItemCondition> loot_table_modifier$getConditions() {
        return Arrays.asList(this.conditions);
    }

    @Override
    public void loot_table_modifier$setEntries(List<LootPoolEntryContainer> entries) {
        this.entries = entries.toArray(LootPoolEntryContainer[]::new);
    }

    @Override
    public List<LootPoolEntryContainer> loot_table_modifier$getEntries() {
        return Arrays.asList(this.entries);
    }

    @Override
    public void loot_table_modifier$setFunctions(List<LootItemFunction> functions) {
        this.functions = functions.toArray(LootItemFunction[]::new);
        this.compositeFunction = LootItemFunctions.compose(this.functions);
    }

    @Override
    public List<LootItemFunction> loot_table_modifier$getFunctions() {
        return Arrays.asList(this.functions);
    }
}
