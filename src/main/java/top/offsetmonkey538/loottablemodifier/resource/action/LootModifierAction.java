package top.offsetmonkey538.loottablemodifier.resource.action;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;

public interface LootModifierAction {
    Codec<LootModifierAction> CODEC = LootModifierActionType.REGISTRY.getCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

    LootModifierActionType getType();

    /**
     * Applies this action to the provided table
     * @param context the context to apply to
     * @return true when table was modified, false otherwise
     */
    int apply(final @NotNull LootModifierContext context);

    @FunctionalInterface
    interface Builder {
        LootModifierAction build();
    }
}
