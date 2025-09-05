package me.general_breddok.blockdisplaycreator.util;

import com.github.retrooper.packetevents.util.Vector3i;
import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.annotation.EmptyCollection;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class LocationUtil {
    public double getCoordinateAxis(@NotNull Vector vector, Axis axis) {
        return switch (axis) {
            case X -> vector.getX();
            case Y -> vector.getY();
            case Z -> vector.getZ();
        };
    }

    public double getCoordinateAxis(@NotNull Location location, Axis axis) {
        return getCoordinateAxis(location.toVector(), axis);
    }

    public double getCoordinateAxis(@NotNull Entity entity, Axis axis) {
        return getCoordinateAxis(entity.getLocation(), axis);
    }

    public double getCoordinateAxis(@NotNull Block block, Axis axis) {
        return getCoordinateAxis(block.getLocation(), axis);
    }

    public boolean isBlockAtLocation(Material material, Location location) {
        Block block = location.getBlock();
        return block.getType().equals(material);
    }

    @EmptyCollection
    public List<Entity> getEntitiesWithinRadius(Location location, double radius, @Nullable Predicate<Entity> filter) {
        return (List<Entity>) location.getWorld().getNearbyEntities(location, radius, radius, radius, filter);
    }

    @EmptyCollection
    public List<Entity> getEntitiesWithinRadius(Location location, double radius) {
        return getEntitiesWithinRadius(location, radius, null);
    }

    @EmptyCollection
    public List<Entity> getEntities(Location location, @Nullable Predicate<Entity> filter) {
        return getEntitiesWithinRadius(location, 0.01, filter);
    }

    @EmptyCollection
    public List<Entity> getEntities(Location location) {
        return getEntities(location, null);
    }

    public Entity getNearestEntity(Location location, double radius) {
        return getEntitiesWithinRadius(location, radius).stream()
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distance(location)))
                .orElse(null);
    }

    public boolean isEntityAtLocation(EntityType entityType, Location location) {
        return !getEntities(location, entity -> entity.getType().equals(entityType)).isEmpty();
    }


    public Location toLocation(@NotNull Vector3i vector3i, @Nullable World world) {
        return new Location(world, vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector toVector(@NotNull Vector3i vector3i) {
        return new Vector(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }
}
