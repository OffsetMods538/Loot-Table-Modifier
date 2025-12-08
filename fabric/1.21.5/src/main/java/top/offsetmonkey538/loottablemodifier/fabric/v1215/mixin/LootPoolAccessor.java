package top.offsetmonkey538.loottablemodifier.fabric.v1215.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface LootPoolAccessor {

    @Mutable
    @Accessor
    void setEntries(List<LootPoolEntry> entries);

    @Mutable
    @Accessor
    void setFunctions(List<LootFunction> functions);
}
