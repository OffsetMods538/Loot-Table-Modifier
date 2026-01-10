package top.offsetmonkey538.loottablemodifier.modded.v1212.impl.resource.action;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.common.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.modded.impl.resource.action.LootModifierActionTypeRegistryImpl;

public final class LootModifierActionTypeCodecProviderImpl implements LootModifierActionType.CodecProvider {
    private static final Codec<LootModifierAction> CODEC = LootModifierActionTypeRegistryImpl.REGISTRY.byNameCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

    @Override
    public Codec<LootModifierAction> get() {
        return CODEC;
    }
}
