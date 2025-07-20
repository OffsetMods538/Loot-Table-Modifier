package top.offsetmonkey538.loottablemodifier.api.resource.util;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A context for predicates to match against and actions to modify.
 *
 * @param table the table
 * @param tableId the id of the table
 * @param pool the pool
 * @param entry the entry
 * @param tableAlreadyModified if the table has already been modified by the current action
 * @param poolAlreadyModified if the pool has already been modified by the current action
 */
public record LootModifierContext(@NotNull LootTable table, @NotNull Identifier tableId, @Nullable LootPool pool, @Nullable LootPoolEntry entry, boolean tableAlreadyModified, boolean poolAlreadyModified) {

}
