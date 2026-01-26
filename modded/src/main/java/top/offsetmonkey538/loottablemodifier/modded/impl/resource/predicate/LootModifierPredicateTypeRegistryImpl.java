package top.offsetmonkey538.loottablemodifier.modded.impl.resource.predicate;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.monkeylib538.modded.api.resource.ResourceKeyApi;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.id;

public final class LootModifierPredicateTypeRegistryImpl implements LootModifierPredicateType.Registry {
    public static final Registry<LootModifierPredicateType> REGISTRY = new MappedRegistry<>(
            ResourceKeyApi.createRegistry(id("loot_modifier_predicate_types")),
            Lifecycle.stable()
    );

    @Override
    public LootModifierPredicateType register(Identifier id, LootModifierPredicateType type) {
        return Registry.register(REGISTRY, ResourceKeyApi.create(REGISTRY.key(), id), type);
    }
}
