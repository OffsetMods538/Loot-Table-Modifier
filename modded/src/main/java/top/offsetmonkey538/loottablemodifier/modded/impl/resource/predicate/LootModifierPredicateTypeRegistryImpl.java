package top.offsetmonkey538.loottablemodifier.modded.impl.resource.predicate;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;

import static top.offsetmonkey538.loottablemodifier.modded.platform.ModdedPlatformMain.id;

public final class LootModifierPredicateTypeRegistryImpl implements LootModifierPredicateType.Registry {
    public static final Registry<LootModifierPredicateType> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(id("loot_modifier_predicate_types")), Lifecycle.stable()
    );

    @Override
    public top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType register(@NotNull Identifier id, @NotNull top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType type) {
        return Registry.register(REGISTRY, ((IdentifierWrapper) id).vanillaIdentifier(), type);
    }
}
