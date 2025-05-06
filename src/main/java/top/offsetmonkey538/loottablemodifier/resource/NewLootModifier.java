package top.offsetmonkey538.loottablemodifier.resource;

import com.google.common.collect.ImmutableList;
import com.ibm.icu.impl.EmojiProps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.action.LootModifierAction;

import java.util.ArrayList;
import java.util.List;

public record NewLootModifier(List<Identifier> modifies, List<LootModifierAction> actions) {
    public static final Codec<NewLootModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(Identifier.CODEC, Identifier.CODEC.listOf()).fieldOf("modifies").forGetter(NewLootModifier::modifiesEither),
            Codec.either(LootModifierAction.CODEC, LootModifierAction.CODEC.listOf()).fieldOf("actions").forGetter(NewLootModifier::actionsEither)
            //LootModifierAction.CODEC.listOf().fieldOf("")
            //Codec.mapEither(LootPool.CODEC.listOf().fieldOf("pools"), LootPool.CODEC.listOf().fieldOf("loot_pools")).forGetter(NewLootModifier::poolsEither)
    ).apply(instance, NewLootModifier::new));

    private NewLootModifier(Either<Identifier, List<Identifier>> modifiesEither, Either<LootModifierAction, List<LootModifierAction>> actionsEither) {
        this(
                new ArrayList<>(modifiesEither.right().orElseGet(() -> List.of(modifiesEither.left().orElseThrow()))),
                new ArrayList<>(actionsEither.right().orElseGet(() -> List.of(actionsEither.left().orElseThrow())))
        );
    }

    private Either<Identifier, List<Identifier>> modifiesEither() {
        if (modifies.size() == 1) return Either.left(modifies.get(0));
        return Either.right(modifies);
    }

    private Either<LootModifierAction, List<LootModifierAction>> actionsEither() {
        if (actions.size() == 1) return Either.left(actions.get(0));
        return Either.right(actions);
    }

    // Don't think this is needed? public static NewLootModifier.Builder builder() {
    // Don't think this is needed?     return new NewLootModifier.Builder();
    // Don't think this is needed? }

    // Don't think this is needed? public static class Builder {
    // Don't think this is needed?     private final ImmutableList.Builder<Identifier> modifies = ImmutableList.builder();
    // Don't think this is needed?     private final ImmutableList.Builder<LootModifierAction> actions = ImmutableList.builder();
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier.Builder modifies(@NotNull Identifier... modifies) {
    // Don't think this is needed?         this.modifies.add(modifies);
    // Don't think this is needed?         return this;
    // Don't think this is needed?     }
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier.Builder conditionally(@NotNull LootModifierAction.Builder action) {
    // Don't think this is needed?         this.actions.add(action.build());
    // Don't think this is needed?         return this;
    // Don't think this is needed?     }
    // Don't think this is needed?
    // Don't think this is needed?     public NewLootModifier build() {
    // Don't think this is needed?         return new NewLootModifier(this.modifies.build(), this.actions.build());
    // Don't think this is needed?     }
    // Don't think this is needed? }
}
