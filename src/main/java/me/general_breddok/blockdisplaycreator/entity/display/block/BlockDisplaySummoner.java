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


/**
 * Summoner implementation for {@link BlockDisplay} entities.
 * <p>
 * Extends {@link DisplaySummoner} to handle general display entity properties
 * and adds support for {@link BlockData} specific to block displays.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockDisplaySummoner extends DisplaySummoner<BlockDisplay> implements BlockDisplayCharacteristics {

    /**
     * The block data displayed by this entity.
     */
    BlockData block;

    /**
     * Default constructor initializing the summoner for {@link BlockDisplay} entities.
     */
    public BlockDisplaySummoner() {
        super(BlockDisplay.class);
    }

    /**
     * Constructor initializing the summoner based on existing characteristics.
     *
     * @param characteristics the characteristics to copy
     */
    public BlockDisplaySummoner(BlockDisplayCharacteristics characteristics) {
        super(BlockDisplay.class, characteristics);
        this.block = characteristics.getBlock();
    }

    /**
     * Summons a {@link BlockDisplay} at the specified location.
     * <p>
     * Applies the block data along with all inherited display characteristics.
     * </p>
     *
     * @param location the location to summon the entity
     * @return the summoned {@link BlockDisplay} entity
     */
    @Override
    public BlockDisplay summon(@NotNull Location location) {
        BlockDisplay blockDisplay = super.summon(location);
        OperationUtil.doIfNotNull(block, blockDisplay::setBlock);
        return blockDisplay;
    }

    /**
     * Creates a deep clone of this summoner, including block data and inherited properties.
     *
     * @return a cloned instance of this summoner
     */
    @Override
    public BlockDisplaySummoner clone() {
        BlockDisplaySummoner cloned = (BlockDisplaySummoner) super.clone();
        cloned.block = this.block;
        return cloned;
    }
}

