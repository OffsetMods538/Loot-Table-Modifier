package top.offsetmonkey538.loottablemodifier.modded.mixin;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

@Mixin(LootPool.class)
public interface LootPoolAccessor {

    @Mutable
    @Accessor
    void setEntries(List<LootPoolEntryContainer> entries);

    @Accessor
    List<LootPoolEntryContainer> getEntries();

    @Mutable
    @Accessor
    void setFunctions(List<LootItemFunction> functions);

    @Accessor
    List<LootItemFunction> getFunctions();
}
