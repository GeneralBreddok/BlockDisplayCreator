package me.general_breddok.blockdisplaycreator.entity.display.block;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.block.data.BlockData;

/**
 * Defines characteristics specific to block display entities.
 * <p>
 * Extends {@link DisplayCharacteristics} to include general display entity properties
 * and adds a single property for the block data being displayed.
 * </p>
 */
public interface BlockDisplayCharacteristics extends DisplayCharacteristics {

    /**
     * Gets the {@link BlockData} representing the block displayed by this entity.
     *
     * @return the current block data
     */
    BlockData getBlock();

    /**
     * Sets the {@link BlockData} to be displayed by this entity.
     *
     * @param block the block data to set
     */
    void setBlock(BlockData block);
}
