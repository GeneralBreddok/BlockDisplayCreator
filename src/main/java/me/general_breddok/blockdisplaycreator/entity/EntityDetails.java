package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

/**
 * Extends {@link EntityCharacteristics} to include specific entity data.
 * <p>
 * Provides methods for accessing and modifying the entity's type, location, and unique identifier (UUID).
 * </p>
 */
public interface EntityDetails extends EntityCharacteristics {

    /**
     * Gets the type of the entity.
     *
     * @return the {@link EntityType} of the entity
     */
    EntityType getType();

    /**
     * Sets the type of the entity.
     *
     * @param entityType the {@link EntityType} to set
     */
    void setType(EntityType entityType);

    /**
     * Gets the location of the entity in the world.
     *
     * @return the {@link Location} of the entity
     */
    Location getLocation();

    /**
     * Sets the location of the entity in the world.
     *
     * @param location the {@link Location} to set
     */
    void setLocation(Location location);

    /**
     * Gets the unique identifier (UUID) of the entity.
     *
     * @return the {@link UUID} of the entity
     */
    UUID getUniqueId();

    /**
     * Sets the unique identifier (UUID) of the entity.
     *
     * @param uuid the {@link UUID} to set
     */
    void setUniqueId(UUID uuid);
}
