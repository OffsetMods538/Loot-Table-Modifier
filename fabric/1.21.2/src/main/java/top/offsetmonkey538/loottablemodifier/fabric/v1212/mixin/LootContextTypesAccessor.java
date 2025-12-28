package top.offsetmonkey538.loottablemodifier.fabric.v1212.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootContextParamSets.class)
public interface LootContextTypesAccessor {
    @Accessor("REGISTRY")
    static BiMap<ResourceLocation, ContextKeySet> getMAP() {
        throw new AssertionError();
    }
}
