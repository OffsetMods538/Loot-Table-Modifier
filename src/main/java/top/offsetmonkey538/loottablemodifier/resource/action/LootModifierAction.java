package top.offsetmonkey538.loottablemodifier.resource.action;

import com.mojang.serialization.Codec;
import net.minecraft.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.LootModifierActionTypes;

public interface LootModifierAction {
    Codec<LootModifierAction> CODEC = LootModifierActionType.REGISTRY.getCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

    LootModifierActionType getType();

    /**
     * Applies this action to the provided table
     * @param table the table to apply to
     * @return true when table was modified, false otherwise
     */
    boolean apply(final @NotNull LootTable table);

    @FunctionalInterface
    interface Builder {
        LootModifierAction build();
    }
}
