package me.general_breddok.blockdisplaycreator.entity.display.item;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public interface ItemDisplayCharacteristics extends DisplayCharacteristics {
    ItemDisplay.ItemDisplayTransform getItemDisplayTransform();

    void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform itemDisplayTransform);

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);
}
