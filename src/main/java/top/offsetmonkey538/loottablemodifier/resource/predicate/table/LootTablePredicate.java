package top.offsetmonkey538.loottablemodifier.resource.predicate.table;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.api.LootModifierPredicateTypes;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
import top.offsetmonkey538.loottablemodifier.resource.OptionalPattern;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.LOGGER;


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
        return LootModifierPredicateTypes.LOOT_TABLE;
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


        public LootTablePredicate.Builder name(@NotNull EntityType<?>... names) {
            for (EntityType<?> name : names) name(EntityLootTableIdGetter.get.apply(name));
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull RegistryKey<?>... names) {
            for (RegistryKey<?> name : names) name(name.getValue());
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull Identifier... names) {
            for (Identifier name : names) name(name.toString());
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull String... names) {
            for (String name : names) this.names.add(OptionalPattern.literal(name));
            return this;
        }
        public LootTablePredicate.Builder name(@NotNull OptionalPattern... names) {
            this.names.add(names);
            return this;
        }

        public LootTablePredicate.Builder type(@NotNull ContextType... types) {
            final BiMap<ContextType, Identifier> inverse = LootContextTypes.MAP.inverse();
            for (ContextType type : types) type(inverse.get(type));
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull Identifier... types) {
            for (Identifier type : types) type(type.toString());
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull String... types) {
            for (String type : types) this.types.add(OptionalPattern.literal(type));
            return this;
        }
        public LootTablePredicate.Builder type(@NotNull OptionalPattern... types) {
            this.types.add(types);
            return this;
        }

        public LootTablePredicate build() {
            return new LootTablePredicate(names.build(), types.build());
        }

        private static class EntityLootTableIdGetter {
            // Resolver returns the provided name (like 'method_16351') when it fails to map it
            private static final MappingResolver RESOLVER = FabricLoader.getInstance().getMappingResolver();

            private static final String V1d21d2 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Ljava/util/Optional;");
            private static final String V1d20d5 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Lnet/minecraft/class_5321;");

            // mod only supports down to 1.20.5 soo: private static final String V1d14d0 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Lnet/minecraft/util/Identifier;");

            public static final Function<EntityType<?>, Identifier> get;

            // Should be executed when class is first loaded/accessed
            static {
                try {
                    //final Class<?> entityType = EntityType.class;
                    final Class<?> entityType = Class.forName(RESOLVER.mapClassName("intermediary", "net.minecraft.class_1299"));
                    final Method method;

                    // 1.21.2 to future:tm:
                    if (isMethod(entityType, V1d21d2)) {
                        method = entityType.getDeclaredMethod(V1d21d2);
                        method.setAccessible(true);
                        get = entity -> {
                            try {
                                @SuppressWarnings("unchecked")
                                final Optional<RegistryKey<LootTable>> optional = (Optional<RegistryKey<LootTable>>) method.invoke(entity);
                                if (optional.isPresent()) return optional.get().getValue();
                                throw new IllegalStateException("Entity '" + entity + "' has no loot table! (It is created with 'builder.dropsNothing()')");
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    }

                    // 1.20.5 to 1.21.1
                    else if (isMethod(entityType, V1d20d5)) {
                        method = entityType.getDeclaredMethod(V1d20d5);
                        method.setAccessible(true);
                        get = entity -> {
                            try {
                                return ((RegistryKey<?>) method.invoke(entity)).getValue();
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        };
                    }

                    else {
                        throw new IllegalStateException("No valid way to get entity loot table id found!");
                    }
                } catch (NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            private static boolean isMethod(Class<?> clazz, String method) {
                try {
                    clazz.getDeclaredMethod(method);
                    return true;
                } catch (NoSuchMethodException e) {
                    LOGGER.warn("", e);
                    return false;
                }
            }
        }
    }
}
