package me.general_breddok.blockdisplaycreator.serialize;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

public class SerialLocation extends Location implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerialLocation(@Nullable World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public SerialLocation(@Nullable World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }
}
