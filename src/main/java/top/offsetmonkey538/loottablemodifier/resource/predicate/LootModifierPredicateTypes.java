package top.offsetmonkey538.loottablemodifier.resource.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.predicate.entry.ItemEntryPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.AllOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.AnyOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.InvertedLootPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.table.LootTablePredicate;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public final class LootModifierPredicateTypes {
    private LootModifierPredicateTypes() {

    }

    public static final LootModifierPredicateType INVERTED = register(id("inverted"), InvertedLootPredicate.CODEC);
    public static final LootModifierPredicateType ANY_OF = register(id("any_of"), AnyOfLootPredicate.CODEC);
    public static final LootModifierPredicateType ALL_OF = register(id("all_of"), AllOfLootPredicate.CODEC);

    public static final LootModifierPredicateType ITEM_ENTRY = register(id("item_entry"), ItemEntryPredicate.CODEC);

    public static final LootModifierPredicateType LOOT_TABLE = register(id("loot_table"), LootTablePredicate.CODEC);

    //public static final LootModifierPredicateType LOOT_POOL = register(id("loot_pool"), LootPoolPredicate.CODEC);

    private static LootModifierPredicateType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierPredicate> codec) {
        return Registry.register(LootModifierPredicateType.REGISTRY, id, new LootModifierPredicateType(codec));
    }

    public static void register() {
        // Registers action types by loading the class
    }
}
