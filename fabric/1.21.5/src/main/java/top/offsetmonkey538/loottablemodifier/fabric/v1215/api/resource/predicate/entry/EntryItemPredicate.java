package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.Item;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot.entry.ItemEntry;

/**
 * Matches an item entry based on its item
 *
 * @param name the {@link RegexPattern} matching the item identifier
 */
public record EntryItemPredicate(RegexPattern name) implements LootModifierPredicate {
    public static final MapCodec<EntryItemPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(RegexPattern.CODEC.fieldOf("name").forGetter(EntryItemPredicate::name)).apply(instance, EntryItemPredicate::new)
    );

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ENTRY_ITEM;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        // No need for a separate null check of entry as null isn't an instance of ItemEntry
        if (!(context.entry() instanceof ItemEntry itemEntry)) return false;

        return name.matches(itemEntry.getId());
    }

    /**
     * Creates a builder for {@link EntryItemPredicate} matching the provided item
     *
     * @param name the item to match
     * @return a new {@link EntryItemPredicate.Builder}
     */
    @Contract("_->new")
    public static EntryItemPredicate.Builder builder(Item name) {
        return builder(name.getId());
    }
    /**
     * Creates a builder for {@link EntryItemPredicate} matching the item based on the provided identifier
     *
     * @param name the item id to match
     * @return a new {@link EntryItemPredicate.Builder}
     */
    @Contract("_->new")
    public static EntryItemPredicate.Builder builder(String name) {
        return builder(RegexPattern.literal(name));
    }
    /**
     * Creates a builder for {@link EntryItemPredicate} matching the provided item
     *
     * @param name the {@link RegexPattern} to match the item id with
     * @return a new {@link EntryItemPredicate.Builder}
     */
    @Contract("_->new")
    public static EntryItemPredicate.Builder builder(RegexPattern name) {
        return () -> new EntryItemPredicate(name);
    }
}
