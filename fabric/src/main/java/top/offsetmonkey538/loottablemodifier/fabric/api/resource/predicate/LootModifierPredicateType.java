package top.offsetmonkey538.loottablemodifier.fabric.api.resource.predicate;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.fabric.LootTableModifier;

/**
 * They type of a {@link LootModifierPredicate}, holds the codec.
 *
 * @param codec the codec for this predicate
 */
public record LootModifierPredicateType(@NotNull MapCodec<? extends LootModifierPredicate> codec) {
    /**
     * Registry of {@link LootModifierPredicateType}s
     */
    public static final Registry<LootModifierPredicateType> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(LootTableModifier.id("loot_modifier_predicate_types")), Lifecycle.stable()
    );
}
