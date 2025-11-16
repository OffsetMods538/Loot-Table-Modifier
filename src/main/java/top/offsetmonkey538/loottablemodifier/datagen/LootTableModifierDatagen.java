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
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
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
import top.offsetmonkey538.loottablemodifier.api.resource.action.condition.ConditionAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryRemoveAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.TablePredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.PoolAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryItemSetAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.PoolRemoveAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry.EntryItemPredicate;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

/**
 * Datagen for loot modifiers used for testing.
 */
public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        pack.addProvider(ModLootModifierProvider::new);
        pack.addProvider(LootProvider::new);
    }

    private static class ModLootModifierProvider extends LootModifierProvider {
        public ModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(RegistryWrapper.WrapperLookup lookup) {
            addModifier(
                    id("replace_ingots_with_command_block"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(RegexPattern.compile("minecraft:.*_ingot"))
                            )
                            .action(
                                    EntryItemSetAction.builder(Items.COMMAND_BLOCK)
                            )
            );
            addModifier(
                    id("sugarcane_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(Items.SUGAR_CANE)
                            )
                            .action(
                                    PoolAddAction.builder()
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
                                    TablePredicate.builder()
                                            .name(EntityType.CREEPER)
                                            .name(EntityType.ZOMBIE)
                            )
                            .action(
                                    PoolAddAction.builder()
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
                                    TablePredicate.builder()
                                            .name(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("test_empty_table")))
                            )
                            .action(
                                    PoolAddAction.builder()
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
                                    EntryItemPredicate.builder(Items.STICK)
                                            // Exclude witch to test if AllOf and Inverted work + so I can test RemoveEntry on witch.
                                            .and(TablePredicate.builder().name(EntityType.WITCH).invert())
                            )
                            .action(
                                    PoolRemoveAction.builder()
                            )
            );
            addModifier(
                    id("add_cake_entry_to_dirt_block"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(Blocks.DIRT)
                            )
                            .action(
                                    EntryAddAction.builder()
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
                                    TablePredicate.builder()
                                            .type(LootContextTypes.BLOCK)
                            )
                            .action(
                                    EntryAddAction.builder()
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
                                    TablePredicate.builder().name(EntityType.WITCH)
                                            .and(
                                                    EntryItemPredicate.builder(Items.GLOWSTONE_DUST)
                                                            .or(EntryItemPredicate.builder(Items.GUNPOWDER))
                                            )
                            )
                            .action(
                                    EntryRemoveAction.builder()
                            )
            );
            addModifier(
                    id("squid_ink_sac_only_from_player"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(EntityType.SQUID)
                                            .and(EntryItemPredicate.builder(Items.INK_SAC))
                            )
                            .action(
                                    ConditionAddAction.builder()
                                            .onlyPools()
                                            .condition(
                                                    KilledByPlayerLootCondition.builder()
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
