package top.offsetmonkey538.loottablemodifier.api.resource.predicate.table;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootTableIdGetter;
import top.offsetmonkey538.loottablemodifier.api.resource.util.OptionalIdentifierPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

import java.util.List;
import java.util.Optional;

/**
 * Matches loot tables based on the identifier and type patterns
 *
 * @param identifiers the identifiers to match. List entries are in an {@code OR} relationship
 * @param types the types to match. List entries are in an {@code OR} relationship
 */
public record LootTablePredicate(@Nullable List<OptionalIdentifierPattern> identifiers, @Nullable List<OptionalIdentifierPattern> types) implements LootModifierPredicate {
    public static final MapCodec<LootTablePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            OptionalIdentifierPattern.CODEC.listOf().optionalFieldOf("identifiers").forGetter(LootTablePredicate::optionalIdentifier),
            OptionalIdentifierPattern.CODEC.listOf().optionalFieldOf("types").forGetter(LootTablePredicate::optionalType)
    ).apply(instance, LootTablePredicate::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Codec gib it to me
    private LootTablePredicate(@NotNull Optional<List<OptionalIdentifierPattern>> optionalIdentifier, @NotNull Optional<List<OptionalIdentifierPattern>> optionalType) {
        this(
                optionalIdentifier.orElse(null),
                optionalType.orElse(null)
        );
    }

    private Optional<List<OptionalIdentifierPattern>> optionalIdentifier() {
        if (identifiers == null || identifiers.isEmpty()) return Optional.empty();

        return Optional.of(identifiers);
    }
    private Optional<List<OptionalIdentifierPattern>> optionalType() {
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
            for (OptionalIdentifierPattern pattern : identifiers) idResult = idResult || pattern.matches(tableIdString);
            result = idResult;
        }

        if (types != null && !types.isEmpty()) {
            boolean typeResult = false;
            final String tableTypeString = LootContextTypes.MAP.inverse().get(context.table().getType()).toString();
            for (OptionalIdentifierPattern pattern : types) typeResult = typeResult || pattern.matches(tableTypeString);
            result = result && typeResult;
        }

        return result;
    }

    /**
     * Creates a builder for {@link LootTablePredicate}
     *
     * @return a new {@link LootTablePredicate.Builder}
     */
    @Contract("->new")
    public static LootTablePredicate.Builder builder() {
        return new LootTablePredicate.Builder();
    }

    /**
     * Builder for {@link LootTablePredicate}
     */
    public static class Builder implements LootModifierPredicate.Builder {
        private Builder() {

        }

        private final ImmutableList.Builder<OptionalIdentifierPattern> names = ImmutableList.builder();
        private final ImmutableList.Builder<OptionalIdentifierPattern> types = ImmutableList.builder();

        /**
         * Adds an entity type to match
         *
         * @param name the {@link EntityType} to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder name(@NotNull EntityType<?> name) {
            name(LootTableIdGetter.INSTANCE.get(name));
            return this;
        }
        /**
         * Adds a block to match
         *
         * @param name the {@link Block} to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder name(@NotNull Block name) {
            name(LootTableIdGetter.INSTANCE.get(name));
            return this;
        }
        /**
         * Adds a loot table to match
         *
         * @param name the {@link RegistryKey} of the loot table to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder name(@NotNull RegistryKey<LootTable> name) {
            name(name.getValue());
            return this;
        }
        /**
         * Adds a loot table to match
         *
         * @param name the {@link Identifier} of the loot table to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder name(@NotNull Identifier name) {
            name(OptionalIdentifierPattern.literal(name));
            return this;
        }
        /**
         * Adds a {@link OptionalIdentifierPattern} to match the loot table id with.
         *
         * @param name the {@link OptionalIdentifierPattern} to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder name(@NotNull OptionalIdentifierPattern name) {
            this.names.add(name);
            return this;
        }

        /**
         * Adds a {@link ContextType type} to match.
         *
         * @param type the {@link ContextType type} to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder type(@NotNull ContextType type) {
            type(LootContextTypes.MAP.inverse().get(type));
            return this;
        }
        /**
         * Adds a type to match.
         *
         * @param type the {@link Identifier} of the type to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder type(@NotNull Identifier type) {
            this.types.add(OptionalIdentifierPattern.literal(type));
            return this;
        }
        /**
         * Adds a {@link OptionalIdentifierPattern} to match the loot table type with.
         *
         * @param type the {@link OptionalIdentifierPattern} of the type to match
         * @return this
         */
        @Contract("_->this")
        public LootTablePredicate.Builder type(@NotNull OptionalIdentifierPattern type) {
            this.types.add(type);
            return this;
        }

        @Override
        public LootTablePredicate build() {
            return new LootTablePredicate(names.build(), types.build());
        }
    }
}
