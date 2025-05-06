package top.offsetmonkey538.loottablemodifier.api;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.action.AddPoolAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierActionType;

import static top.offsetmonkey538.loottablemodifier.LootTableModifier.id;

public final class LootModifierActionTypes {
    private LootModifierActionTypes() {

    }

    public static final LootModifierActionType ADD_ENTRY = register(id("add_pool"), AddPoolAction.CODEC);

    private static LootModifierActionType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierAction> codec) {
        return Registry.register(LootModifierActionType.REGISTRY, id, new LootModifierActionType(codec));
    }

    public static void register() {
        // Registers action types by loading the class
    }
}
