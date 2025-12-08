package top.offsetmonkey538.loottablemodifier.fabric.v1215.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public interface LootTableAccessor {

    @Accessor
    List<LootPool> getPools();

    @Mutable
    @Accessor
    void setPools(List<LootPool> pools);

    @Accessor
    List<LootFunction> getFunctions();

    @Mutable
    @Accessor
    void setFunctions(List<LootFunction> pools);
}
