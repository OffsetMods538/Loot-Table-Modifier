package top.offsetmonkey538.loottablemodifier.fabric.v1201.mixin;

import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ResourceManagerWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.monkeylib538.modded.api.wrapper.ModdedIdentifier;

import java.util.Map;

@Mixin(
        value = LootDataManager.class,
        priority = 900
)
public abstract class LootDataManagerMixin {
    @Inject(
            // Works for fabric >=1.20.1 <=1.20.4
            method = "method_51189(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/world/level/storage/loot/LootDataType;Ljava/util/Map;)V",
            at = @At("RETURN")
    )
    private static <T> void loot_table_modifier$modifyLootTables(ResourceManager resourceManager, LootDataType<T> lootDataType, Map<ResourceLocation, T> map, CallbackInfo ci) {
        if (lootDataType != LootDataType.TABLE) return;

        //noinspection unchecked
        final Map<ResourceLocation, LootTable> lootRegistry = (Map<ResourceLocation, LootTable>) map;

        LootTableModifierCommon.runModification(
                new ResourceManagerWrapper(resourceManager),
                lootRegistry.entrySet()
                        .stream()
                        .map(entry -> Pair.of(
                                ModdedIdentifier.of(entry.getKey()), // TODO: can do like this?
                                new LootTableWrapper(entry.getValue())
                        )),
                JsonOps.INSTANCE // TODO: Is a RegistryOps not needed on 1.20.1? Doesn't look like the loot table loader uses it so maybe not?
        );
    }
}
