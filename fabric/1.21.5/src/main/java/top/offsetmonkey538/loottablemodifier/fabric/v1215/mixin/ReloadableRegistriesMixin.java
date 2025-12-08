package top.offsetmonkey538.loottablemodifier.fabric.v1215.mixin;

import com.google.gson.JsonElement;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.LootTableModifier;

@Mixin(
        value = ReloadableRegistries.class,
        priority = 900,
        remap = false
)
public abstract class ReloadableRegistriesMixin {
    @Inject(
            method = {
                    // 1.21.2 and up: intermediary
                    "method_61240(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    // 1.21.2 and up: yarn
                    "method_61240(Lnet/minecraft/loot/LootDataType;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps;)Lnet/minecraft/registry/MutableRegistry;",
                    // 1.21.2 and up: mojmap
                    "lambda$scheduleRegistryLoad$3(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;",

                    // 1.20.5 to 1.21.1: intermediary
                    "method_58279(Lnet/minecraft/class_8490;Lnet/minecraft/class_3300;Lnet/minecraft/class_6903;)Lnet/minecraft/class_2385;",
                    // 1.20.5 to 1.21.1: yarn
                    "method_58279(Lnet/minecraft/loot/LootDataType;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps;)Lnet/minecraft/registry/MutableRegistry;",
                    // 1.20.5 to 1.21.1: mojmap
                    "lambda$scheduleElementParse$4(Lnet/minecraft/world/level/storage/loot/LootDataType;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/RegistryOps;)Lnet/minecraft/core/WritableRegistry;"
            },
            at = @At("RETURN")
    )
    private static <T> void loottablemodifier$modifyLootTables(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<MutableRegistry<?>> cir) {
        if (lootDataType != LootDataType.LOOT_TABLES) return;
        //noinspection unchecked
        LootTableModifier.runModification(resourceManager, (Registry<LootTable>) cir.getReturnValue(), registryOps);
    }
}
