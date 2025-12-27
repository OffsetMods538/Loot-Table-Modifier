package top.offsetmonkey538.loottablemodifier.fabric.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.loottablemodifier.fabric.api.LootTableIdGetter;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.IdentifierWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.LOGGER;

// TODO: Seems like this should be something to split up and move to different modules?
//  That *would* mean multiple jars and this is cool, but the maintainability of this probably won't be that great as
//  it will be able to build even if stuff is fucked and also intermediary doesn't exist in the newest version so this
//  might be fully broken on that anyway?
//
// TODO: Actually! Separate jars for 1.21.1 and newer would be fun anyway cause statistics of which one is used more :D
@ApiStatus.Internal
public class LootTableIdGetterImpl implements LootTableIdGetter {

    @Override
    public top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier get(@NotNull EntityType<?> entityType) {
        return new IdentifierWrapper(VersionSpecific.Entity.get.apply(entityType));
    }

    @Override
    public top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier get(@NotNull Block block) {
        return new IdentifierWrapper(VersionSpecific.Block.get.apply(block));
    }


    // Should be executed when class is first accessed, which I think should only happen when using action or predicate Builders which are probably only used with datagen so this should only make datagen stuff crash but still work when used by a player?
    private static class VersionSpecific {
        private static final MappingResolver RESOLVER = FabricLoader.getInstance().getMappingResolver();

        private static class Entity {
            private static final String V1d21d2 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Ljava/util/Optional;");
            private static final String V1d20d5 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Lnet/minecraft/class_5321;");

            // mod only supports down to 1.20.5 soo: private static final String V1d14d0 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1299", "method_16351", "()Lnet/minecraft/util/Identifier;");

            private static final Function<EntityType<?>, Identifier> get;

            static {
                // TODO: I had the below statement commented out, maybe it doesnt work? use this then: final Class<?> entityType = Class.forName(RESOLVER.mapClassName("intermediary", "net.minecraft.class_1299"));
                final Class<?> entityType = EntityType.class;
                final Method finalMethod;
                Method method;

                // 1.21.2 to future:tm:
                if ((method = getMethod(entityType, V1d21d2)) != null) {
                    method.setAccessible(true);
                    finalMethod = method;
                    get = entity -> {
                        try {
                            @SuppressWarnings("unchecked") final Optional<RegistryKey<LootTable>> optional = (Optional<RegistryKey<LootTable>>) finalMethod.invoke(entity);
                            if (optional.isPresent()) return optional.get().getValue();
                            throw new IllegalStateException("Entity '" + entity + "' has no loot table! (It is created with 'builder.dropsNothing()')");
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };
                }

                // 1.20.5 to 1.21.1
                else if ((method = getMethod(entityType, V1d20d5)) != null) {
                    method.setAccessible(true);
                    finalMethod = method;
                    get = entity -> {
                        try {
                            return ((RegistryKey<?>) finalMethod.invoke(entity)).getValue();
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };
                } else {
                    throw new IllegalStateException("No valid way to get entity loot table id found!");
                }
            }
        }
        private static class Block {
            private static final String V1d21d2 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_4970", "method_26162", "()Ljava/util/Optional;");
            private static final String V1d20d5 = RESOLVER.mapMethodName("intermediary", "net.minecraft.class_4970", "method_26162", "()Lnet/minecraft/class_5321;");

            private static final Function<AbstractBlock, Identifier> get;

            static {
                final Class<?> abstractBlock = AbstractBlock.class;
                final Method finalMethod;
                Method method;

                // 1.21.2 to future:tm:
                if ((method = getMethod(abstractBlock, V1d21d2)) != null) {
                    method.setAccessible(true);
                    finalMethod = method;
                    get = entity -> {
                        try {
                            @SuppressWarnings("unchecked") final Optional<RegistryKey<LootTable>> optional = (Optional<RegistryKey<LootTable>>) finalMethod.invoke(entity);
                            if (optional.isPresent()) return optional.get().getValue();
                            throw new IllegalStateException("Entity '" + entity + "' has no loot table! (It is created with 'builder.dropsNothing()')");
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };
                }

                // 1.20.5 to 1.21.1
                else if ((method = getMethod(abstractBlock, V1d20d5)) != null) {
                    method.setAccessible(true);
                    finalMethod = method;
                    get = entity -> {
                        try {
                            return ((RegistryKey<?>) finalMethod.invoke(entity)).getValue();
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    };
                } else {
                    throw new IllegalStateException("No valid way to get entity loot table id found!");
                }
            }
        }

        private static @Nullable Method getMethod(Class<?> clazz, String method) {
            try {
                return clazz.getDeclaredMethod(method);
            } catch (NoSuchMethodException e) {
                LOGGER.warn("Method '%s' not valid for getting loot table id on this version, trying next method...", e);
                return null;
            }
        }
    }
}
