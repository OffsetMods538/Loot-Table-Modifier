package top.offsetmonkey538.loottablemodifier.modded.impl.resource.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicate;
import top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;

import static top.offsetmonkey538.loottablemodifier.modded.platform.FabricPlatformMain.id;

public final class LootModifierPredicateTypeImpl {
    private LootModifierPredicateTypeImpl() {

    }

    public static final class RegistryImpl implements LootModifierPredicateType.Registry {
        private static final Registry<LootModifierPredicateType> REGISTRY = new MappedRegistry<>(
                ResourceKey.createRegistryKey(id("loot_modifier_predicate_types")), Lifecycle.stable()
        );

        @Override
        public top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType register(@NotNull Identifier id, @NotNull top.offsetmonkey538.loottablemodifier.common.api.resource.predicate.LootModifierPredicateType type) {
            return Registry.register(REGISTRY, ((IdentifierWrapper) id).vanillaIdentifier(), type);
        }
    }

    public static final class CodecProviderImpl implements LootModifierPredicateType.CodecProvider {
        private static final Codec<LootModifierPredicate> CODEC = RegistryImpl.REGISTRY.byNameCodec().dispatch(LootModifierPredicate::getType, LootModifierPredicateType::codec);

        @Override
        public Codec<LootModifierPredicate> get() {
            return CODEC;
        }
    }
}
