package top.offsetmonkey538.loottablemodifier.api.resource.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.loottablemodifier.impl.LootTableIdGetterImpl;

/**
 * Provides methods for getting the identifier of loot tables on different versions.
 * <br>
 * The implemented methods may compile fine but throw runtime exceptions on newer versions because there's reflection magic going on, but it should be fine as these methods should only be called during data generation afaik.
 */
@ApiStatus.NonExtendable
public interface LootTableIdGetter {
    /**
     * The instance
     */
    LootTableIdGetter INSTANCE = new LootTableIdGetterImpl();

    /**
     * Returns the identifier of an entity's loot table
     *
     * @param entityType the entity to get the loot table id for
     * @return the identifier of the provided entity's loot table
     */
    Identifier get(@NotNull EntityType<?> entityType);
    /**
     * Returns the identifier of a block's loot table
     *
     * @param block the block to get the loot table id for
     * @return the identifier of the provided block's loot table
     */
    Identifier get(@NotNull Block block);
}
