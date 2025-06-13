package top.offsetmonkey538.loottablemodifier.resource.predicate.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.entry.ItemEntry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.OptionalPattern;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;

import java.util.regex.Pattern;

public record ItemEntryPredicate(OptionalPattern name) implements LootModifierPredicate {
    public static final MapCodec<ItemEntryPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(OptionalPattern.CODEC.fieldOf("name").forGetter(ItemEntryPredicate::name)).apply(instance, ItemEntryPredicate::new)
    );

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.ITEM_ENTRY;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        if (!(context.entry() instanceof ItemEntry itemEntry)) return false;

        return name.matcher(itemEntry.item.getIdAsString()).matches();
    }

    // TODO: overrides which can take like identifiers and items and shiz
    public static LootModifierPredicate.Builder builder(OptionalPattern name) {
        return () -> new ItemEntryPredicate(name);
    }
}
