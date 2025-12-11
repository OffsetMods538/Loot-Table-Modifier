package top.offsetmonkey538.loottablemodifier.fabric.api.resource.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * {@link RegexPattern}s allow either matching {@link Identifier}s directly or using regex to do so.
 * <br />
 * Use {@link #literal(String)} for creating literal patterns and {@link #compile(String)} for creating regex patterns.
 *
 * @param isRegex if this {@link RegexPattern} is using regex or not
 * @param patternString the pattern as a plain string
 * @param pattern the compiled pattern
 */
public record RegexPattern(boolean isRegex, @NotNull String patternString, @NotNull Pattern pattern) {
    private static final Codec<RegexPattern> INLINE_CODEC = Identifier.CODEC.xmap(identifier -> RegexPattern.literal(identifier.toString()), instance -> Identifier.of(instance.patternString()));
    private static final Codec<RegexPattern> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("regexPattern").forGetter(RegexPattern::patternString)
    ).apply(instance, RegexPattern::compile));
    public static final Codec<RegexPattern> CODEC = Codec.either(
            INLINE_CODEC,
            FULL_CODEC
    ).xmap(
            either -> either.map(it -> it, it -> it),
            pattern -> pattern.isRegex ? Either.right(pattern) : Either.left(pattern)
    );

    /**
     * Creates a literal {@link RegexPattern} from the provided string.
     *
     * @param literal the literal String to match
     * @return a new {@link RegexPattern} matching the provided string
     */
    @Contract("_->new")
    public static RegexPattern literal(final String literal) {
        return new RegexPattern(
                false,
                literal,
                Pattern.compile(Pattern.quote(literal))
        );
    }

    /**
     * Compiles a {@link RegexPattern} from the provided regex pattern.
     *
     * @param regexPattern the pattern to use
     * @return a new {@link RegexPattern} compiled from the provided regex pattern
     * @see Pattern#compile(String)
     */
    public static RegexPattern compile(final String regexPattern) {
        return new RegexPattern(
                true,
                regexPattern,
                Pattern.compile(regexPattern)
        );
    }

    /**
     * Checks if the provided character sequence matches this.
     *
     * @param input the character sequence to check
     * @return if the provided character sequence matches this
     */
    public boolean matches(CharSequence input) {
        if (isRegex) return pattern.matcher(input).matches();
        return patternString.contentEquals(input);
    }
}
