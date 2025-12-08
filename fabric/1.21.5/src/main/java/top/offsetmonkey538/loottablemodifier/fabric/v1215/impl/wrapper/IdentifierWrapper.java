package top.offsetmonkey538.loottablemodifier.fabric.v1215.impl.wrapper;

import com.mojang.serialization.Codec;
import top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.Identifier;

public record IdentifierWrapper(net.minecraft.util.Identifier vanillaIdentifier) implements Identifier {

    @Override
    public String asString() {
        return vanillaIdentifier.toString();
    }

    public static final class CodecProviderImpl implements CodecProvider {
        @Override
        public Codec<Identifier> get() {
            return net.minecraft.util.Identifier.CODEC.xmap(IdentifierWrapper::new, wrappedIdentifier -> ((IdentifierWrapper) wrappedIdentifier).vanillaIdentifier());
        }
    }
}

