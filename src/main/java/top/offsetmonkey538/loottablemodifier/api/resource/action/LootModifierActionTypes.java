package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.AddEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.RemoveEntryAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.SetItemAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.RemovePoolAction;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public final class LootModifierActionTypes {
    private LootModifierActionTypes() {

    }

    public static final LootModifierActionType POOL_ADD = register(id("pool_add"), AddPoolAction.CODEC);
    public static final LootModifierActionType POOL_REMOVE = register(id("pool_remove"), RemovePoolAction.CODEC);

    public static final LootModifierActionType ENTRY_ADD = register(id("entry_add"), AddEntryAction.CODEC);
    public static final LootModifierActionType ENTRY_REMOVE = register(id("entry_remove"), RemoveEntryAction.CODEC);
    public static final LootModifierActionType ENTRY_ITEM_SET = register(id("entry_item_set"), SetItemAction.CODEC);

    private static LootModifierActionType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierAction> codec) {
        return Registry.register(LootModifierActionType.REGISTRY, id, new LootModifierActionType(codec));
    }

    public static void register() {
        // Registers action types by loading the class
    }
}
