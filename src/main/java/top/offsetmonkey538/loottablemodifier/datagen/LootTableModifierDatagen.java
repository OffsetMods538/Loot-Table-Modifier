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
import top.offsetmonkey538.loottablemodifier.api.datagen.NewLootModifierProvider;
import top.offsetmonkey538.loottablemodifier.resource.action.AddPoolAction;

import java.util.concurrent.CompletableFuture;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        //pack.addProvider(ModLootModifierProvider::new);
        pack.addProvider(NewModLootModifierProvider::new);
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
                                    ItemEntry.builder(Items.NETHERITE_SWORD).apply(
                                            EnchantWithLevelsLootFunction
                                                    .builder(lookup, UniformLootNumberProvider.create(20, 39))
                                            //.builder(UniformLootNumberProvider.create(20, 39))
                                    )
                            ),

                    LootTables.ABANDONED_MINESHAFT_CHEST
            );
        }
    }

    private static class NewModLootModifierProvider extends NewLootModifierProvider {
        public NewModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            addModifier(
                    id("drop_tnt"),


                    AddPoolAction.builder(
                            LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .with(
                                            ItemEntry.builder(Items.TNT)
                                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                    )
                    ),

                    EntityType.CREEPER,
                    EntityType.ZOMBIE
            );

            //todo: temp
            addModifier(
                    id("test"),

                    AddPoolAction.builder(
                            LootPool.builder()
                                    .with(
                                            ItemEntry.builder(Items.NETHERITE_SWORD).apply(
                                                    EnchantWithLevelsLootFunction
                                                            .builder(lookup, UniformLootNumberProvider.create(20, 39))
                                                    //.builder(UniformLootNumberProvider.create(20, 39))
                                            )
                                    )
                    ),

                    LootTables.ABANDONED_MINESHAFT_CHEST
            );
        }
    }
}
