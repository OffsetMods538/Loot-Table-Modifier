package top.offsetmonkey538.loottablemodifier.fabric.impl.resource.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierAction;
import top.offsetmonkey538.loottablemodifier.api.resource.action.LootModifierActionType;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.IdentifierWrapper;

import static top.offsetmonkey538.loottablemodifier.fabric.platform.FabricPlatformMain.id;

public final class LootModifierActionTypeImpl {
    private LootModifierActionTypeImpl() {

    }

    public static final class RegistryImpl implements LootModifierActionType.Registry {
        private static final Registry<LootModifierActionType> REGISTRY = new SimpleRegistry<>(
                RegistryKey.ofRegistry(id("loot_modifier_action_types")), Lifecycle.stable()
        );

        @Override
        public LootModifierActionType register(@NotNull Identifier id, @NotNull LootModifierActionType type) {
            return Registry.register(REGISTRY, ((IdentifierWrapper) id).vanillaIdentifier(), type);
        }
    }

    public static final class CodecProviderImpl implements LootModifierActionType.CodecProvider {
        private static final Codec<LootModifierAction> CODEC = RegistryImpl.REGISTRY.getCodec().dispatch(LootModifierAction::getType, LootModifierActionType::codec);

        @Override
        public Codec<LootModifierAction> get() {
            return CODEC;
        }
    }
}
