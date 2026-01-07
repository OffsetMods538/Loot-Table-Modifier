package top.offsetmonkey538.loottablemodifier.fabric.v121.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import top.offsetmonkey538.loottablemodifier.common.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.condition.ConditionAddAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryAddAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryRemoveAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.table.TablePredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool.PoolAddAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryItemSetAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool.PoolRemoveAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.entry.EntryItemPredicate;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootPoolWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v121.mixin.LootContextTypesAccessor;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformMain.id;

/**
 * Datagen for loot modifiers used for testing.
 */
public class LootTableModifierDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        //final FabricDataGenerator.Pack pack = fabricDataGenerator.createBuiltinResourcePack(id("example_pack"));

        //pack.addProvider(ModLootModifierProvider::new);
        //pack.addProvider(LootProvider::new);
    }
/*
    private static class ModLootModifierProvider extends LootModifierProvider {
        public ModLootModifierProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate(HolderLookup.Provider lookup) {
            addModifier(
                    id("replace_ingots_with_command_block"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(RegexPattern.compile("minecraft:.*_ingot"))
                            )
                            .action(
                                    EntryItemSetAction.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.COMMAND_BLOCK)))
                            )
            );
            addModifier(
                    id("sugarcane_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.SUGAR_CANE)))
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.lootPool()
                                                            .setRolls(ConstantValue.exactly(1))
                                                            .add(
                                                                    LootItem.lootTableItem(Items.TNT)
                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("mobs_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(EntityType.CREEPER.getDefaultLootTable().location().toString())
                                            .name(EntityType.ZOMBIE.getDefaultLootTable().location().toString())
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.lootPool()
                                                            .setRolls(ConstantValue.exactly(1))
                                                            .add(
                                                                    LootItem.lootTableItem(Items.TNT)
                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("empty_table_test"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(ResourceKey.create(Registries.LOOT_TABLE, id("test_empty_table")).location().toString())
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.lootPool()
                                                            .setRolls(ConstantValue.exactly(1))
                                                            .add(
                                                                    LootItem.lootTableItem(Items.TNT)
                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("remove_pools_with_sticks"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.STICK)))
                                            // Exclude witch to test if AllOf and Inverted work + so I can test RemoveEntry on witch.
                                            .and(TablePredicate.builder().name(EntityType.WITCH.getDefaultLootTable().location().toString()).invert())
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
                                            .name(Blocks.DIRT.getLootTable().location().toString())
                            )
                            .action(
                                    EntryAddAction.builder()
                                            .entry(
                                                    LootPoolEntryWrapper.create(LootItem.lootTableItem(Items.CAKE)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).build())
                                            )
                            )
            );
            addModifier(
                    id("add_command_block_to_all_blocks"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .type(LootContextTypesAccessor.getMAP().inverse().get(LootContextParamSets.BLOCK).toString())
                            )
                            .action(
                                    EntryAddAction.builder()
                                            .entry(
                                                    LootPoolEntryWrapper.create(LootItem.lootTableItem(Items.CAKE)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).build())
                                            )
                            )
            );
            addModifier(
                    id("remove_glowstone_and_gunpowder_from_witch"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder().name(EntityType.WITCH.getDefaultLootTable().location().toString())
                                            .and(
                                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.GLOWSTONE_DUST)))
                                                            .or(EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.GUNPOWDER))))
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
                                            .name(EntityType.SQUID.getDefaultLootTable().location().toString())
                                            .and(EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.INK_SAC))))
                            )
                            .action(
                                    ConditionAddAction.builder()
                                            .onlyPools()
                                            .condition(
                                                    new LootConditionWrapper(LootItemKilledByPlayerCondition.killedByPlayer().build())
                                            )
                            )
            );
        }
    }

    private static class LootProvider extends SimpleFabricLootTableProvider {
        public LootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup, LootContextParamSets.CHEST);
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, id("test_empty_table")), LootTable.lootTable());
        }
    }

 */
}
