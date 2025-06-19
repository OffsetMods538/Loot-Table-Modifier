package top.offsetmonkey538.loottablemodifier.api.resource.predicate.function;

import com.mojang.serialization.Codec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

// TODO: maybe in the future? nou iea
//public record LootFunctionPredicate(@NotNull Pattern functionPattern) {
//    public static final Codec<LootFunctionPredicate> CODEC = Util.PATTERN_CODEC.xmap(LootFunctionPredicate::new, LootFunctionPredicate::functionPattern);
//
//    public boolean matches(final @NotNull LootFunction function) {
//        final Identifier functionId = Registries.LOOT_FUNCTION_TYPE.getId(function.getType());
//        if (functionId == null) return false;
//
//        return functionPattern.matcher(functionId.toString()).matches();
//    }
//}
