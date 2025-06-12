package top.offsetmonkey538.loottablemodifier.resource.predicate.pool;

//import com.mojang.datafixers.util.Either;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.loot.LootPool;
//import net.minecraft.loot.LootTable;
//import net.minecraft.loot.condition.AllOfLootCondition;
//import net.minecraft.loot.condition.AnyOfLootCondition;
//import net.minecraft.loot.condition.InvertedLootCondition;
//import net.minecraft.loot.context.LootContextTypes;
//import net.minecraft.util.Identifier;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import top.offsetmonkey538.loottablemodifier.api.LootModifierPredicateTypes;
//import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.condition.LootConditionPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.function.LootFunctionPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.entry.LootEntryPredicate;
//
//import java.util.List;
//import java.util.Optional;
//
//public record LootPoolPredicate(@Nullable Integer rolls, @Nullable Integer bonusRolls, @Nullable List<LootEntryPredicate> entries, @Nullable List<LootFunctionPredicate> functions, @Nullable List<LootConditionPredicate> conditions) implements LootModifierPredicate {
//    public static final MapCodec<LootPoolPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
//            // TODO: smt complex that allows like matching integer expressions? Yknow like >=2 // TODO: ok actually Minecraft uses some number provider stuff that I need to match. Not always just a single int
//            Codec.INT.optionalFieldOf("rolls").forGetter(LootPoolPredicate::optionalRolls),
//            Codec.INT.optionalFieldOf("bonusRolls").forGetter(LootPoolPredicate::optionalBonusRolls),
//            Codec.either(LootEntryPredicate.CODEC, LootEntryPredicate.CODEC.listOf()).optionalFieldOf("entries").forGetter(LootPoolPredicate::optionalEntries),
//            Codec.either(LootFunctionPredicate.CODEC, LootFunctionPredicate.CODEC.listOf()).optionalFieldOf("functions").forGetter(LootPoolPredicate::optionalFunctions),
//            Codec.either(LootConditionPredicate.CODEC, LootConditionPredicate.CODEC.listOf()).optionalFieldOf("conditions").forGetter(LootPoolPredicate::optionalConditions)
//    ).apply(instance, LootPoolPredicate::new));
//
//    private Optional<Integer> optionalRolls() {
//        return Optional.ofNullable(rolls);
//    }
//
//    private Optional<Integer> optionalBonusRolls() {
//        return Optional.ofNullable(bonusRolls);
//    }
//
//    private Optional<Either<LootEntryPredicate, List<LootEntryPredicate>>> optionalEntries() {
//        if (entries == null) return Optional.empty();
//        if (entries.size() == 1) return Optional.of(Either.left(entries.get(0)));
//        return Optional.of(Either.right(entries));
//    }
//
//    private Optional<Either<LootFunctionPredicate, List<LootFunctionPredicate>>> optionalFunctions() {
//        if (functions == null) return Optional.empty();
//        if (functions.size() == 1) return Optional.of(Either.left(functions.get(0)));
//        return Optional.of(Either.right(functions));
//    }
//
//    private Optional<Either<LootConditionPredicate, List<LootConditionPredicate>>> optionalConditions() {
//        if (conditions == null) return Optional.empty();
//        if (conditions.size() == 1) return Optional.of(Either.left(conditions.get(0)));
//        return Optional.of(Either.right(conditions));
//    }
//
//    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // 'cause of codecccc
//    private LootPoolPredicate(
//            @NotNull Optional<Integer> optionalRolls,
//            @NotNull Optional<Integer> optionalBonusRolls,
//            @NotNull Optional<Either<LootEntryPredicate, List<LootEntryPredicate>>> optionalEntries,
//            @NotNull Optional<Either<LootFunctionPredicate, List<LootFunctionPredicate>>> optionalFunctions,
//            @NotNull Optional<Either<LootConditionPredicate, List<LootConditionPredicate>>> optionalConditions
//    ) {
//        this(
//                optionalRolls.orElse(null),
//                optionalBonusRolls.orElse(null),
//                optionalEntries.map(entriesEither -> entriesEither.map(List::of, entries -> entries)).orElse(null),
//                optionalFunctions.map(functionsEither -> functionsEither.map(List::of, functions -> functions)).orElse(null),
//                optionalConditions.map(conditionsEither -> conditionsEither.map(List::of, conditions -> conditions)).orElse(null)
//        );
//    }
//
//
//    //public boolean matches(final @NotNull LootTable table, final @NotNull Identifier tableId) {
//    @Override
//    public boolean test(final @NotNull LootModifierContext context, final @NotNull LootTable table, final @NotNull Identifier tableId, final @NotNull LootPool pool) {
//        boolean result = true;
//
//        // todo: if (rolls != null) {
//        // todo:     result = rolls == pool.rolls.;
//        // todo: }
//
//        // todo: if (bonusRolls != null) {
//        // todo:     result = result && bonusRolls == pool.rolls.;
//        // todo: }
//
//        if (entries != null) {
//            for (LootEntryPredicate entryPredicate : entries) {
//                result = result && entryPredicate.test(context);
//            }
//            result = result && type.matcher(LootContextTypes.MAP.inverse().get(table.getType()).toString()).matches();
//        }
//
//        if (functions != null) {
//            for (LootFunctionPredicate functionPredicate : functions) {
//                result = result && table.functions.stream().anyMatch(functionPredicate::matches);
//            }
//        }
//
//        return result;
//    }
//
//    @Override
//    public LootModifierPredicateType getType() {
//        return LootModifierPredicateTypes.LOOT_POOL;
//    }
//
//    @Override
//    public byte requiredContext() {
//        return LootModifierContext.REQUIRES_ENTRY;
//    }
//
//    //@FunctionalInterface
//    //public interface Builder {
//    //    LootPoolPredicate build();
//
//    //    default LootPoolPredicate.Builder invert() {
//    //        return InvertedLootCondition.builder(this);
//    //    }
//
//    //    default AnyOfLootCondition.Builder or(LootPoolPredicate.Builder otherPredicate) {
//    //        return AnyOfLootCondition.builder(this, otherPredicate);
//    //    }
//
//    //    default AllOfLootCondition.Builder and(LootPoolPredicate.Builder otherPredicate) {
//    //        return AllOfLootCondition.builder(this, otherPredicate);
//    //    }
//    //}
//}
//