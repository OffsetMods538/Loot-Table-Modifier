package top.offsetmonkey538.loottablemodifier.mixin;

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
import top.offsetmonkey538.loottablemodifier.LootTableModifier;

@Mixin(
        value = ReloadableRegistries.class,
        priority = 900
)
public abstract class ReloadableRegistriesMixin {
    @Inject(
            method = "method_58279",
            at = @At("RETURN")
    )
    private static <T> void loottablemodifier$modifyLootTables(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<MutableRegistry<?>> cir) {
        if (lootDataType != LootDataType.LOOT_TABLES) return;

        //noinspection unchecked
        LootTableModifier.runModification(resourceManager, (Registry<LootTable>) cir.getReturnValue(), registryOps);
    }
}
