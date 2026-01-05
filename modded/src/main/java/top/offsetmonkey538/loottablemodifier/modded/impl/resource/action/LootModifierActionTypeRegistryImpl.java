package top.offsetmonkey538.loottablemodifier.modded.impl.resource.action;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;

import static top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformMain.id;

public final class LootModifierActionTypeRegistryImpl implements LootModifierActionType.Registry {
    public static final Registry<LootModifierActionType> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(id("loot_modifier_action_types")), Lifecycle.stable()
    );

    @Override
    public LootModifierActionType register(@NotNull Identifier id, @NotNull LootModifierActionType type) {
        return Registry.register(REGISTRY, ((IdentifierWrapper) id).vanillaIdentifier(), type);
    }
}
