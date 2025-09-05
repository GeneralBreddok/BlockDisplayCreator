package me.general_breddok.blockdisplaycreator.serialize;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class SerialItemStack extends ItemStack implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerialItemStack() {
        super();
    }

    public SerialItemStack(@NotNull Material type) {
        super(type);
    }

    public SerialItemStack(@NotNull Material type, int amount) {
        super(type, amount);
    }

    public SerialItemStack(@NotNull ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }
}
