package top.offsetmonkey538.loottablemodifier.api.resource.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record OptionalIdentifierPattern(boolean isRegex, @NotNull String patternString, @NotNull Pattern pattern) {
    private static final Codec<OptionalIdentifierPattern> INLINE_CODEC = Identifier.CODEC.xmap(OptionalIdentifierPattern::literal, instance -> Identifier.of(instance.patternString()));
    private static final Codec<OptionalIdentifierPattern> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("regexPattern").forGetter(OptionalIdentifierPattern::patternString)
    ).apply(instance, OptionalIdentifierPattern::compile));
    public static final Codec<OptionalIdentifierPattern> CODEC = Codec.either(
            INLINE_CODEC,
            FULL_CODEC
    ).xmap(
            either -> either.map(it -> it, it -> it),
            pattern -> pattern.isRegex ? Either.right(pattern) : Either.left(pattern)
    );

    public static OptionalIdentifierPattern literal(final Identifier identifier) {
        return new OptionalIdentifierPattern(
                false,
                identifier.toString(),
                Pattern.compile(Pattern.quote(identifier.toString()))
        );
    }
    //public static OptionalIdentifierPattern literal(final String literalString) {
    //
    //}

    public static OptionalIdentifierPattern compile(final String regexPattern) {
        return new OptionalIdentifierPattern(
                true,
                regexPattern,
                Pattern.compile(regexPattern)
        );
    }

    /**
     * Not deprecated for removal.
     * <p>
     * Use this only if you really need an instance of the {@link Matcher}. Otherwise, use {@link #matches(CharSequence)}, because it removes the overhead of the {@link Matcher} when {@link #isRegex} is false.
     * @see #matches(CharSequence)
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated(forRemoval = false)
    public Matcher matcher(CharSequence input) {
        return pattern.matcher(input);
    }

    public boolean matches(CharSequence input) {
        if (isRegex) return matcher(input).matches();
        return patternString.contentEquals(input);
    }
}
