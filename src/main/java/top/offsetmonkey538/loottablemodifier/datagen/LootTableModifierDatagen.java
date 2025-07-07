package top.offsetmonkey538.loottablemodifier.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.AddEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.RemoveEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.util.OptionalIdentifierPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.SetItemAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.RemovePoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry.ItemEntryPredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.LootTablePredicate;

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
                                    ItemEntryPredicate.builder(OptionalIdentifierPattern.compile("minecraft:.*_ingot"))
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
                                    AddPoolAction.builder()
                                            .pool(
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
                                    AddPoolAction.builder()
                                            .pool(
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
                                    AddPoolAction.builder()
                                            .pool(
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
                                            // Exclude witch to test if AllOf and Inverted work + so I can test RemoveEntry on witch.
                                            .and(LootTablePredicate.builder().name(EntityType.WITCH).invert())
                            )
                            .action(
                                    RemovePoolAction.builder()
                            )
            );
            addModifier(
                    id("add_cake_entry_to_dirt_block"),
                    LootModifier.builder()
                            .conditionally(
                                    LootTablePredicate.builder()
                                            .name(Blocks.DIRT)
                            )
                            .action(
                                    AddEntryAction.builder()
                                            .entry(
                                                    ItemEntry.builder(Items.CAKE)
                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                            )
                            )
            );
            addModifier(
                    id("add_command_block_to_all_blocks"),
                    LootModifier.builder()
                            .conditionally(
                                    LootTablePredicate.builder()
                                            .type(LootContextTypes.BLOCK)
                            )
                            .action(
                                    AddEntryAction.builder()
                                            .entry(
                                                    ItemEntry.builder(Items.CAKE)
                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                            )
                            )
            );
            addModifier(
                    id("remove_glowstone_and_gunpowder_from_witch"),
                    LootModifier.builder()
                            .conditionally(
                                    LootTablePredicate.builder().name(EntityType.WITCH)
                                            .and(
                                                    ItemEntryPredicate.builder(Items.GLOWSTONE_DUST)
                                                            .or(ItemEntryPredicate.builder(Items.GUNPOWDER))
                                            )
                            )
                            .action(
                                    RemoveEntryAction.builder()
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
