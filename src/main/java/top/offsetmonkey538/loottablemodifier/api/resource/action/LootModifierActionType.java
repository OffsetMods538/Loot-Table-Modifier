package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.LootTableModifier;

/**
 * The type of a {@link LootModifierAction}, holds the codec.
 *
 * @param codec the codec for this action.
 */
public record LootModifierActionType(@NotNull MapCodec<? extends LootModifierAction> codec) {
    /**
     * Registry of {@link LootModifierActionType}s
     */
    public static final Registry<LootModifierActionType> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(LootTableModifier.id("loot_modifier_action_types")), Lifecycle.stable()
    );
}
