package top.offsetmonkey538.loottablemodifier.resource.predicate;

import com.mojang.serialization.Codec;

import java.util.regex.Pattern;

public final class Util {
    private Util() {

    }

    public static final Codec<Pattern> PATTERN_CODEC = Codec.STRING.xmap(Pattern::compile, Pattern::pattern);
}
