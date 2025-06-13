package top.offsetmonkey538.loottablemodifier.resource;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record OptionalPattern(boolean isRegex, @NotNull String patternString, @NotNull Pattern pattern) {
    private static final Codec<OptionalPattern> INLINE_CODEC = Codec.STRING.xmap(OptionalPattern::new, OptionalPattern::patternString);
    private static final Codec<OptionalPattern> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("regex", false).forGetter(OptionalPattern::isRegex),
            Codec.STRING.fieldOf("pattern").forGetter(OptionalPattern::patternString)
    ).apply(instance, OptionalPattern::new));
    public static final Codec<OptionalPattern> CODEC = Codec.either(
            INLINE_CODEC,
            FULL_CODEC
    ).xmap(
            either -> either.map(it -> it, it -> it),
            pattern -> pattern.isRegex ? Either.right(pattern) : Either.left(pattern)
    );

    private OptionalPattern(final String stringPattern) {
        this(
                false,
                stringPattern,
                Pattern.compile(Pattern.quote(stringPattern))
        );
    }

    private OptionalPattern(final boolean isRegex, final String stringPattern) {
        this(
                isRegex,
                stringPattern,
                Pattern.compile(isRegex ? stringPattern : Pattern.quote(stringPattern))
        );
    }

    public static OptionalPattern compile(String patternString) {
        return new OptionalPattern(
                true,
                patternString
        );
    }

    public static OptionalPattern literal(String patternString) {
        return new OptionalPattern(
                patternString
        );
    }

    /**
     * Not deprecated for removal.
     * <p>
     * Use this only if you really need an instance of the {@link Matcher}. Otherwise, use {@link #matches(CharSequence)}, because it removes the overhead the {@link Matcher} when {@link #isRegex} is false.
     * @see #matches(CharSequence)
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated()
    public Matcher matcher(CharSequence input) {
        return pattern.matcher(input);
    }

    public boolean matches(CharSequence input) {
        if (isRegex) return matcher(input).matches();
        return patternString.contentEquals(input);
    }
}
