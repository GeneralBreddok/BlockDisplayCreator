package me.general_breddok.blockdisplaycreator.entity.living;

import org.bukkit.DyeColor;
import org.bukkit.block.BlockFace;

public interface ShulkerCharacteristics extends LivingEntityCharacteristics {
    Float getPeek();

    void setPeek(Float value);

    BlockFace getAttachedFace();

    void setAttachedFace(BlockFace face);

    DyeColor getColor();

    void setColor(DyeColor color);
}
