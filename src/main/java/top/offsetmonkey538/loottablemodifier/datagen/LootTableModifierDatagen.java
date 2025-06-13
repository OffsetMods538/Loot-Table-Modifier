package top.offsetmonkey538.loottablemodifier.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;
import top.offsetmonkey538.loottablemodifier.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.resource.OptionalPattern;
import top.offsetmonkey538.loottablemodifier.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.resource.action.entry.SetItemAction;
import top.offsetmonkey538.loottablemodifier.resource.action.pool.RemovePoolAction;
import top.offsetmonkey538.loottablemodifier.resource.predicate.entry.ItemEntryPredicate;
import top.offsetmonkey538.loottablemodifier.resource.predicate.table.LootTablePredicate;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        pack.addProvider(NewModLootModifierProvider::new);
        pack.addProvider(LootProvider::new);
    }

    private static class NewModLootModifierProvider extends LootModifierProvider {
        public NewModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            addModifier(
                    id("replace_ingots_with_command_block"),
                    LootModifier.builder()
                            .conditionally(
                                    ItemEntryPredicate.builder(OptionalPattern.compile("minecraft:.*_ingot"))
                            )
                            .action(
                                    SetItemAction.builder(Items.COMMAND_BLOCK)
                            )
            );
            addModifier(
                    id("sugarcane_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    ItemEntryPredicate.builder(Items.SUGAR_CANE)
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
            addModifier(
                    id("empty_table_test"),
                    LootModifier.builder()
                            .conditionally(
                                    LootTablePredicate.builder()
                                            .name(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("test_empty_table")))
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
                    id("remove_pools_with_sticks"),
                    LootModifier.builder()
                            .conditionally(
                                    ItemEntryPredicate.builder(Items.STICK)
                            )
                            .action(
                                    RemovePoolAction.builder()
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
