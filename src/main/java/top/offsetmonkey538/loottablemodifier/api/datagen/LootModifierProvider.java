package top.offsetmonkey538.loottablemodifier.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

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
        return "New New Loot Table Modifiers"; // todo: change
    }

    /**
     * Override and use {@code addModifier()} methods to add modifiers.
     *
     * @param lookup A lookup for registries.
     */
    protected abstract void generate(RegistryWrapper.WrapperLookup lookup);

    protected void addModifier(@NotNull Identifier name, @NotNull LootModifier.Builder builder) {
        provider.accept(name, builder.build());
    }
}
