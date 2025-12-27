package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.util.LootModifierContext;

/**
 * A loot modifier action
 */
public interface LootModifierAction {
    /**
     * Codec containing the type id
     */
    Codec<LootModifierAction> CODEC = LootModifierActionType.CODEC_PROVIDER.get();

    /**
     * Bitmask specifying that the action modified nothing
     */
    int MODIFIED_NONE = 0b000;
    /**
     * Bitmask specifying that the action modified a table
     */
    int MODIFIED_TABLE = 0b001;
    /**
     * Bitmask specifying that the action modified a pool
     */
    int MODIFIED_POOL = 0b011;
    /**
     * Bitmask specifying that the action modified an entry
     */
    int MODIFIED_ENTRY = 0b111;

    /**
     * Returns the type of this action.
     *
     * @return the {@link LootModifierActionType type} of this action.
     */
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

    /**
     * A builder for loot modifier actions.
     */
    @FunctionalInterface
    interface Builder {
        /**
         * Builds the action
         *
         * @return a built {@link LootModifierAction}
         */
        LootModifierAction build();
    }
}
