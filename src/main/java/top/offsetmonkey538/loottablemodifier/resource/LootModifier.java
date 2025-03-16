package top.offsetmonkey538.loottablemodifier.resource;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record LootModifier(ArrayList<Identifier> modifies, List<LootPool> pools) {
    public static final Codec<LootModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(LootModifier::modifiesEither),
            Codec.mapEither(LootPool.CODEC.listOf().fieldOf("pools"), LootPool.CODEC.listOf().fieldOf("loot_pools")).forGetter(LootModifier::poolsEither)
    ).apply(instance, LootModifier::new));

    private LootModifier(Either<Identifier, List<Identifier>> modifiesEither, Either<List<LootPool>, List<LootPool>> poolsEither) {
        this(
                new ArrayList<>(modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow()))),
                new ArrayList<>(poolsEither.left().orElseGet(() -> poolsEither.right().orElseThrow()))
        );
    }

    private Either<Identifier, List<Identifier>> modifiesEither() {
        if (modifies.size() == 1) return Either.left(modifies.get(0));
        return Either.right(modifies);
    }

    // Left is "pools", right is "loot_pools". Want datagen to use "pools" so left it is.
    private Either<List<LootPool>, List<LootPool>> poolsEither() {
        return Either.left(pools);
    }
}
