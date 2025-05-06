package top.offsetmonkey538.loottablemodifier.resource.action;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;

public interface LootModifierAction {
    Codec<LootModifierAction> CODEC = LootModifierActionType.REGISTRY.getCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

    LootModifierActionType getType();

    @FunctionalInterface
    interface Builder {
        LootModifierAction build();
    }
}
