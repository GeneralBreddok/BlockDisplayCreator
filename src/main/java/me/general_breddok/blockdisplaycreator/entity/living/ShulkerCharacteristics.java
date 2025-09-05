package me.general_breddok.blockdisplaycreator.entity.living;

import org.bukkit.DyeColor;
import org.bukkit.block.BlockFace;

/**
 * Extends {@link LivingEntityCharacteristics} to provide attributes specific to Shulker entities.
 * <p>
 * Includes properties such as peek progress, attachment face, and shulker color.
 * </p>
 */
public interface ShulkerCharacteristics extends LivingEntityCharacteristics {

    /**
     * Gets the current peek value of the Shulker (how open it is).
     *
     * @return peek value as a float
     */
    Float getPeek();

    /**
     * Sets the peek value of the Shulker (how open it is).
     *
     * @param value peek value as a float
     */
    void setPeek(Float value);

    /**
     * Gets the block face to which the Shulker is attached.
     *
     * @return the attached block face
     */
    BlockFace getAttachedFace();

    /**
     * Sets the block face to which the Shulker is attached.
     *
     * @param face the block face to attach to
     */
    void setAttachedFace(BlockFace face);

    /**
     * Gets the color of the Shulker.
     *
     * @return the shulker's color
     */
    DyeColor getColor();

    /**
     * Sets the color of the Shulker.
     *
     * @param color the shulker's color
     */
    void setColor(DyeColor color);
}

