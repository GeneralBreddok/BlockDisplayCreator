package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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

    public void teleportWithVehicle(@NotNull Entity entity, @NotNull Location location) {
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
}
