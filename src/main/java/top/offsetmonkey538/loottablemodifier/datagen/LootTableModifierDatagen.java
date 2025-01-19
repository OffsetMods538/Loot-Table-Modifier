package top.offsetmonkey538.loottablemodifier.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;

import java.util.concurrent.CompletableFuture;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        pack.addProvider(ModLootModifierProvider::new);
    }

    private static class ModLootModifierProvider extends LootModifierProvider {
        public ModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            addModifier(
                    id("drop_tnt"),


                    LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(
                                    ItemEntry.builder(Items.TNT)
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                            ),

                    EntityType.CREEPER,
                    EntityType.ZOMBIE
            );

            //todo: temp
            addModifier(
                    id("test"),

                    LootPool.builder()
                            .with(
                                    ItemEntry.builder(Items.DIRT).apply(
                                            EnchantWithLevelsLootFunction
                                                    .builder(lookup, UniformLootNumberProvider.create(20, 39))
                                    )
                            ),

                    LootTables.END_CITY_TREASURE_CHEST
            );
        }
    }
}
