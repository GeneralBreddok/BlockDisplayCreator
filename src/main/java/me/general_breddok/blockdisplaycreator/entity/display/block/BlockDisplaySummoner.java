package me.general_breddok.blockdisplaycreator.entity.display.block;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.display.DisplaySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;


@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockDisplaySummoner extends DisplaySummoner<BlockDisplay> implements BlockDisplayCharacteristics {
    BlockData block;

    public BlockDisplaySummoner() {
        super(BlockDisplay.class);
    }

    public BlockDisplaySummoner(BlockDisplayCharacteristics characteristics) {
        super(BlockDisplay.class, characteristics);

        this.block = characteristics.getBlock();
    }


    @Override
    public BlockDisplay summon(@NotNull Location location) {
        BlockDisplay blockDisplay = super.summon(location);

        OperationUtil.doIfNotNull(block, blockDisplay::setBlock);

        return blockDisplay;
    }

    @Override
    public BlockDisplaySummoner clone() {
        BlockDisplaySummoner cloned = (BlockDisplaySummoner) super.clone();

        cloned.block = this.block;

        return cloned;
    }
}
