package top.offsetmonkey538.loottablemodifier.neoforge.v121.mixin;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.ResourceManagerWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootTableWrapper;
import top.offsetmonkey538.monkeylib538.modded.v121.api.wrapper.ModdedVersionIdentifier;

@Mixin(
        value = ReloadableServerRegistries.class,
        priority = 900
)
public abstract class ReloadableRegistriesMixin {
    @Inject(
            method = "lambda$scheduleElementParse$4(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",
            at = @At("RETURN")
    )
    private static <T> void loot_table_modifier$modifyLootTables(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<WritableRegistry<?>> cir) {
        if (lootDataType != LootDataType.TABLE) return;

        //noinspection unchecked
        final Registry<LootTable> lootRegistry = (Registry<LootTable>) cir.getReturnValue();

        LootTableModifierCommon.runModification(
                new ResourceManagerWrapper(resourceManager),
                lootRegistry.asLookup()
                        .listElements()
                        .map(registryEntry -> Pair.of(
                                ModdedVersionIdentifier.of(registryEntry.key().location()),
                                new LootTableWrapper(lootRegistry.get(registryEntry.key()))
                        )),
                registryOps
        );
    }
}
