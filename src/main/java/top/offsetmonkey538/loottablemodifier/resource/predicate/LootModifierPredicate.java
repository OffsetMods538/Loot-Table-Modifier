package top.offsetmonkey538.loottablemodifier.resource.predicate;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.AnyOfLootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.AllOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.AnyOfLootPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.op.InvertedLootPredicate;

import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.resource.LootModifierContext.*;

public interface LootModifierPredicate extends Predicate<LootModifierContext> {
    Codec<LootModifierPredicate> CODEC = LootModifierPredicateType.REGISTRY.getCodec().dispatch(LootModifierPredicate::getType, LootModifierPredicateType::codec);

    LootModifierPredicateType getType();

    byte requiredContext();

    /**
     * Tests if this predicate matches the provided context
     * @param context the context to test against
     * @return true when this predicate matches the provided context, false when not
     */

    default boolean test(final @NotNull LootModifierContext context) {
        return switch (requiredContext()) {
            case REQUIRES_TABLE -> test(context, context.table(), context.tableId());
            case REQUIRES_POOL -> {
                if (context.pool() == null) throw new IllegalArgumentException("PredicateContext for LootPoolPredicate doesn't have a pool set!");
                yield test(context, context.table(), context.tableId(), context.pool());
            }
            case REQUIRES_ENTRY -> {
                if (context.pool() == null) throw new IllegalArgumentException("PredicateContext for LootPoolPredicate doesn't have a pool set!");
                if (context.entry() == null) throw new IllegalArgumentException("PredicateContext for LootPoolPredicate doesn't have an entry set!");
                yield test(context, context.table(), context.tableId(), context.pool(), context.entry());
            }
            default -> throw new UnsupportedOperationException("Loot modifier predicate defines unsupported requiredContext value '%s', only values 0 - 2 are supported.".formatted(requiredContext()));
        };
    }

    default boolean test(final @NotNull LootModifierContext context, final @NotNull LootTable table, final @NotNull Identifier tableId) {
        throw new UnsupportedOperationException("Loot modifier predicate doesn't implement test method for table, but says it requires a table");
    }
    default boolean test(final @NotNull LootModifierContext context, final @NotNull LootTable table, final @NotNull Identifier tableId, final @NotNull LootPool pool) {
        throw new UnsupportedOperationException("Loot modifier predicate doesn't implement test method for table and pool, but says it requires a pool");
    }
    default boolean test(final @NotNull LootModifierContext context, final @NotNull LootTable table, final @NotNull Identifier tableId, final @NotNull LootPool pool, final @NotNull LootPoolEntry entry) {
        throw new UnsupportedOperationException("Loot modifier predicate doesn't implement test method for table, pool and entry, but says it requires an entry");
    }

    @FunctionalInterface
    interface Builder {
        LootModifierPredicate build();

        default Builder invert() {
            return InvertedLootPredicate.builder(this);
        }

        default LootModifierPredicate.Builder or(LootModifierPredicate.Builder otherPredicate) {
            return AnyOfLootPredicate.builder(this, otherPredicate);
        }

        default LootModifierPredicate.Builder and(LootModifierPredicate.Builder otherPredicate) {
            return AllOfLootPredicate.builder(this, otherPredicate);
        }
    }
}
