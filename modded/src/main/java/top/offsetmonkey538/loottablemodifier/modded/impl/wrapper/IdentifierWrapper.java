package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;

public record IdentifierWrapper(net.minecraft.resources.ResourceLocation vanillaIdentifier) implements Identifier {

    @Override
    public String asString() {
        return vanillaIdentifier.toString();
    }

    @Override
    public String getNamespace() {
        return vanillaIdentifier.getNamespace();
    }

    @Override
    public String getPath() {
        return vanillaIdentifier.getPath();
    }

    @Override
    public @NotNull String toString() {
        return vanillaIdentifier.toString();
    }

    public static final class CodecProviderImpl implements CodecProvider {
        @Override
        public Codec<Identifier> get() {
            return net.minecraft.resources.ResourceLocation.CODEC.xmap(IdentifierWrapper::new, wrappedIdentifier -> ((IdentifierWrapper) wrappedIdentifier).vanillaIdentifier());
        }
    }
}

