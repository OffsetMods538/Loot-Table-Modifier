package top.offsetmonkey538.loottablemodifier.resource.action;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.LootTableModifier;

public record LootModifierActionType(@NotNull MapCodec<? extends LootModifierAction> codec) {
    public static final Registry<LootModifierActionType> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(LootTableModifier.id("loot_modifier_action_types")), Lifecycle.stable()
    );
}
