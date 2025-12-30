package top.offsetmonkey538.loottablemodifier.modded.v1205.impl.wrapper;

import net.minecraft.resources.ResourceLocation;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.IdentifierWrapper;

public final class IdentifierInstantiator implements Identifier.Instantiator {
    @Override
    public Identifier apply(String s) {
        return new IdentifierWrapper(new ResourceLocation(s));
    }
}
