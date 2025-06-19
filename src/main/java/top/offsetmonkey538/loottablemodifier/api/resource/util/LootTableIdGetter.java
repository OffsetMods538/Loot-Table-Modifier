package top.offsetmonkey538.loottablemodifier.api.resource.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.impl.LootTableIdGetterImpl;

public interface LootTableIdGetter {
    LootTableIdGetter INSTANCE = new LootTableIdGetterImpl();

    Identifier get(@NotNull EntityType<?> entityType);
    Identifier get(@NotNull Block block);
}
