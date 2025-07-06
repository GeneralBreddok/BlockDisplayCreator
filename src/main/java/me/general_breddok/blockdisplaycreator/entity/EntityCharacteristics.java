package me.general_breddok.blockdisplaycreator.entity;

import me.general_breddok.blockdisplaycreator.annotation.EmptyCollection;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import org.bukkit.Nameable;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityCharacteristics extends Nameable {
    Vector getVelocity();

    void setVelocity(Vector velocity);

    EntityRotation getRotation();

    void setRotation(EntityRotation rotation);

    Integer getFireTicks();

    void setFireTicks(Integer ticks);

    Integer getFreezeTicks();

    void setFreezeTicks(Integer ticks);

    Integer getPortalCooldown();

    void setPortalCooldown(Integer portalCooldown);

    Boolean getVisualFire();

    void setVisualFire(Boolean fire);

    Float getFallDistance();

    void setFallDistance(Float fallDistance);

    @EmptyCollection
    List<Map.Entry<EntityType, EntityCharacteristics>> getPassengers();

    void setPassengers(List<Map.Entry<EntityType, EntityCharacteristics>> passengers);

    boolean addPassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics);

    boolean removePassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics);

    void eject();

    Boolean getCustomNameVisible();

    void setCustomNameVisible(Boolean customNameVisible);

    Boolean getGlowing();

    void setGlowing(Boolean glowing);

    Boolean getInvulnerable();

    void setInvulnerable(Boolean invulnerable);

    Boolean getSilent();

    void setSilent(Boolean silent);

    Boolean getGravity();

    void setGravity(Boolean gravity);

    @EmptyCollection
    Set<String> getScoreboardTags();

    boolean addScoreboardTag(String tag);

    boolean removeScoreboardTag(String tag);

    void clearScoreboardTags();
}

