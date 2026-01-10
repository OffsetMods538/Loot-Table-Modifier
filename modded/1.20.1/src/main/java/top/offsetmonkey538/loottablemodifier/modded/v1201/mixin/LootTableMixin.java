package top.offsetmonkey538.loottablemodifier.modded.v1201.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import top.offsetmonkey538.loottablemodifier.modded.duck.LootTableDuck;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements LootTableDuck {

    @Shadow
    @Mutable
    @Final
    LootPool[] pools;

    @Shadow
    @Mutable
    @Final
    LootItemFunction[] functions;

    @Shadow
    @Mutable
    @Final
    private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    @Override
    public void loot_table_modifier$setPools(List<LootPool> pools) {
        this.pools = pools.toArray(LootPool[]::new);
    }

    @Override
    public List<LootPool> loot_table_modifier$getPools() {
        return Arrays.asList(this.pools);
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
