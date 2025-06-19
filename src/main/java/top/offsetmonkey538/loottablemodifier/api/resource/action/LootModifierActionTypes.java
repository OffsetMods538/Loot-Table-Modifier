package top.offsetmonkey538.loottablemodifier.api.resource.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.entry.SetItemAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.pool.RemovePoolAction;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public final class LootModifierActionTypes {
    private LootModifierActionTypes() {

    }

    public static final LootModifierActionType ADD_POOL = register(id("add_pool"), AddPoolAction.CODEC);
    public static final LootModifierActionType REMOVE_POOL = register(id("remove_pool"), RemovePoolAction.CODEC);

    public static final LootModifierActionType SET_ITEM = register(id("set_item"), SetItemAction.CODEC);

    private static LootModifierActionType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierAction> codec) {
        return Registry.register(LootModifierActionType.REGISTRY, id, new LootModifierActionType(codec));
    }

    public static void register() {
        // Registers action types by loading the class
    }
}
