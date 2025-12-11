package top.offsetmonkey538.loottablemodifier.fabric.api.resource.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.condition.ConditionAddAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.entry.EntryAddAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.entry.EntryRemoveAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.pool.PoolAddAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.entry.EntryItemSetAction;
import top.offsetmonkey538.loottablemodifier.fabric.api.resource.action.pool.PoolRemoveAction;

import static top.offsetmonkey538.loottablemodifier.fabric.LootTableModifier.id;

/**
 * Contains all {@link LootModifierAction} types available in Loot Table Modifier.
 * <br />
 * Use their builders to create them.
 */
public final class LootModifierActionTypes {
    private LootModifierActionTypes() {

    }

    /**
     * Type of {@link PoolAddAction}
     */
    public static final LootModifierActionType POOL_ADD = register(id("pool_add"), PoolAddAction.CODEC);
    /**
     * Type of {@link PoolRemoveAction}
     */
    public static final LootModifierActionType POOL_REMOVE = register(id("pool_remove"), PoolRemoveAction.CODEC);

    /**
     * Type of {@link EntryAddAction}
     */
    public static final LootModifierActionType ENTRY_ADD = register(id("entry_add"), EntryAddAction.CODEC);
    /**
     * Type of {@link EntryRemoveAction}
     */
    public static final LootModifierActionType ENTRY_REMOVE = register(id("entry_remove"), EntryRemoveAction.CODEC);
    /**
     * Type of {@link EntryItemSetAction}
     */
    public static final LootModifierActionType ENTRY_ITEM_SET = register(id("entry_item_set"), EntryItemSetAction.CODEC);

    /**
     * Type of {@link ConditionAddAction}
     */
    public static final LootModifierActionType CONDITION_ADD = register(id("condition_add"), ConditionAddAction.CODEC);

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
