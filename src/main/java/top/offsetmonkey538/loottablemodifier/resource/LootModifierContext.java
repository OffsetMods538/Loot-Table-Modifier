package top.offsetmonkey538.loottablemodifier.resource;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record LootModifierContext(@NotNull LootTable table, @NotNull Identifier tableId, @NotNull LootPool pool, @NotNull LootPoolEntry entry) {

}