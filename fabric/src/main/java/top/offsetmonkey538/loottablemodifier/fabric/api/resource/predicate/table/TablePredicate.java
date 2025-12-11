package top.offsetmonkey538.loottablemodifier.fabric.api.resource.predicate.table;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.predicate.LootModifierPredicateType;

import java.util.List;
import java.util.Optional;

/**
 * Matches loot tables based on the identifier and type patterns
 *
 * @param identifiers the identifiers to match. List entries are in an {@code OR} relationship
 * @param types the types to match. List entries are in an {@code OR} relationship
 */
public record TablePredicate(@Nullable List<RegexPattern> identifiers, @Nullable List<RegexPattern> types) implements LootModifierPredicate {
    public static final MapCodec<TablePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegexPattern.CODEC.listOf().optionalFieldOf("identifiers").forGetter(TablePredicate::optionalIdentifier),
            RegexPattern.CODEC.listOf().optionalFieldOf("types").forGetter(TablePredicate::optionalType)
    ).apply(instance, TablePredicate::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Codec gib it to me
    private TablePredicate(@NotNull Optional<List<RegexPattern>> optionalIdentifier, @NotNull Optional<List<RegexPattern>> optionalType) {
        this(
                optionalIdentifier.orElse(null),
                optionalType.orElse(null)
        );
    }

    private Optional<List<RegexPattern>> optionalIdentifier() {
        if (identifiers == null || identifiers.isEmpty()) return Optional.empty();

        return Optional.of(identifiers);
    }
    private Optional<List<RegexPattern>> optionalType() {
        if (types == null || types.isEmpty()) return Optional.empty();

        return Optional.of(types);
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.TABLE;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        boolean result = true;

        if (identifiers != null && !identifiers.isEmpty()) {
            boolean idResult = false;
            final String tableIdString = context.tableId().toString();
            for (RegexPattern pattern : identifiers) idResult = idResult || pattern.matches(tableIdString);
            result = idResult;
        }

        if (types != null && !types.isEmpty()) {
            boolean typeResult = false;
            final String tableTypeString = context.table().getType();
            for (RegexPattern pattern : types) typeResult = typeResult || pattern.matches(tableTypeString);
            result = result && typeResult;
        }

        return result;
    }

    /**
     * Creates a builder for {@link TablePredicate}
     *
     * @return a new {@link TablePredicate.Builder}
     */
    @Contract("->new")
    public static TablePredicate.Builder builder() {
        return new TablePredicate.Builder();
    }

    /**
     * Builder for {@link TablePredicate}
     */
    public static class Builder implements LootModifierPredicate.Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<RegexPattern> names = ImmutableList.builder();
        private final ImmutableList.Builder<RegexPattern> types = ImmutableList.builder();

        /**
         * Adds a loot table to match
         *
         * @param name the identifier of the loot table to match
         * @return this
         */
        @Contract("_->this")
        public TablePredicate.Builder name(@NotNull String name) {
            name(RegexPattern.literal(name));
            return this;
        }
        /**
         * Adds a {@link RegexPattern} to match the loot table id with.
         *
         * @param name the {@link RegexPattern} to match
         * @return this
         */
        @Contract("_->this")
        public TablePredicate.Builder name(@NotNull RegexPattern name) {
            this.names.add(name);
            return this;
        }

        /**
         * Adds a type to match.
         *
         * @param type the identifier of the type to match
         * @return this
         */
        @Contract("_->this")
        public TablePredicate.Builder type(@NotNull String type) {
            type(RegexPattern.literal(type));
            return this;
        }
        /**
         * Adds a {@link RegexPattern} to match the loot table type with.
         *
         * @param type the {@link RegexPattern} of the type to match
         * @return this
         */
        @Contract("_->this")
        public TablePredicate.Builder type(@NotNull RegexPattern type) {
            this.types.add(type);
            return this;
        }

        @Override
        public TablePredicate build() {
            return new TablePredicate(names.build(), types.build());
        }
    }
}
