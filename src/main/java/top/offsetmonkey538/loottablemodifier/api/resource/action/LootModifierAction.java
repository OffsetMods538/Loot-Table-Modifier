package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;

public interface LootModifierAction {
    Codec<LootModifierAction> CODEC = LootModifierActionType.REGISTRY.getCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

    int MODIFIED_NONE = 0b000;
    int MODIFIED_TABLE = 0b001;
    int MODIFIED_POOL = 0b011;
    int MODIFIED_ENTRY = 0b111;

    LootModifierActionType getType();

    /**
     * Applies this action to the provided context
     * @param context the context to apply to
     * @return the applied modification level
     * @see #MODIFIED_NONE
     * @see #MODIFIED_TABLE
     * @see #MODIFIED_POOL
     * @see #MODIFIED_ENTRY
     */
    int apply(final @NotNull LootModifierContext context);

    @FunctionalInterface
    interface Builder {
        LootModifierAction build();
    }
}
