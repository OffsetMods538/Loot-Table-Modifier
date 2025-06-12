package top.offsetmonkey538.loottablemodifier.resource;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record LootModifierContext(@NotNull LootTable table, @NotNull Identifier tableId, @NotNull LootPool pool, @NotNull LootPoolEntry entry, boolean tableAlreadyModified, boolean poolAlreadyModified) {
    public static final int MODIFIED_NONE = 0b000;
    public static final int MODIFIED_TABLE = 0b001;
    public static final int MODIFIED_POOL = 0b011;
    public static final int MODIFIED_ENTRY = 0b111;
}