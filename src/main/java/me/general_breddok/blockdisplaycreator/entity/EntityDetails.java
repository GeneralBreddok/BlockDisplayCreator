package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public interface EntityDetails extends EntityCharacteristics {
    EntityType getType();

    void setType(EntityType entityType);

    Location getLocation();

    void setLocation(Location location);

    UUID getUniqueId();

    void setUniqueId(UUID uuid);
}
