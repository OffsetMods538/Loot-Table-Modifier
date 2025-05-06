package top.offsetmonkey538.loottablemodifier.resource;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.action.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Using ArrayList as I want it to be modifiable because I empty it when applying, so I can check for things that weren't applied
public record LootModifier(@NotNull ArrayList<Identifier> modifies, @NotNull List<LootModifierAction> actions) {
    public static final Codec<LootModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(LootModifier::modifiesEither),
            Codec.either(LootModifierAction.CODEC, LootModifierAction.CODEC.listOf()).optionalFieldOf("actions").forGetter(LootModifier::actionsOptionalEither),
            LootPool.CODEC.listOf().optionalFieldOf("pools").forGetter(lootModifier -> Optional.empty()),
            LootPool.CODEC.listOf().optionalFieldOf("loot_pools").forGetter(lootModifier -> Optional.empty())
    ).apply(instance, LootModifier::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // From codec soo yeah
    private LootModifier(@NotNull Either<Identifier, List<Identifier>> modifiesEither, @NotNull Optional<Either<LootModifierAction, List<LootModifierAction>>> actions, @NotNull Optional<List<LootPool>> pools, @NotNull Optional<List<LootPool>> lootPools) {
        this(
                modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow())),
                getActions(actions, pools, lootPools)
        );
    }
    public LootModifier(@NotNull List<Identifier> modifies, @NotNull List<LootModifierAction> actions) {
        this(
                new ArrayList<>(modifies),
                Collections.unmodifiableList(actions)
        );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // From codec soo yeah
    private static List<LootModifierAction> getActions(@NotNull Optional<Either<LootModifierAction, List<LootModifierAction>>> actions, @NotNull Optional<List<LootPool>> pools, @NotNull Optional<List<LootPool>> lootPools) {
        List<LootModifierAction> result = null;

        if (actions.isPresent()) result = actions.get().right().orElseGet(() -> actions.get().left().isEmpty() ? null : List.of(actions.get().left().orElseThrow()));

        if (result != null && pools.isPresent()) throw new IllegalStateException("Both \"actions\" and \"pools\" present in loot modifier!");
        if (result != null && lootPools.isPresent()) throw new IllegalStateException("Both \"actions\" and \"loot_pools\" present in loot modifier!");
        if (pools.isPresent() && lootPools.isPresent()) throw new IllegalStateException("Both \"pools\" and \"loot_pools\" present in loot modifier!");

        if (result == null && pools.isPresent()) result = List.of(new AddPoolAction(pools.get()));
        if (result == null && lootPools.isPresent()) result = List.of(new AddPoolAction(lootPools.get()));

        if (result == null) throw new IllegalStateException("Neither \"actions\" nor \"pools\" present in loot modifier!");

        return result;
    }

    private Either<Identifier, List<Identifier>> modifiesEither() {
        if (modifies.size() == 1) return Either.left(modifies.get(0));
        return Either.right(modifies);
    }

    private Optional<Either<LootModifierAction, List<LootModifierAction>>> actionsOptionalEither() {
        if (actions.size() == 1) return Optional.of(Either.left(actions.get(0)));
        return Optional.of(Either.right(actions));
    }

    /**
     * @param tableRegistry registry of loot tables to modify
     * @return amount of loot tables modified
     */
    public int apply(final @NotNull Registry<LootTable> tableRegistry) {
        final List<RegistryKey<LootTable>> tableKeys = getRegistryAsWrapper(tableRegistry).streamEntries().map(RegistryEntry.Reference::registryKey).filter(key -> modifies.contains(key.getValue())).toList();
        if (tableKeys.isEmpty()) return 0;
        // At this point only ones in 'modifies' remain

        int modified = 0;

        for (RegistryKey<LootTable> key : tableKeys) {
            final LootTable table = tableRegistry.get(key);

            if (table == null) throw new IllegalStateException("Loot table with id '%s' is null!".formatted(key));

            modified += apply(table) ? 1 : 0;
            modifies.remove(key.getValue());
        }

        return modified;
    }

    /**
     * @param table table to modify
     * @return true when any of the 'actions' could be applied, false otherwise
     */
    private boolean apply(final @NotNull LootTable table) {
        boolean result = false;
        for (LootModifierAction action : actions) {
            if (action.apply(table)) result = true;
        }
        return result;
    }

    /*
	In 1.21.4, the 'Registry' class extends 'RegistryWrapper' and inherits the 'streamEntries' method from *it*.
	In 1.20.5, the 'Registry' class *doesn't* extend the 'RegistryWrapper' and implements its own 'streamEntries' method.
	Compiling on both versions works, because the names of the methods are the same, but they compile to different intermediary names, thus a jar compiled for 1.20.5 doesn't work on 1.21.4 and vice versa.
	Solution: Turn the 'Registry' into a 'RegistryWrapper' as its 'streamEntries' retains the same intermediary on both versions.
	If 'Registry' implements 'RegistryWrapper': cast it
	Else: call 'getReadOnlyWrapper' on the registry (doesn't exist on 1.21.4, otherwise would've used 'registry.getReadOnlyWrapper().streamEntries()')
	 */
    private static <T> RegistryWrapper<T> getRegistryAsWrapper(@NotNull Registry<T> registry) {
        //noinspection ConstantValue,RedundantSuppression: On lower versions, Registry doesn't extend RegistryWrapper and thus the 'isAssignableFrom' check can be false. The redundant supression is for the unchecked cast below.
        if (RegistryWrapper.class.isAssignableFrom(registry.getClass()))
            //noinspection unchecked,RedundantCast: I swear it casts 🤞
            return (RegistryWrapper<T>) registry;

        try {
            //noinspection unchecked: Seriously I swear 🤞🤞
            return (RegistryWrapper<T>) registry.getClass().getDeclaredMethod(FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_2378", "method_46771", "()Lnet/minecraft/class_7225$class_7226;")).invoke(registry);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // Don't think this is needed? public static NewLootModifier.Builder builder() {
    // Don't think this is needed?     return new NewLootModifier.Builder();
    // Don't think this is needed? }

    // Don't think this is needed? public static class Builder {
    // Don't think this is needed?     private final ImmutableList.Builder<Identifier> modifies = ImmutableList.builder();
    // Don't think this is needed?     private final ImmutableList.Builder<LootModifierAction> actions = ImmutableList.builder();
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier.Builder modifies(@NotNull Identifier... modifies) {
    // Don't think this is needed?         this.modifies.add(modifies);
    // Don't think this is needed?         return this;
    // Don't think this is needed?     }
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier.Builder conditionally(@NotNull LootModifierAction.Builder action) {
    // Don't think this is needed?         this.actions.add(action.build());
    // Don't think this is needed?         return this;
    // Don't think this is needed?     }
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier build() {
    // Don't think this is needed?         return new NewLootModifier(this.modifies.build(), this.actions.build());
    // Don't think this is needed?     }
    // Don't think this is needed? }
}
