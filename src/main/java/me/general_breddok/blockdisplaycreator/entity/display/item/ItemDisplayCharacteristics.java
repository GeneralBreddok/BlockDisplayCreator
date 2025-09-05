package me.general_breddok.blockdisplaycreator.entity.display.item;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the characteristics specific to {@link ItemDisplay} entities.
 * <p>
 * Extends {@link DisplayCharacteristics} to include all general display properties,
 * and adds item-specific properties such as the item transform and the item stack to display.
 * </p>
 */
public interface ItemDisplayCharacteristics extends DisplayCharacteristics {

    /**
     * Gets the {@link ItemDisplay.ItemDisplayTransform} applied to this ItemDisplay.
     *
     * @return the current item display transform
     */
    ItemDisplay.ItemDisplayTransform getItemDisplayTransform();

    /**
     * Sets the {@link ItemDisplay.ItemDisplayTransform} to apply to this ItemDisplay.
     *
     * @param itemDisplayTransform the transform to set
     */
    void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform itemDisplayTransform);

    /**
     * Gets the {@link ItemStack} that this ItemDisplay is showing.
     *
     * @return the current ItemStack
     */
    ItemStack getItemStack();

    /**
     * Sets the {@link ItemStack} to display in this ItemDisplay.
     *
     * @param itemStack the ItemStack to set
     */
    void setItemStack(ItemStack itemStack);
}
