package top.offsetmonkey538.loottablemodifier.modded.mixin;

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
import top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformMain;

@Mixin(
        value = ReloadableServerRegistries.class,
        priority = 900,
        remap = false
)
// TODO: move to different subprojects instead of this maybe? Though I mean I guess it works so maybe not....?
// todo: fixme: yeah i have no idea how this will work in 1.20.1
public abstract class ReloadableRegistriesMixin {
    @Inject(
            method = {
                    // Everything >=1.21.2
                    "method_61240(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    "method_61240(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",
                    "lambda$scheduleRegistryLoad$3(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",

                    // >=1.20.5 <=1.21.1
                    "method_58279(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    "method_58279(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",
                    "lambda$scheduleElementParse$4(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;"
            },
            at = @At("RETURN")
    )
    private static <T> void loottablemodifier$modifyLootTables(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<WritableRegistry<?>> cir) {
        if (lootDataType != LootDataType.TABLE) return;
        //noinspection unchecked
        ModdedPlatformMain.runModification(resourceManager, (Registry<LootTable>) cir.getReturnValue(), registryOps);
    }
}
