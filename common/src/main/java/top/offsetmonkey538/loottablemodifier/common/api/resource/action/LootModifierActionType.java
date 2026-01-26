package top.offsetmonkey538.loottablemodifier.common.api.resource.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.util.function.Supplier;

import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.load;

/**
 * The type of a {@link LootModifierAction}, holds the codec.
 *
 * @param codec the codec for this action.
 */
public record LootModifierActionType(MapCodec<? extends LootModifierAction> codec) {
    /**
     * Provides codec for {@link LootModifierActionType}
     */
    public static final Supplier<Codec<LootModifierAction>> CODEC_PROVIDER = load(CodecProvider.class);

    /**
     * Register method
     *
     * @param id the identifier to register as
     * @param codec the codec for the loot modifier action type
     * @return a loot modifier action type for the provided codec
     */
    public static LootModifierActionType register(final Identifier id, final MapCodec<? extends LootModifierAction> codec) {
        return Registry.INSTANCE.register(id, new LootModifierActionType(codec));
    }

    @Internal
    public interface Registry {
        Registry INSTANCE = load(Registry.class);

        LootModifierActionType register(final Identifier id, final LootModifierActionType type);
    }

    @Internal
    public interface CodecProvider extends Supplier<Codec<LootModifierAction>> {

    }
}
