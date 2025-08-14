package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class EntityUtil {
    public void teleportEntityWithPassengers(@NotNull Entity entity, @NotNull Location location) {
        List<Entity> passengers = entity.getPassengers();

        entity.eject();
        entity.teleport(location);

        passengers.forEach(entity::addPassenger);
    }

    public void teleportEntityWithVehicle(@NotNull Entity entity, @NotNull Location location) {
        Entity vehicle = entity.getVehicle();
        if (vehicle == null) {
            return;
        }
        teleportEntityWithPassengers(vehicle, location);
    }

    public <E extends Entity> EntityType getEntityType(Class<E> entityClass) {
        for (EntityType type : EntityType.values()) {
            if (type.getEntityClass() != null && type.getEntityClass().isAssignableFrom(entityClass)) {
                return type;
            }
        }
        return null;
    }

    public boolean isSpawnable(@NotNull Class<? extends Entity> entityClass) {
        return getEntityType(entityClass) != null;
    }

    public Entity getNearEntity(Location nearbyLoc, int entityId) {
        World world = nearbyLoc.getWorld();
        Chunk center = nearbyLoc.getChunk();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (Entity entity : world.getChunkAt(center.getX() + dx, center.getZ() + dz).getEntities()) {
                    if (entity.getEntityId() == entityId) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
}
