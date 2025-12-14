package top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;

public record IdentifierWrapper(net.minecraft.util.Identifier vanillaIdentifier) implements Identifier {

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
            return net.minecraft.util.Identifier.CODEC.xmap(IdentifierWrapper::new, wrappedIdentifier -> ((IdentifierWrapper) wrappedIdentifier).vanillaIdentifier());
        }
    }

    public static final class InstantiatorImpl implements Instantiator {
        @Override
        public Identifier apply(String s) {
            return new IdentifierWrapper(net.minecraft.util.Identifier.of(s));
        }
    }
}

