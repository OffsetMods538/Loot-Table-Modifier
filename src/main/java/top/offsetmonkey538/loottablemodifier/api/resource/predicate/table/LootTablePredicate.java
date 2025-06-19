package top.offsetmonkey538.loottablemodifier.api.resource.predicate.table;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootTableIdGetter;
import top.offsetmonkey538.loottablemodifier.api.resource.util.OptionalPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.LootModifierPredicateType;

import java.util.List;
import java.util.Optional;


public record LootTablePredicate(@Nullable List<OptionalPattern> identifiers, @Nullable List<OptionalPattern> types) implements LootModifierPredicate {
    public static final MapCodec<LootTablePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(OptionalPattern.CODEC, OptionalPattern.CODEC.listOf()).optionalFieldOf("identifiers").forGetter(LootTablePredicate::optionalEitherIdentifier),
            Codec.either(OptionalPattern.CODEC, OptionalPattern.CODEC.listOf()).optionalFieldOf("types").forGetter(LootTablePredicate::optionalEitherType)
    ).apply(instance, LootTablePredicate::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Codec gib it to me
    private LootTablePredicate(@NotNull Optional<Either<OptionalPattern, List<OptionalPattern>>> optionalIdentifier, @NotNull Optional<Either<OptionalPattern, List<OptionalPattern>>> optionalType) {
        this(
                optionalIdentifier.map(it -> it.map(List::of, it2 -> it2)).orElse(null),
                optionalType.map(it -> it.map(List::of, it2 -> it2)).orElse(null)
        );
    }

    private Optional<Either<OptionalPattern, List<OptionalPattern>>> optionalEitherIdentifier() {
        if (identifiers == null || identifiers.isEmpty()) return Optional.empty();

        if (identifiers.size() == 1) return Optional.of(Either.left(identifiers.get(0)));
        return Optional.of(Either.right(identifiers));
    }
    private Optional<Either<OptionalPattern, List<OptionalPattern>>> optionalEitherType() {
        if (types == null || types.isEmpty()) return Optional.empty();

        if (types.size() == 1) return Optional.of(Either.left(types.get(0)));
        return Optional.of(Either.right(types));
    }

    @Override
    public LootModifierPredicateType getType() {
        return LootModifierPredicateTypes.TABLE;
    }

    @Override
    public boolean test(@NotNull LootModifierContext context) {
        boolean result = true;

        if (identifiers != null) {
            boolean idResult = false;
            final String tableIdString = context.tableId().toString();
            for (OptionalPattern pattern : identifiers) idResult = idResult || pattern.matches(tableIdString);
            result = idResult;
        }

        if (types != null) {
            boolean typeResult = false;
            final String tableTypeString = LootContextTypes.MAP.inverse().get(context.table().getType()).toString();
            for (OptionalPattern pattern : types) typeResult = typeResult || pattern.matches(tableTypeString);
            result = result && typeResult;
        }

        return result;
    }

    public static LootTablePredicate.Builder builder() {
        return new LootTablePredicate.Builder();
    }

    public static class Builder implements LootModifierPredicate.Builder {
        private final ImmutableList.Builder<OptionalPattern> names = ImmutableList.builder();
        private final ImmutableList.Builder<OptionalPattern> types = ImmutableList.builder();


        public LootTablePredicate.Builder name(@NotNull EntityType<?> name) {
            name(LootTableIdGetter.INSTANCE.get(name));
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull Block name) {
            name(LootTableIdGetter.INSTANCE.get(name));
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull RegistryKey<LootTable> name) {
            name(name.getValue());
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull Identifier name) {
            name(name.toString());
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull String name) {
            name(OptionalPattern.literal(name));
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull OptionalPattern name) {
            this.names.add(name);
            return this;
        }

        public LootTablePredicate.Builder type(@NotNull ContextType type) {
            type(LootContextTypes.MAP.inverse().get(type));
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull Identifier type) {
            type(type.toString());
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull String type) {
            this.types.add(OptionalPattern.literal(type));
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull OptionalPattern type) {
            this.types.add(type);
            return this;
        }

        public LootTablePredicate build() {
            return new LootTablePredicate(names.build(), types.build());
        }
    }
}
