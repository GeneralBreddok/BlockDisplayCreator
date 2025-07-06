package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface CustomBlock extends AbstractCustomBlock {

    Location getLocation();

    void setLocation(@NotNull Location location, CustomBlockOption... options);

    List<Display> getDisplays();

    void setDisplays(@NotNull List<Display> displays, CustomBlockOption... options);

    List<Interaction> getInteractions();

    void setInteractions(@NotNull List<Interaction> interactions, CustomBlockOption... options);

    List<Shulker> getCollisions();

    void setCollisions(@NotNull List<Shulker> collisions, CustomBlockOption... options);

    CustomBlockRotation getRotation();

    void setRotation(@NotNull CustomBlockRotation rotation, CustomBlockOption... options);

    UUID getUuid();
}
