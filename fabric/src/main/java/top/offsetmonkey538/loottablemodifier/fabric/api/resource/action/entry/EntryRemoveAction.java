package top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.fabric.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.fabric.api.wrapper.loot.entry.LootPoolEntry;

import java.util.ArrayList;

/**
 * Removes the matched entries from their pools
 */
public record EntryRemoveAction() implements LootModifierAction {
    public static final MapCodec<EntryRemoveAction> CODEC = Codec.of(Encoder.empty(), Decoder.unit(EntryRemoveAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_REMOVE;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPool pool = context.pool();
        final LootPoolEntry entry = context.entry();
        if (pool == null || entry == null) return MODIFIED_NONE;

        final ArrayList<LootPoolEntry> poolEntries = pool.getEntries();
        poolEntries.remove(entry);
        pool.setEntries(poolEntries);

        return MODIFIED_ENTRY;
    }

    /**
     * Creates a builder for {@link EntryRemoveAction}
     *
     * @return a new {@link EntryRemoveAction.Builder}
     */
    @Contract("->new")
    public static EntryRemoveAction.Builder builder() {
        return EntryRemoveAction::new;
    }
}
