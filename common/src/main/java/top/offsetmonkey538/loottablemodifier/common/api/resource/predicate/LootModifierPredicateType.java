package top.offsetmonkey538.loottablemodifier.common.api.resource.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

/**
 * They type of a {@link LootModifierPredicate}, holds the codec.
 *
 * @param codec the codec for this predicate
 */
public record LootModifierPredicateType(@NotNull MapCodec<? extends LootModifierPredicate> codec) {
    /**
     * Provides codec for {@link LootModifierPredicate}
     */
    public static final Supplier<Codec<LootModifierPredicate>> CODEC_PROVIDER = load(CodecProvider.class);

    /**
     * Register method
     *
     * @param id the identifier to register as
     * @param codec the codec for the loot modifier predicate type
     * @return a loot modifier predicate type for the provided codec
     */
    public static LootModifierPredicateType register(final @NotNull Identifier id, final @NotNull MapCodec<? extends LootModifierPredicate> codec) {
        return Registry.INSTANCE.register(id, new LootModifierPredicateType(codec));
    }

    @ApiStatus.Internal
    public interface Registry {
        Registry INSTANCE = load(Registry.class);

        LootModifierPredicateType register(final @NotNull Identifier id, final @NotNull LootModifierPredicateType type);
    }

    @ApiStatus.Internal
    public interface CodecProvider extends Supplier<Codec<LootModifierPredicate>> {

    }
}
