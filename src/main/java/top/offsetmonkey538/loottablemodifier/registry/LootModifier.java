package top.offsetmonkey538.loottablemodifier.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record LootModifier(Either<Identifier, List<Identifier>> modifiesEither, LootTable lootTable, @Nullable String name) {
    public static final Codec<LootModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(LootModifier::modifiesEither),
            LootTable.CODEC.fieldOf("loot_table").forGetter(LootModifier::lootTable)
    ).apply(instance, LootModifier::new));

    private LootModifier(Either<Identifier, List<Identifier>> modifiesEither, LootTable lootTable) {
        this(modifiesEither, lootTable, null);
    }

    public LootModifier(LootTable lootTable, List<Identifier> modifies) {
        this(
                modifies.size() == 1 ? Either.left(modifies.get(0)) : Either.right(modifies),
                lootTable
        );
    }

    public List<Identifier> modifies() {
        return modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow()));
    }

    public static LootModifier copyOfEntry(RegistryEntry.Reference<LootModifier> other) {
        return new LootModifier(
                other.value().modifiesEither(),
                other.value().lootTable(),
                other.getIdAsString()
        );
    }
}
