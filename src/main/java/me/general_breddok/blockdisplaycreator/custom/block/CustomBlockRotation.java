package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Shulker;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface CustomBlockRotation {
    BlockFace getAttachedFace();

    void setAttachedFace(BlockFace blockFace);

    float getDirection();

    void setDirection(float direction);

    void adjustDisplayRotation(@NotNull Display display, int sidesCount);
    void adjustInteractionRotation(@NotNull Interaction interaction, Vector offset, int sidesCount);
    void adjustCollisionRotation(@NotNull Shulker collision, Vector offset, int sidesCount);
    //void adjustBlockRotation(@NotNull Block block, Vector offset, int sidesCount);
}
