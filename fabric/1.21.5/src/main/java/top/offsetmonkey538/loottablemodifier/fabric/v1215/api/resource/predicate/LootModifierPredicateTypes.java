package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.entry.EntryItemPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.op.AllOfPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.op.AnyOfPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.op.InvertedPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.resource.predicate.table.TablePredicate;

import static top.offsetmonkey538.loottablemodifier.fabric.v1215.LootTableModifier.id;

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
    public static final LootModifierPredicateType INVERTED = register(id("inverted"), InvertedPredicate.CODEC);
    /**
     * Type of {@link AnyOfPredicate}
     */
    public static final LootModifierPredicateType ANY_OF = register(id("any_of"), AnyOfPredicate.CODEC);
    /**
     * Type of {@link AllOfPredicate}
     */
    public static final LootModifierPredicateType ALL_OF = register(id("all_of"), AllOfPredicate.CODEC);

    /**
     * Type of {@link EntryItemPredicate}
     */
    public static final LootModifierPredicateType ENTRY_ITEM = register(id("entry_item"), EntryItemPredicate.CODEC);

    /**
     * Type of {@link TablePredicate}
     */
    public static final LootModifierPredicateType TABLE = register(id("table"), TablePredicate.CODEC);

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
