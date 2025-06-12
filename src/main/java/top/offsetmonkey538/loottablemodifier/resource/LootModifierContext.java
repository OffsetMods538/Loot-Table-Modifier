package top.offsetmonkey538.loottablemodifier.resource;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record LootModifierContext(@NotNull LootTable table, @NotNull Identifier tableId, @Nullable LootPool pool, @Nullable LootPoolEntry entry) {
    public static final byte REQUIRES_TABLE = 2;
    public static final byte REQUIRES_POOL  = 1;
    public static final byte REQUIRES_ENTRY = 0;

    public LootModifierContext(@NotNull final LootTable table, @NotNull final Identifier tableId, @NotNull final LootPool pool) {
        this(table, tableId, pool, null);
    }
    public LootModifierContext(@NotNull final LootTable table, @NotNull final Identifier tableId) {
        this(table, tableId, null, null);
    }
}