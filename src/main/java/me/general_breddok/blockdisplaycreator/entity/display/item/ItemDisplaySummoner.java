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

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDisplaySummoner extends DisplaySummoner<ItemDisplay> implements ItemDisplayCharacteristics {
    ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    ItemStack itemStack;


    public ItemDisplaySummoner() {
        super(ItemDisplay.class);
    }

    public ItemDisplaySummoner(ItemDisplayCharacteristics characteristics) {
        super(ItemDisplay.class, characteristics);

        this.itemDisplayTransform = characteristics.getItemDisplayTransform();
        this.itemStack = characteristics.getItemStack();
    }

    @Override
    public ItemDisplay summon(@NotNull Location location) {
        ItemDisplay itemDisplay = super.summon(location);

        OperationUtil.doIfNotNull(itemDisplayTransform, itemDisplay::setItemDisplayTransform);
        OperationUtil.doIfNotNull(itemStack, itemDisplay::setItemStack);

        return itemDisplay;
    }

    @Override
    public ItemDisplaySummoner clone() {
        ItemDisplaySummoner cloned = (ItemDisplaySummoner) super.clone();

        cloned.itemDisplayTransform = this.itemDisplayTransform;
        cloned.itemStack = this.itemStack;

        return cloned;
    }
}
