package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.AddEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.RemoveEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.SetItemAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.RemovePoolAction;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

/**
 * Contains all {@link LootModifierAction} types available in Loot Table Modifier.
 * <br />
 * Use their builders to create them.
 */
public final class LootModifierActionTypes {
    private LootModifierActionTypes() {

    }

    /**
     * Type of {@link AddPoolAction}
     */
    public static final LootModifierActionType POOL_ADD = register(id("pool_add"), AddPoolAction.CODEC);
    /**
     * Type of {@link RemovePoolAction}
     */
    public static final LootModifierActionType POOL_REMOVE = register(id("pool_remove"), RemovePoolAction.CODEC);

    /**
     * Type of {@link AddEntryAction}
     */
    public static final LootModifierActionType ENTRY_ADD = register(id("entry_add"), AddEntryAction.CODEC);
    /**
     * Type of {@link RemoveEntryAction}
     */
    public static final LootModifierActionType ENTRY_REMOVE = register(id("entry_remove"), RemoveEntryAction.CODEC);
    /**
     * Type of {@link SetItemAction}
     */
    public static final LootModifierActionType ENTRY_ITEM_SET = register(id("entry_item_set"), SetItemAction.CODEC);

    private static LootModifierActionType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierAction> codec) {
        return Registry.register(LootModifierActionType.REGISTRY, id, new LootModifierActionType(codec));
    }

    /**
     * Registers action types by loading the class.
     * <br />
     * Only for the loot table modifier initializer to call, NO TOUCHY >:(
     */
    @ApiStatus.Internal
    public static void register() {
        // Registers action types by loading the class
    }
}
