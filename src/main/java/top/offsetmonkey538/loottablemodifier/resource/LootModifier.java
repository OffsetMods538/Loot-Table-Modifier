package top.offsetmonkey538.loottablemodifier.resource;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record LootModifier(ArrayList<Identifier> modifies, List<LootPool> lootPools) {
    public static final Codec<LootModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(LootModifier::modifiesEither),
            LootPool.CODEC.listOf().fieldOf("loot_pools").forGetter(LootModifier::lootPools)
    ).apply(instance, LootModifier::new));

    private LootModifier(Either<Identifier, List<Identifier>> modifiesEither, List<LootPool> lootPools) {
        this(
                new ArrayList<>(modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow()))),
                lootPools
        );
    }

    private Either<Identifier, List<Identifier>> modifiesEither() {
        if (modifies.size() == 1) return Either.left(modifies.get(0));
        return Either.right(modifies);
    }
}
