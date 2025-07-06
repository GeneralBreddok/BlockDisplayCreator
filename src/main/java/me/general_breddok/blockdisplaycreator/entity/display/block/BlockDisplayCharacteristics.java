package me.general_breddok.blockdisplaycreator.entity.display.block;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.block.data.BlockData;

public interface BlockDisplayCharacteristics extends DisplayCharacteristics {
    BlockData getBlock();

    void setBlock(BlockData block);
}
