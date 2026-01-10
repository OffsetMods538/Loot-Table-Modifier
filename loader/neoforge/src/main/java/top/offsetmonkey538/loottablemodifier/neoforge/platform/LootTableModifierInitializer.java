package top.offsetmonkey538.loottablemodifier.neoforge.platform;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;

@Mod("loot_table_modifier")
public class LootTableModifierInitializer {

    public LootTableModifierInitializer(IEventBus modEventBus, ModContainer modContainer) {
        LootTableModifierCommon.initialize();
        //TODO: if (IS_DEV) NeoForge.EVENT_BUS.addListener(AddPackFindersEvent.class, event -> event.addPackFinders(
        //TODO:         id("example_pack"),
        //TODO:         PackType.SERVER_DATA,
        //TODO:         Component.literal("Example Pack"),
        //TODO:         PackSource.BUILT_IN,
        //TODO:         false,
        //TODO:         Pack.Position.TOP
        //TODO: ));
    }
}
