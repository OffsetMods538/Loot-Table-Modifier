package top.offsetmonkey538.loottablemodifier.fabric.mixin;

import com.google.gson.JsonElement;
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
import top.offsetmonkey538.loottablemodifier.fabric.platform.FabricPlatformMain;

@Mixin(
        value = ReloadableServerRegistries.class,
        priority = 900,
        remap = false
)
public abstract class ReloadableRegistriesMixin {
    @Inject(
            method = {
                    "method_61240(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    "method_61240(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",
                    "lambda$scheduleRegistryLoad$3(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",

                    "method_58279(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    "method_58279(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",
                    "lambda$scheduleElementParse$4(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;"
            },
            at = @At("RETURN")
    )
    private static <T> void loottablemodifier$modifyLootTables(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<WritableRegistry<?>> cir) {
        if (lootDataType != LootDataType.TABLE) return;
        //noinspection unchecked
        FabricPlatformMain.runModification(resourceManager, (Registry<LootTable>) cir.getReturnValue(), registryOps);
    }
}
