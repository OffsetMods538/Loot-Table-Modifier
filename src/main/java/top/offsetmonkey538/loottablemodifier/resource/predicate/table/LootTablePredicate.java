package top.offsetmonkey538.loottablemodifier.resource.predicate.table;

//import com.mojang.datafixers.util.Either;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.loot.LootPool;
//import net.minecraft.loot.LootTable;
//import net.minecraft.loot.context.LootContextTypes;
//import net.minecraft.util.Identifier;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.Util;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.function.LootFunctionPredicate;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//// TODO: add entries predicate too
//public record LootTablePredicate(@Nullable Pattern identifier, @Nullable Pattern type, @Nullable List<LootFunctionPredicate> functions /* TODO: no need to use list cause anyOf and allOf are a thing */) implements LootModifierPredicate {
//    private static final Codec<LootTablePredicate> INLINE_CODEC = Util.PATTERN_CODEC.xmap(LootTablePredicate::new, LootTablePredicate::identifier);
//    private static final Codec<LootTablePredicate> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            Util.PATTERN_CODEC.optionalFieldOf("id").forGetter(LootTablePredicate::optionalIdentifier),
//            Util.PATTERN_CODEC.optionalFieldOf("type").forGetter(LootTablePredicate::optionalType),
//            Codec.either(LootFunctionPredicate.CODEC, LootFunctionPredicate.CODEC.listOf()).optionalFieldOf("functions").forGetter(LootTablePredicate::optionalFunctions)
//    ).apply(instance, LootTablePredicate::new));
//    public static final Codec<LootTablePredicate> CODEC = Codec.either(LootTablePredicate.INLINE_CODEC, LootTablePredicate.FULL_CODEC).xmap(lootTablePredicateLootTablePredicateEither -> lootTablePredicateLootTablePredicateEither.left().orElseGet(() -> lootTablePredicateLootTablePredicateEither.right().orElseThrow()),
//            lootTablePredicate -> {
//        if (lootTablePredicate.identifier != null && lootTablePredicate.type == null && (lootTablePredicate.functions == null || lootTablePredicate.functions.isEmpty())) {
//            return Either.left(lootTablePredicate);
//        }
//        return Either.right(lootTablePredicate);
//    });
//
//    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Codec gib it to me
//    private LootTablePredicate(@NotNull Optional<Pattern> optionalIdentifier, @NotNull Optional<Pattern> optionalType, @NotNull Optional<Either<LootFunctionPredicate, List<LootFunctionPredicate>>> optionalFunctions) {
//        this(optionalIdentifier.orElse(null), optionalType.orElse(null), optionalFunctions.map(functionsEither -> functionsEither.map(List::of, patterns -> patterns)).orElse(null));
//    }
//
//    public LootTablePredicate(@NotNull Pattern pattern) {
//        this(pattern, null, null);
//    }
//
//    private Optional<Pattern> optionalIdentifier() {
//        return Optional.ofNullable(identifier);
//    }
//    private Optional<Pattern> optionalType() {
//        return Optional.ofNullable(type);
//    }
//    private Optional<Either<LootFunctionPredicate, List<LootFunctionPredicate>>> optionalFunctions() {
//        if (functions == null) return Optional.empty();
//        if (functions.size() == 1) return Optional.of(Either.left(functions.get(0)));
//        return Optional.of(Either.right(functions));
//    }
//
//    // TODO: also loops over pools and checks that
//    @Override
//    public boolean test(final @NotNull LootTable table, final @NotNull Identifier tableId) {
//        boolean result = true;
//
//        if (identifier != null) {
//            result = identifier.matcher(tableId.toString()).matches();
//        }
//
//        if (type != null) {
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
//}
//