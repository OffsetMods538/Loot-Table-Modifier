package top.offsetmonkey538.loottablemodifier.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.data.DataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.loottablemodifier.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.MOD_ID;

/**
 * FIXME: wrong
 * A datagen provider for creating loot modifiers.
 * <br>
 * Override {@link #generate(RegistryWrapper.WrapperLookup) generate()} and use the {@code addModifier(...)} methods to add modifiers.
 * <br>
 * <pre>{@code
 * @Override
 * protected void generate(RegistryWrapper.WrapperLookup lookup) {
 *     addModifier(
 *             Identifier.of("testmod", "drop_tnt"),
 *             LootPool.builder()
 *                     .rolls(ConstantLootNumberProvider.create(1))
 *                     .with(
 *                             ItemEntry.builder(Items.TNT)
 *                                     .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
 *                     ),
 *             EntityType.CREEPER,
 *             EntityType.ZOMBIE
 *     );
 * }
 * }</pre>
 */
public abstract class NewLootModifierProvider extends FabricCodecDataProvider<LootModifier> {
    private BiConsumer<Identifier, LootModifier> provider;

    public NewLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, MOD_ID + "/loot_modifier", LootModifier.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, LootModifier> provider, RegistryWrapper.WrapperLookup lookup) {
        this.provider = provider;
        generate(lookup);
    }

    @Override
    public String getName() {
        return "New Loot Table Modifiers";
    }

    /**
     * Override and use {@code addModifier()} methods to add modifiers.
     *
     * @param lookup A lookup for registries.
     */
    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    /**
     * Adds a new loot table modifier for the given {@link EntityType}s.
     *
     * @param name Name of this modifier
     * @param builder The loot pool to add
     * @param modifies The {@link EntityType} to add the modifier to
     * @param modifiesAdditional Additional {@link EntityType}s to add the modifier to
     */
    protected void addModifier(Identifier name, LootModifierAction.Builder builder, EntityType<?> modifies, EntityType<?>... modifiesAdditional) {
        addModifier(
                name,
                List.of(builder),
                modifies,
                modifiesAdditional
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link EntityType}s.
     *
     * @param name Name of this modifier
     * @param builders The loot pools to add
     * @param modifies The {@link EntityType} to add the modifier to
     * @param modifiesAdditional Additional {@link EntityType}s to add the modifier to
     */
    protected void addModifier(Identifier name, List<LootModifierAction.Builder> builders, EntityType<?> modifies, EntityType<?>... modifiesAdditional) {
        addModifier(
                name,
                builders,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).map(EntityLootTableIdGetter.get).toList()
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link RegistryKey}s.
     *
     * @param name Name of this modifier
     * @param builder The loot pool to add
     * @param modifies The {@link RegistryKey} to add the modifier to
     * @param modifiesAdditional Additional {@link RegistryKey}s to add the modifier to
     */
    protected void addModifier(Identifier name, LootModifierAction.Builder builder, RegistryKey<?> modifies, RegistryKey<?>... modifiesAdditional) {
        addModifier(
                name,
                List.of(builder),
                modifies,
                modifiesAdditional
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link RegistryKey}s.
     *
     * @param name Name of this modifier
     * @param builders The loot pools to add
     * @param modifies The {@link RegistryKey} to add the modifier to
     * @param modifiesAdditional Additional {@link RegistryKey}s to add the modifier to
     */
    protected void addModifier(Identifier name, List<LootModifierAction.Builder> builders, RegistryKey<?> modifies, RegistryKey<?>... modifiesAdditional) {
        addModifier(
                name,
                builders,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).map(RegistryKey::getValue).toList()
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link Identifier}s.
     *
     * @param name Name of this modifier
     * @param builder The loot pool to add
     * @param modifies The {@link Identifier} to add the modifier to
     * @param modifiesAdditional Additional {@link Identifier}s to add the modifier to
     */
    protected void addModifier(Identifier name, LootModifierAction.Builder builder, Identifier modifies, Identifier... modifiesAdditional) {
        addModifier(
                name,
                List.of(builder),
                modifies,
                modifiesAdditional
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link Identifier}s.
     *
     * @param name Name of this modifier
     * @param builders The loot pools to add
     * @param modifies The {@link Identifier} to add the modifier to
     * @param modifiesAdditional Additional {@link Identifier}s to add the modifier to
     */
    protected void addModifier(Identifier name, List<LootModifierAction.Builder> builders, Identifier modifies, Identifier... modifiesAdditional) {
        addModifier(
                name,
                builders,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).toList()
        );
    }

    private void addModifier(Identifier name, List<LootModifierAction.Builder> builders, List<Identifier> modifies) {
        provider.accept(name, new LootModifier(
                new ArrayList<>(modifies),
                builders.stream()
                        .map(LootModifierAction.Builder::build)
                        .toList()
        ));
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