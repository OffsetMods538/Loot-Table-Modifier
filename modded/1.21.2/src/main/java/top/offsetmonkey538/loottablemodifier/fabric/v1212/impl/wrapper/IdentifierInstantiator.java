package top.offsetmonkey538.loottablemodifier.fabric.v1212.impl.wrapper;

import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.fabric.impl.wrapper.IdentifierWrapper;

public final class IdentifierInstantiator implements Identifier.Instantiator {
        @Override
        public Identifier apply(String s) {
            return new IdentifierWrapper(net.minecraft.resources.ResourceLocation.parse(s));
        }
    }