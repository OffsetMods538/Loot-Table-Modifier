package top.offsetmonkey538.loottablemodifier.resource.predicate.entry;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.LootTableModifier;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.resource.predicate.condition.LootConditionPredicate;

import java.util.List;
import java.util.Optional;

public abstract class LootEntryPredicate implements LootModifierPredicate {
    protected final @Nullable List<LootConditionPredicate> conditions;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // da codecccccc
    protected LootEntryPredicate(final @NotNull Optional<List<LootConditionPredicate>> conditions) {
        this.conditions = conditions.orElse(null);
    }

    protected static <T extends LootEntryPredicate> Products.P1<RecordCodecBuilder.Mu<T>, Optional<List<LootConditionPredicate>>> addConditionsToCodec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(LootConditionPredicate.CODEC.listOf().optionalFieldOf("conditions").forGetter(entry -> Optional.ofNullable(entry.conditions)));
    }


    public abstract LootModifierPredicateType getType();

    /**
     * Checks if this predicate matches the provided loot pool entry
     * @param entry the entry to check against
     * @return true when the provided loot pool entry matches, false otherwise
     */
    @Override
    public boolean test(final @NotNull LootModifierContext context, final @NotNull LootTable table, final @NotNull Identifier tableId, final @NotNull LootPool pool, final @NotNull LootPoolEntry entry) {
        final int conditionCount = conditions == null ? 0 : conditions.size();
        final int entryConditionsCount = entry.conditions == null ? 0 : entry.conditions.size();

        if (conditionCount == entryConditionsCount) return true;
        if (conditionCount == 0) return false;

        for (LootConditionPredicate predicate : this.conditions) {

        }

        return conditions.stream().allMatch(predicate -> entry.conditions.stream().anyMatch(predicate::matches));
    }
}
