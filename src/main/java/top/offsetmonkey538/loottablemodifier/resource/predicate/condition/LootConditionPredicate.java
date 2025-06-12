package top.offsetmonkey538.loottablemodifier.resource.predicate.condition;

import com.mojang.serialization.Codec;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.resource.predicate.Util;

import java.util.regex.Pattern;

public record LootConditionPredicate(@NotNull Pattern functionPattern) {
    public static final Codec<LootConditionPredicate> CODEC = Util.PATTERN_CODEC.xmap(LootConditionPredicate::new, LootConditionPredicate::functionPattern);

    public boolean matches(final @NotNull LootCondition condition) {
        final Identifier functionId = Registries.LOOT_CONDITION_TYPE.getId(condition.getType());
        if (functionId == null) return false;

        return functionPattern.matcher(functionId.toString()).matches();
    }
}
