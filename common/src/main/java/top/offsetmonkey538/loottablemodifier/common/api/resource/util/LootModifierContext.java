package top.offsetmonkey538.loottablemodifier.common.api.resource.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootTable;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;

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
