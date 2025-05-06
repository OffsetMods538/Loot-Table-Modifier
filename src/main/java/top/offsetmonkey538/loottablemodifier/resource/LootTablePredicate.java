package top.offsetmonkey538.loottablemodifier.resource;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

public record LootTablePredicate(@NotNull Pattern identifier, @Nullable Pattern type) {
    private static final Codec<Pattern> PATTERN_CODEC = Codec.STRING.xmap(Pattern::compile, Pattern::pattern);
    private static final Codec<LootTablePredicate> INLINE_CODEC = PATTERN_CODEC.xmap(LootTablePredicate::new, LootTablePredicate::identifier);
    private static final Codec<LootTablePredicate> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PATTERN_CODEC.fieldOf("identifier").forGetter(LootTablePredicate::identifier),
            PATTERN_CODEC.optionalFieldOf("type").forGetter(LootTablePredicate::optionalType)
    ).apply(instance, LootTablePredicate::new));
    public static final Codec<LootTablePredicate> CODEC = Codec.either(LootTablePredicate.INLINE_CODEC, LootTablePredicate.FULL_CODEC).xmap(lootTablePredicateLootTablePredicateEither -> lootTablePredicateLootTablePredicateEither.left().orElseGet(() -> lootTablePredicateLootTablePredicateEither.right().orElseThrow()),
            lootTablePredicate -> {
        if (lootTablePredicate.type == null) {
            return Either.left(lootTablePredicate);
        }
        return Either.right(lootTablePredicate);
    });

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Codec gib it to me
    private LootTablePredicate(@NotNull Pattern pattern, @NotNull Optional<Pattern> optionalType) {
        this(pattern, optionalType.orElse(null));
    }

    public LootTablePredicate(@NotNull Pattern pattern) {
        this(pattern, (Pattern) null);
    }

    private Optional<Pattern> optionalType() {
        return Optional.ofNullable(type);
    }


    public boolean matches(final @NotNull LootTable table, final @NotNull Identifier tableId) {
        boolean result = identifier.matcher(tableId.toString()).matches();

        if (type != null) {
            result = result && type.matcher(LootContextTypes.MAP.inverse().get(table.getType()).toString()).matches();
        }

        return result;
    }
}
