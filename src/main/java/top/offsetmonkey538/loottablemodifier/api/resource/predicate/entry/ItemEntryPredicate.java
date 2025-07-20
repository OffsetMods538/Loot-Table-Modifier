package top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

/**
 * Matches an item entry based on its item
 *
 * @param name the {@link RegexPattern} matching the item identifier
 */
public record ItemEntryPredicate(RegexPattern name) implements LootModifierPredicate {
    public static final MapCodec<ItemEntryPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(RegexPattern.CODEC.fieldOf("name").forGetter(ItemEntryPredicate::name)).apply(instance, ItemEntryPredicate::new)
    );

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ENTRY_ITEM;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        // No need for a separate null check of entry as null isn't an instance of ItemEntry
        if (!(context.entry() instanceof ItemEntry itemEntry)) return false;

        return name.matches(itemEntry.item.getIdAsString());
    }

    /**
     * Creates a builder for {@link ItemEntryPredicate} matching the provided item
     *
     * @param name the item to match
     * @return a new {@link ItemEntryPredicate.Builder}
     */
    @Contract("_->new")
    public static ItemEntryPredicate.Builder builder(ItemConvertible name) {
        return builder(Registries.ITEM.getId(name.asItem()));
    }
    /**
     * Creates a builder for {@link ItemEntryPredicate} matching the item based on the provided identifier
     *
     * @param name the item id to match
     * @return a new {@link ItemEntryPredicate.Builder}
     */
    @Contract("_->new")
    public static ItemEntryPredicate.Builder builder(Identifier name) {
        return builder(RegexPattern.literal(name));
    }
    /**
     * Creates a builder for {@link ItemEntryPredicate} matching the provided item
     *
     * @param name the {@link RegexPattern} to match the item id with
     * @return a new {@link ItemEntryPredicate.Builder}
     */
    @Contract("_->new")
    public static ItemEntryPredicate.Builder builder(RegexPattern name) {
        return () -> new ItemEntryPredicate(name);
    }
}
