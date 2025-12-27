package top.offsetmonkey538.loottablemodifier.fabric.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.MOD_ID;

/**
 * A datagen provider for creating loot modifiers.
 * <br>
 * Override {@link #generate(RegistryWrapper.WrapperLookup) generate()} and use the {@link #addModifier(Identifier, LootModifier.Builder)} method to add modifiers.
 * <br>
 * <pre>{@code
 * @Override
 * protected void generate(RegistryWrapper.WrapperLookup lookup) {
 *      addModifier(
 *          Identifier.of("testmod", "mobs_drop_tnt"),
 *              LootModifier.builder()
 *                  .conditionally(
 *                      TablePredicate.builder()
 *                          .name(EntityType.CREEPER)
 *                          .name(EntityType.ZOMBIE)
 *                  )
 *                  .action(
 *                      PoolAddAction.builder()
 *                          .pool(
 *                              LootPoolWrapper.builder()
 *                                  .rolls(ConstantLootNumberProvider.create(1))
 *                                  .with(
 *                                      ItemEntry.builder(Items.TNT)
 *                                          .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
 *                                  )
 *                          )
 *                  )
 *     );
 * }
 * }</pre>
 */
// TODO: FIXME: Datagen part of api should be platform specific and probably include helper methods for turning vanilla things into wrappers cause calling the constructors seems annoying
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
     * Override and use {@link #addModifier(Identifier, LootModifier.Builder)} method to add modifiers.
     *
     * @param lookup A lookup for registries.
     */
    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    /**
     * Adds a loot modifier using the provided name and builder.
     *
     * @param name the identifier of the modifier
     * @param builder the builder to generate the loot modifier from
     */
    protected void addModifier(@NotNull Identifier name, @NotNull LootModifier.Builder builder) {
        provider.accept(name, builder.build());
    }
}
