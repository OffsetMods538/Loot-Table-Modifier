package top.offsetmonkey538.loottablemodifier.api.resource.predicate.entry.leaf;

//import com.mojang.datafixers.Products;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.loot.entry.ItemEntry;
//import net.minecraft.loot.entry.LootPoolEntry;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.condition.LootConditionPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.function.LootFunctionPredicate;
//import top.offsetmonkey538.loottablemodifier.resource.predicate.entry.LootEntryPredicate;
//
//import java.util.List;
//import java.util.Optional;
//
//public abstract class LeafEntryPredicate extends LootEntryPredicate {
//    protected final @Nullable Integer weight;
//    protected final @Nullable Integer quality;
//    protected final @Nullable List<LootFunctionPredicate> functions;
//
//    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // codec giv it
//    protected LeafEntryPredicate(@NotNull Optional<Integer> weight, @NotNull Optional<Integer> quality, @NotNull Optional<List<LootConditionPredicate>> conditions, @NotNull Optional<List<LootFunctionPredicate>> functions) {
//        super(conditions);
//        this.weight = weight.orElse(null);
//        this.quality = quality.orElse(null);
//        this.functions = functions.orElse(null);
//    }
//
//    protected static <T extends LeafEntryPredicate> Products.P4<RecordCodecBuilder.Mu<T>, Optional<Integer>, Optional<Integer>, Optional<List<LootConditionPredicate>>, Optional<List<LootFunctionPredicate>>> addLeafFieldsToCodec(RecordCodecBuilder.Instance<T> instance) {
//        return instance.group(
//                Codec.INT.optionalFieldOf("weight").forGetter(entry -> Optional.ofNullable(entry.weight)),
//                Codec.INT.optionalFieldOf("quality").forGetter(entry -> Optional.ofNullable(entry.weight))
//                )
//                .and(addConditionsToCodec(instance).t1())
//                .and(LootFunctionPredicate.CODEC.listOf().optionalFieldOf("functions").forGetter(entry -> Optional.ofNullable(entry.functions)));
//
//    }
//ItemEntry
//    // todo: @Override
//    // todo: public boolean matches(@NotNull LootPoolEntry entry) {
//    // todo:     return super.matches(entry);
//    // todo: }
//}
//
