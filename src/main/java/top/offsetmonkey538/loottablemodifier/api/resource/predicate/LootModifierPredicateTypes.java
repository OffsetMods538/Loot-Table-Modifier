package top.offsetmonkey538.loottablemodifier.api.resource.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry.ItemEntryPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AllOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.AnyOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.op.InvertedLootPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.LootTablePredicate;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

/**
 * Contains all {@link LootModifierPredicate} types available in Loot Table Modifier
 * <br />
 * Use their builders to create them.
 */
public final class LootModifierPredicateTypes {
    private LootModifierPredicateTypes() {

    }

    /**
     * Type of {@link InvertedLootPredicate}
     */
    public static final LootModifierPredicateType INVERTED = register(id("inverted"), InvertedLootPredicate.CODEC);
    /**
     * Type of {@link AnyOfLootPredicate}
     */
    public static final LootModifierPredicateType ANY_OF = register(id("any_of"), AnyOfLootPredicate.CODEC);
    /**
     * Type of {@link AllOfLootPredicate}
     */
    public static final LootModifierPredicateType ALL_OF = register(id("all_of"), AllOfLootPredicate.CODEC);

    /**
     * Type of {@link ItemEntryPredicate}
     */
    public static final LootModifierPredicateType ENTRY_ITEM = register(id("entry_item"), ItemEntryPredicate.CODEC);

    /**
     * Type of {@link LootTablePredicate}
     */
    public static final LootModifierPredicateType TABLE = register(id("table"), LootTablePredicate.CODEC);

    //public static final LootModifierPredicateType LOOT_POOL = register(id("loot_pool"), LootPoolPredicate.CODEC);

    private static LootModifierPredicateType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierPredicate> codec) {
        return Registry.register(LootModifierPredicateType.REGISTRY, id, new LootModifierPredicateType(codec));
    }

    /**
     * Registers predicate types by loading the class.
     * <br />
     * Only for the loot table modifier initializer to call, NO TOUCHY >:(
     */
    @ApiStatus.Internal
    public static void register() {
        // Registers predicate types by loading the class
    }
}
