package top.offsetmonkey538.loottablemodifier.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.loottable.LootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.context.ContextType;
import top.offsetmonkey538.loottablemodifier.api.datagen.NewLootModifierProvider;
import top.offsetmonkey538.loottablemodifier.api.datagen.NewNewLootModifierProvider;
import top.offsetmonkey538.loottablemodifier.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.resource.action.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.resource.action.SetItemAction;
import top.offsetmonkey538.loottablemodifier.resource.predicate.entry.ItemEntryPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.table.LootTablePredicate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        pack.addProvider(NewModLootModifierProvider::new);
        pack.addProvider(LootProvider::new);
    }

    //private static class NewModLootModifierProvider extends NewLootModifierProvider {
    //    public NewModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    //        super(dataOutput, registriesFuture);
    //    }

    //    @Override
    //    protected void generate(RegistryWrapper.WrapperLookup lookup) {
    //        addModifier(
    //                id("drop_tnt"),


    //                AddPoolAction.builder(
    //                        LootPool.builder()
    //                                .rolls(ConstantLootNumberProvider.create(1))
    //                                .with(
    //                                        ItemEntry.builder(Items.TNT)
    //                                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
    //                                )
    //                ),

    //                EntityType.CREEPER,
    //                EntityType.ZOMBIE
    //        );

    //        //todo: temp
    //        addModifier(
    //                id("test"),

    //                AddPoolAction.builder(
    //                        LootPool.builder()
    //                                .with(
    //                                        ItemEntry.builder(Items.NETHERITE_SWORD).apply(
    //                                                EnchantWithLevelsLootFunction
    //                                                        .builder(lookup, UniformLootNumberProvider.create(20, 39))
    //                                                //.builder(UniformLootNumberProvider.create(20, 39))
    //                                        )
    //                                )
    //                ),

    //                LootTables.ABANDONED_MINESHAFT_CHEST
    //        );
    //    }
    //}
    private static class NewModLootModifierProvider extends NewNewLootModifierProvider {
        public NewModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            addModifier(
                    id("replace_ingots_with_command_block"),
                    LootModifier.builder()
                            .conditionally(
                                    ItemEntryPredicate.builder(Pattern.compile("minecraft:.*_ingot"))
                            )
                            .action(
                                    SetItemAction.builder(Items.COMMAND_BLOCK)
                            )
            );
            addModifier(
                    id("sugarcane_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    ItemEntryPredicate.builder(Pattern.compile("minecraft:sugar_cane"))
                            )
                            .action(
                                    AddPoolAction.builder(
                                            LootPool.builder()
                                                    .rolls(ConstantLootNumberProvider.create(1))
                                                    .with(
                                                            ItemEntry.builder(Items.TNT)
                                                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                                    )
                                    )
                            )
            );
            addModifier(
                    id("mobs_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    LootTablePredicate.builder()
                                            .name(EntityType.CREEPER)
                                            .name(EntityType.ZOMBIE)
                            )
                            .action(
                                    AddPoolAction.builder(
                                            LootPool.builder()
                                                    .rolls(ConstantLootNumberProvider.create(1))
                                                    .with(
                                                            ItemEntry.builder(Items.TNT)
                                                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                                    )
                                    )
                            )
            );
        }
    }

    private static class LootProvider extends SimpleFabricLootTableProvider {
        public LootProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.CHEST);
        }

        @Override
        public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
            lootTableBiConsumer.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("test_empty_table")), LootTable.builder());
        }
    }
}
