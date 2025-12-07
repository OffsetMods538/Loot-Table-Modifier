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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import top.offsetmonkey538.loottablemodifier.api.datagen.LootModifierProvider;
import top.offsetmonkey538.loottablemodifier.api.resource.LootModifier;
import top.offsetmonkey538.loottablemodifier.api.resource.action.condition.ConditionAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryRemoveAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.table.TablePredicate;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootTableIdGetter;
import top.offsetmonkey538.loottablemodifier.api.resource.util.RegexPattern;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.PoolAddAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.EntryItemSetAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.PoolRemoveAction;
import top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry.EntryItemPredicate;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.ItemWrapper;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.loot.LootConditionWrapper;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.loot.LootPoolWrapper;
import top.offsetmonkey538.loottablemodifier.impl.wrapper.loot.entry.LootPoolEntryWrapper;
import top.offsetmonkey538.loottablemodifier.mixin.LootContextTypesAccessor;

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
                                    EntryItemSetAction.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.COMMAND_BLOCK)))
                            )
            );
            addModifier(
                    id("sugarcane_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.SUGAR_CANE)))
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.builder()
                                                            .rolls(ConstantLootNumberProvider.create(1))
                                                            .with(
                                                                    ItemEntry.builder(Items.TNT)
                                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("mobs_drop_tnt"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(LootTableIdGetter.INSTANCE.get(EntityType.CREEPER).toString())
                                            .name(LootTableIdGetter.INSTANCE.get(EntityType.ZOMBIE).toString())
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.builder()
                                                            .rolls(ConstantLootNumberProvider.create(1))
                                                            .with(
                                                                    ItemEntry.builder(Items.TNT)
                                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("empty_table_test"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .name(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("test_empty_table")).getValue().toString())
                            )
                            .action(
                                    PoolAddAction.builder()
                                            .pool(
                                                    new LootPoolWrapper(LootPool.builder()
                                                            .rolls(ConstantLootNumberProvider.create(1))
                                                            .with(
                                                                    ItemEntry.builder(Items.TNT)
                                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)))
                                                            ).build())
                                            )
                            )
            );
            addModifier(
                    id("remove_pools_with_sticks"),
                    LootModifier.builder()
                            .conditionally(
                                    EntryItemPredicate.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.STICK)))
                                            // Exclude witch to test if AllOf and Inverted work + so I can test RemoveEntry on witch.
                                            .and(TablePredicate.builder().name(LootTableIdGetter.INSTANCE.get(EntityType.WITCH).toString()).invert())
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
                                            .name(LootTableIdGetter.INSTANCE.get(Blocks.DIRT).toString())
                            )
                            .action(
                                    EntryAddAction.builder()
                                            .entry(
                                                    LootPoolEntryWrapper.create(ItemEntry.builder(Items.CAKE)
                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))).build())
                                            )
                            )
            );
            addModifier(
                    id("add_command_block_to_all_blocks"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder()
                                            .type(LootContextTypesAccessor.getMAP().inverse().get(LootContextTypes.BLOCK).toString())
                            )
                            .action(
                                    EntryAddAction.builder()
                                            .entry(
                                                    LootPoolEntryWrapper.create(ItemEntry.builder(Items.CAKE)
                                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))).build())
                                            )
                            )
            );
            addModifier(
                    id("remove_glowstone_and_gunpowder_from_witch"),
                    LootModifier.builder()
                            .conditionally(
                                    TablePredicate.builder().name(LootTableIdGetter.INSTANCE.get(EntityType.WITCH).toString())
                                            .and(
                                                    EntryItemPredicate.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.GLOWSTONE_DUST)))
                                                            .or(EntryItemPredicate.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.GUNPOWDER))))
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
                                            .name(LootTableIdGetter.INSTANCE.get(EntityType.SQUID).toString())
                                            .and(EntryItemPredicate.builder(new ItemWrapper(Registries.ITEM.getEntry(Items.INK_SAC))))
                            )
                            .action(
                                    ConditionAddAction.builder()
                                            .onlyPools()
                                            .condition(
                                                    new LootConditionWrapper(KilledByPlayerLootCondition.builder().build())
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
