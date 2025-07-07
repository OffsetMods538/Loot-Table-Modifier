package top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.util.OptionalIdentifierPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

public record ItemEntryPredicate(OptionalIdentifierPattern name) implements LootModifierPredicate {
    public static final MapCodec<ItemEntryPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(OptionalIdentifierPattern.CODEC.fieldOf("name").forGetter(ItemEntryPredicate::name)).apply(instance, ItemEntryPredicate::new)
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

    public static LootModifierPredicate.Builder builder(ItemConvertible name) {
        return builder(Registries.ITEM.getId(name.asItem()));
    }
    public static LootModifierPredicate.Builder builder(Identifier name) {
        return () -> new ItemEntryPredicate(OptionalIdentifierPattern.literal(name));
    }
    public static LootModifierPredicate.Builder builder(OptionalIdentifierPattern name) {
        return () -> new ItemEntryPredicate(name);
    }
}
