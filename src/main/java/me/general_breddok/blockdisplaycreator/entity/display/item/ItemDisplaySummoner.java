package me.general_breddok.blockdisplaycreator.entity.display.item;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.display.DisplaySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ItemDisplayCharacteristics} for summoning {@link ItemDisplay} entities.
 * <p>
 * Extends {@link DisplaySummoner} to inherit general display properties and adds
 * item-specific characteristics such as {@link #itemDisplayTransform} and {@link #itemStack}.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDisplaySummoner extends DisplaySummoner<ItemDisplay> implements ItemDisplayCharacteristics {

    /**
     * The transform to apply to the displayed item.
     */
    ItemDisplay.ItemDisplayTransform itemDisplayTransform;

    /**
     * The {@link ItemStack} to display.
     */
    ItemStack itemStack;

    /**
     * Default constructor for an empty ItemDisplaySummoner.
     */
    public ItemDisplaySummoner() {
        super(ItemDisplay.class);
    }

    /**
     * Creates a new ItemDisplaySummoner using the specified characteristics.
     *
     * @param characteristics the source characteristics to copy from
     */
    public ItemDisplaySummoner(ItemDisplayCharacteristics characteristics) {
        super(ItemDisplay.class, characteristics);
        this.itemDisplayTransform = characteristics.getItemDisplayTransform();
        this.itemStack = characteristics.getItemStack();
    }

    /**
     * Summons an {@link ItemDisplay} at the given location with all configured properties.
     *
     * @param location the location to spawn the ItemDisplay
     * @return the summoned ItemDisplay entity
     */
    @Override
    public ItemDisplay summon(@NotNull Location location) {
        ItemDisplay itemDisplay = super.summon(location);
        OperationUtil.doIfNotNull(itemDisplayTransform, itemDisplay::setItemDisplayTransform);
        OperationUtil.doIfNotNull(itemStack, itemDisplay::setItemStack);
        return itemDisplay;
    }

    /**
     * Creates a deep copy of this ItemDisplaySummoner.
     *
     * @return a cloned instance of ItemDisplaySummoner
     */
    @Override
    public ItemDisplaySummoner clone() {
        ItemDisplaySummoner cloned = (ItemDisplaySummoner) super.clone();
        cloned.itemDisplayTransform = this.itemDisplayTransform;
        cloned.itemStack = this.itemStack;
        return cloned;
    }
}

