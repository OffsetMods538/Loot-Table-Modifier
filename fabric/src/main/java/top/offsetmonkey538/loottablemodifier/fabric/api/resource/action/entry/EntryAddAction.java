package top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.entry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.LootModifierActionTypes;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.LootPool;
import top.offsetmonkey538.loottablemodifier.api.wrapper.loot.entry.LootPoolEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds the provided entries to matched pools
 *
 * @param entries the entries to add
 */
public record EntryAddAction(List<LootPoolEntry> entries) implements LootModifierAction {
    public static final MapCodec<EntryAddAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootPoolEntry.CODEC_PROVIDER.get().listOf().fieldOf("entries").forGetter(EntryAddAction::entries)
    ).apply(instance, EntryAddAction::new));

    @Override
    public LootModifierActionType getType() {
        return LootModifierActionTypes.ENTRY_ADD;
    }

    @Override
    public int apply(@NotNull LootModifierContext context) {
        if (context.poolAlreadyModified()) return MODIFIED_NONE;

        final LootPool pool = context.pool();
        if (pool == null) return MODIFIED_NONE;

        final ArrayList<LootPoolEntry> poolEntries = pool.getEntries();
        poolEntries.addAll(this.entries);
        pool.setEntries(poolEntries);

        return MODIFIED_ENTRY;
    }

    /**
     * Creates a builder for {@link EntryAddAction}
     *
     * @return a new {@link EntryAddAction.Builder}
     */
    @Contract("->new")
    public static EntryAddAction.Builder builder() {
        return new EntryAddAction.Builder();
    }

    /**
     * Builder for {@link EntryAddAction}
     */
    public static class Builder implements LootModifierAction.Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<LootPoolEntry> entries = ImmutableList.builder();

        /**
         * Adds an entry
         *
         * @param entry The entry to add
         * @return this
         */
        @Contract("_->this")
        public EntryAddAction.Builder entry(LootPoolEntry entry) {
            this.entries.add(entry);
            return this;
        }

        @Override
        public EntryAddAction build() {
            return new EntryAddAction(entries.build());
        }
    }
}
