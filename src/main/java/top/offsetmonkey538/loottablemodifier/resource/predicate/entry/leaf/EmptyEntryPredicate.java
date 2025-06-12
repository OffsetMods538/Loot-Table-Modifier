package top.offsetmonkey538.loottablemodifier.resource.predicate.entry.leaf;

//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.loot.LootTable;
//import net.minecraft.loot.entry.ItemEntry;
//import net.minecraft.util.Identifier;
//import org.jetbrains.annotations.NotNull;
//import top.offsetmonkey538.loottablemodifier.api.LootModifierPredicateTypes;
//import top.offsetmonkey538.loottablemodifier.resource.LootModifierContext;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.LootModifierPredicateType;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.condition.LootConditionPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.function.LootFunctionPredicate;
//
//import java.util.List;
//import java.util.Optional;
//
//public class EmptyEntryPredicate extends LeafEntryPredicate {
//    public static final MapCodec<EmptyEntryPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> addLeafFieldsToCodec(instance).apply(instance, EmptyEntryPredicate::new));
//
//    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // given by da codecc
//    protected EmptyEntryPredicate(@NotNull Optional<Integer> weight, @NotNull Optional<Integer> quality, @NotNull Optional<List<LootConditionPredicate>> conditions, @NotNull Optional<List<LootFunctionPredicate>> functions) {
//        super(weight, quality, conditions, functions);
//    }
//
//    @Override
//    public LootModifierPredicateType getType() {
//        return LootModifierPredicateTypes.EMPTY_ENTRY;
//    }
//
//    @Override
//    public boolean test(@NotNull LootModifierContext context, @NotNull LootTable table, @NotNull Identifier tableId) {
//        return super.test(context, table, tableId);
//    }   ItemEntry
//
//    @Override
//    public byte requiredContext() {
//        return LootModifierContext.REQUIRES_TABLE;
//    }
//}
//