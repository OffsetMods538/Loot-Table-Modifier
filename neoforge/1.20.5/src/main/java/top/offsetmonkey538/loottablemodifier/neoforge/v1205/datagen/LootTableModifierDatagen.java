//package top.offsetmonkey538.loottablemodifier.neoforge.v1205.datagen;
//
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.loot.LootTableProvider;
//import net.minecraft.data.loot.LootTableSubProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.storage.loot.LootPool;
//import net.minecraft.world.level.storage.loot.LootTable;
//import net.minecraft.world.level.storage.loot.entries.LootItem;
//import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
//import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
//import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
//import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
//import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
//import net.neoforged.neoforge.common.NeoForge;
//import net.neoforged.neoforge.data.event.GatherDataEvent;
//import top.offsetmonkey538.loottablemodifier.neoforge.api.datagen.LootModifierProvider;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.LootModifier;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.condition.ConditionAddAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryAddAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryRemoveAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.table.TablePredicate;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.util.RegexPattern;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool.PoolAddAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.entry.EntryItemSetAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.action.pool.PoolRemoveAction;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.entry.EntryItemPredicate;
//import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ItemWrapper;
//import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;
//import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootPoolWrapper;
//import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;
//import top.offsetmonkey538.loottablemodifier.modded.v1205.mixin.LootContextTypesAccessor;
//import top.offsetmonkey538.loottablemodifier.neoforge.platform.LootTableModifierInitializer;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.BiConsumer;
//
//import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.MOD_ID;
//import static top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformMain.id;
//
///**
// * Datagen for loot modifiers used for testing.
// */
//public final class LootTableModifierDatagen implements LootTableModifierInitializer.Datagen {
//
//    @Override
//    public void init() {
//        NeoForge.EVENT_BUS.addListener(GatherDataEvent.class, event -> {
//            final DataGenerator.PackGenerator pack = event.getGenerator().getBuiltinDatapack(true, MOD_ID + "/example_pack");
//
//            pack.addProvider(packOutput -> new ModLootModifierProvider(packOutput, event.getLookupProvider()));
//            pack.addProvider(packOutput -> new LootTableProvider(
//                    packOutput,
//                    Set.of(),
//                    List.of()
//            ));
//        });
//    }
//
//    private static class ModLootModifierProvider extends LootModifierProvider {
//        public ModLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
//            super(output, lookupProvider, MOD_ID);
//        }
//
//        @Override
//        protected void gather() {
//            unconditional(
//                    id("replace_ingots_with_command_block"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    EntryItemPredicate.builder(RegexPattern.compile("minecraft:.*_ingot"))
//                            )
//                            .action(
//                                    EntryItemSetAction.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.COMMAND_BLOCK)))
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("sugarcane_drop_tnt"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.SUGAR_CANE)))
//                            )
//                            .action(
//                                    PoolAddAction.builder()
//                                            .pool(
//                                                    new LootPoolWrapper(LootPool.lootPool()
//                                                            .setRolls(ConstantValue.exactly(1))
//                                                            .add(
//                                                                    LootItem.lootTableItem(Items.TNT)
//                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
//                                                            ).build())
//                                            )
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("mobs_drop_tnt"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder()
//                                            .name(EntityType.CREEPER.getDefaultLootTable().toString()) // TODO: absraction for LootTableIdGetter in modded
//                                            .name(EntityType.ZOMBIE.getDefaultLootTable().toString())
//                            )
//                            .action(
//                                    PoolAddAction.builder()
//                                            .pool(
//                                                    new LootPoolWrapper(LootPool.lootPool()
//                                                            .setRolls(ConstantValue.exactly(1))
//                                                            .add(
//                                                                    LootItem.lootTableItem(Items.TNT)
//                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
//                                                            ).build())
//                                            )
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("empty_table_test"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder()
//                                            .name(id("test_empty_table").toString())
//                            )
//                            .action(
//                                    PoolAddAction.builder()
//                                            .pool(
//                                                    new LootPoolWrapper(LootPool.lootPool()
//                                                            .setRolls(ConstantValue.exactly(1))
//                                                            .add(
//                                                                    LootItem.lootTableItem(Items.TNT)
//                                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
//                                                            ).build())
//                                            )
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("remove_pools_with_sticks"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.STICK)))
//                                            // Exclude witch to test if AllOf and Inverted work + so I can test RemoveEntry on witch.
//                                            .and(TablePredicate.builder().name(EntityType.WITCH.getDefaultLootTable().toString()).invert())
//                            )
//                            .action(
//                                    PoolRemoveAction.builder()
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("add_cake_entry_to_dirt_block"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder()
//                                            .name(Blocks.DIRT.getLootTable().toString())
//                            )
//                            .action(
//                                    EntryAddAction.builder()
//                                            .entry(
//                                                    LootPoolEntryWrapper.create(LootItem.lootTableItem(Items.CAKE)
//                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).build())
//                                            )
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("add_command_block_to_all_blocks"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder()
//                                            .type(LootContextTypesAccessor.getMAP().inverse().get(LootContextParamSets.BLOCK).toString())
//                            )
//                            .action(
//                                    EntryAddAction.builder()
//                                            .entry(
//                                                    LootPoolEntryWrapper.create(LootItem.lootTableItem(Items.CAKE)
//                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))).build())
//                                            )
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("remove_glowstone_and_gunpowder_from_witch"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder().name(EntityType.WITCH.getDefaultLootTable().toString())
//                                            .and(
//                                                    EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.GLOWSTONE_DUST)))
//                                                            .or(EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.GUNPOWDER))))
//                                            )
//                            )
//                            .action(
//                                    EntryRemoveAction.builder()
//                            )
//                            .build()
//            );
//            unconditional(
//                    id("squid_ink_sac_only_from_player"),
//                    LootModifier.builder()
//                            .conditionally(
//                                    TablePredicate.builder()
//                                            .name(EntityType.SQUID.getDefaultLootTable().toString())
//                                            .and(EntryItemPredicate.builder(new ItemWrapper(BuiltInRegistries.ITEM.wrapAsHolder(Items.INK_SAC))))
//                            )
//                            .action(
//                                    ConditionAddAction.builder()
//                                            .onlyPools()
//                                            .condition(
//                                                    new LootConditionWrapper(LootItemKilledByPlayerCondition.killedByPlayer().build())
//                                            )
//                            )
//                            .build()
//            );
//        }
//    }
//
//    private static final class LootProvider implements LootTableSubProvider {
//
//        @Override
//        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
//            consumer.accept(id("test_empty_table"), LootTable.lootTable());
//        }
//    }
//}
//