package top.offsetmonkey538.loottablemodifier.api.resource.action.entry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootPoolEntry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.mixin.LootPoolAccessor;

/**
 * Removes the matched entries from their pools
 */
public record RemoveEntryAction() implements LootModifierAction {
    public static final MapCodec<RemoveEntryAction> CODEC = Codec.of(Encoder.empty(), Decoder.unit(RemoveEntryAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_REMOVE;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        final LootPool pool = context.pool();
        final LootPoolEntry entry = context.entry();
        if (pool == null || entry == null) return MODIFIED_NONE;

        final ImmutableList.Builder<LootPoolEntry> newEntriesBuilder = ImmutableList.builder();

        for (final LootPoolEntry originalEntry : pool.entries) {
            if (originalEntry == entry) continue;
            newEntriesBuilder.add(originalEntry);
        }

        ((LootPoolAccessor) pool).setEntries(newEntriesBuilder.build());

        return MODIFIED_ENTRY;
    }

    /**
     * Creates a builder for {@link RemoveEntryAction}
     *
     * @return a new {@link RemoveEntryAction.Builder}
     */
    @Contract("->new")
    public static RemoveEntryAction.Builder builder() {
        return RemoveEntryAction::new;
    }
}
