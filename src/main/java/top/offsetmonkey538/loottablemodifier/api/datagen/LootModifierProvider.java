package top.offsetmonkey538.loottablemodifier.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import top.offsetmonkey538.loottablemodifier.registry.LootModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.MOD_ID;

/**
 * A datagen provider for creating loot modifiers.
 * Override {@link #generate(RegistryWrapper.WrapperLookup) generate()} and use the {@code addModifier()} methods to add modifiers.
 *
 * @see #addModifier(Identifier, LootTable.Builder, EntityType, EntityType...)
 * @see #addModifier(Identifier, LootTable.Builder, RegistryKey, RegistryKey...)
 * @see #addModifier(Identifier, LootTable.Builder, Identifier, Identifier...)
 */
public abstract class LootModifierProvider extends FabricCodecDataProvider<LootModifier> {
    private BiConsumer<Identifier, LootModifier> provider;

    public LootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, MOD_ID + "/loot_modifier", LootModifier.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, LootModifier> provider, RegistryWrapper.WrapperLookup lookup) {
        this.provider = provider;
        generate(lookup);
    }

    @Override
    public String getName() {
        return "Loot Table Modifiers";
    }

    /**
     * Override and use {@code addModifier()} methods to add modifiers.
     *
     * @param lookup A lookup for registries.
     * @see #addModifier(Identifier, LootTable.Builder, EntityType, EntityType...)
     * @see #addModifier(Identifier, LootTable.Builder, RegistryKey, RegistryKey...)
     * @see #addModifier(Identifier, LootTable.Builder, Identifier, Identifier...)
     */
    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    /**
     * Adds a new loot table modifier for the given {@link EntityType}s.
     *
     * @param name Name of this modifier
     * @param builder The loot table to add
     * @param modifies The {@link EntityType} to add the modifier to
     * @param modifiesAdditional Additional {@link EntityType}s to add the modifier to
     * @see #addModifier(Identifier, LootTable.Builder, RegistryKey, RegistryKey...)
     * @see #addModifier(Identifier, LootTable.Builder, Identifier, Identifier...)
     */
    protected void addModifier(Identifier name, LootTable.Builder builder, EntityType<?> modifies, EntityType<?>... modifiesAdditional) {
        addModifier(
                name,
                builder,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).map(EntityType::getLootTableId).map(RegistryKey::getValue).toList()
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link RegistryKey}s.
     *
     * @param name Name of this modifier
     * @param builder The loot table to add
     * @param modifies The {@link RegistryKey} to add the modifier to
     * @param modifiesAdditional Additional {@link RegistryKey}s to add the modifier to
     * @see #addModifier(Identifier, LootTable.Builder, EntityType, EntityType...)
     * @see #addModifier(Identifier, LootTable.Builder, Identifier, Identifier...)
     */
    protected void addModifier(Identifier name, LootTable.Builder builder, RegistryKey<?> modifies, RegistryKey<?>... modifiesAdditional) {
        addModifier(
                name,
                builder,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).map(RegistryKey::getValue).toList()
        );
    }

    /**
     * Adds a new loot table modifier for the given {@link Identifier}s.
     *
     * @param name Name of this modifier
     * @param builder The loot table to add
     * @param modifies The {@link Identifier} to add the modifier to
     * @param modifiesAdditional Additional {@link Identifier}s to add the modifier to
     * @see #addModifier(Identifier, LootTable.Builder, EntityType, EntityType...)
     * @see #addModifier(Identifier, LootTable.Builder, RegistryKey, RegistryKey...)
     */
    protected void addModifier(Identifier name, LootTable.Builder builder, Identifier modifies, Identifier... modifiesAdditional) {
        addModifier(
                name,
                builder,
                Stream.concat(Stream.of(modifies), Stream.of(modifiesAdditional)).toList()
        );
    }

    private void addModifier(Identifier name, LootTable.Builder builder, List<Identifier> modifies) {
        provider.accept(name, new LootModifier(
                builder.build(),
                modifies
        ));
    }
}