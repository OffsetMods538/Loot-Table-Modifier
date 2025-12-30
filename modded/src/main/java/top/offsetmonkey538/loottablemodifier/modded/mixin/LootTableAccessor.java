package top.offsetmonkey538.loottablemodifier.modded.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

@Mixin(LootTable.class)
public interface LootTableAccessor {

    @Accessor
    List<LootPool> getPools();

    @Mutable
    @Accessor
    void setPools(List<LootPool> pools);

    @Accessor
    List<LootItemFunction> getFunctions();

    @Mutable
    @Accessor
    void setFunctions(List<LootItemFunction> pools);
}
