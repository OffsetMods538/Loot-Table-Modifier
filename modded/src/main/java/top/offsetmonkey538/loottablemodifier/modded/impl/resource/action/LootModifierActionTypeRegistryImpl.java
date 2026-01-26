package top.offsetmonkey538.loottablemodifier.modded.impl.resource.action;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.monkeylib538.modded.api.resource.ResourceKeyApi;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.id;

public final class LootModifierActionTypeRegistryImpl implements LootModifierActionType.Registry {
    public static final Registry<LootModifierActionType> REGISTRY = new MappedRegistry<>(
            ResourceKeyApi.createRegistry(id("loot_modifier_action_types")),
            Lifecycle.stable()
    );

    @Override
    public LootModifierActionType register(Identifier id, LootModifierActionType type) {
        return Registry.register(REGISTRY, ResourceKeyApi.create(REGISTRY.key(), id), type);
    }
}
