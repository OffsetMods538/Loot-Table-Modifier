package top.offsetmonkey538.loottablemodifier.common.api.resource.predicate;

import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.entry.EntryItemPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.AllOfPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.AnyOfPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.op.InvertedPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.table.TablePredicate;

import static top.offsetmonkey538.loottablemodifier.common.platform.PlatformMain.id;

/**
 * Contains all {@link LootModifierPredicate} types available in Loot Table Modifier
 * <br />
 * Use their builders to create them.
 */
public final class LootModifierPredicateTypes {
    private LootModifierPredicateTypes() {

    }

    /**
     * Type of {@link InvertedPredicate}
     */
    public static final LootModifierPredicateType INVERTED = LootModifierPredicateType.register(id("inverted"), InvertedPredicate.CODEC);
    /**
     * Type of {@link AnyOfPredicate}
     */
    public static final LootModifierPredicateType ANY_OF = LootModifierPredicateType.register(id("any_of"), AnyOfPredicate.CODEC);
    /**
     * Type of {@link AllOfPredicate}
     */
    public static final LootModifierPredicateType ALL_OF = LootModifierPredicateType.register(id("all_of"), AllOfPredicate.CODEC);

    /**
     * Type of {@link EntryItemPredicate}
     */
    public static final LootModifierPredicateType ENTRY_ITEM = LootModifierPredicateType.register(id("entry_item"), EntryItemPredicate.CODEC);

    /**
     * Type of {@link TablePredicate}
     */
    public static final LootModifierPredicateType TABLE = LootModifierPredicateType.register(id("table"), TablePredicate.CODEC);

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
