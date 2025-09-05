package me.general_breddok.blockdisplaycreator.entity;

import me.general_breddok.blockdisplaycreator.annotation.EmptyCollection;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import org.bukkit.Nameable;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the set of configurable characteristics for an entity.
 * <p>
 * Provides methods to access and modify basic entity properties such as velocity,
 * rotation, fire status, passengers, visual effects, invulnerability, and more.
 * Extends {@link Nameable} to include a name for the entity.
 * </p>
 */
public interface EntityCharacteristics extends Nameable {

    /**
     * Gets the current velocity of the entity.
     *
     * @return the entity's velocity vector
     */
    Vector getVelocity();

    /**
     * Sets the velocity of the entity.
     *
     * @param velocity the velocity vector to apply
     */
    void setVelocity(Vector velocity);

    /**
     * Gets the current rotation of the entity.
     *
     * @return the entity's rotation
     */
    EntityRotation getRotation();

    /**
     * Sets the rotation of the entity.
     *
     * @param rotation the rotation to apply
     */
    void setRotation(EntityRotation rotation);

    /**
     * Gets the remaining fire ticks of the entity.
     *
     * @return number of fire ticks, or null if not applicable
     */
    Integer getFireTicks();

    /**
     * Sets the fire ticks for the entity.
     *
     * @param ticks number of ticks to set
     */
    void setFireTicks(Integer ticks);

    /**
     * Gets the remaining freeze ticks of the entity.
     *
     * @return number of freeze ticks, or null if not applicable
     */
    Integer getFreezeTicks();

    /**
     * Sets the freeze ticks for the entity.
     *
     * @param ticks number of ticks to set
     */
    void setFreezeTicks(Integer ticks);

    /**
     * Gets the portal cooldown in ticks.
     *
     * @return the portal cooldown, or null if not applicable
     */
    Integer getPortalCooldown();

    /**
     * Sets the portal cooldown in ticks.
     *
     * @param portalCooldown cooldown value to set
     */
    void setPortalCooldown(Integer portalCooldown);

    /**
     * Checks whether visual fire effect is active on the entity.
     *
     * @return true if visual fire is enabled, false otherwise
     */
    Boolean getVisualFire();

    /**
     * Sets whether the entity should show the visual fire effect.
     *
     * @param fire true to enable, false to disable
     */
    void setVisualFire(Boolean fire);

    /**
     * Gets the current fall distance of the entity.
     *
     * @return fall distance
     */
    Float getFallDistance();

    /**
     * Sets the fall distance of the entity.
     *
     * @param fallDistance distance to set
     */
    void setFallDistance(Float fallDistance);

    /**
     * Gets the list of passengers attached to the entity.
     *
     * @return list of passenger type and characteristics pairs
     */
    @EmptyCollection
    List<Map.Entry<EntityType, EntityCharacteristics>> getPassengers();

    /**
     * Sets the list of passengers attached to the entity.
     *
     * @param passengers list of passenger type and characteristics pairs
     */
    void setPassengers(List<Map.Entry<EntityType, EntityCharacteristics>> passengers);

    /**
     * Adds a passenger to the entity.
     *
     * @param passengerType            type of the passenger
     * @param passengerCharacteristics characteristics of the passenger
     * @return true if added successfully, false otherwise
     */
    boolean addPassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics);

    /**
     * Removes a passenger from the entity.
     *
     * @param passengerType            type of the passenger
     * @param passengerCharacteristics characteristics of the passenger
     * @return true if removed successfully, false otherwise
     */
    boolean removePassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics);

    /**
     * Ejects all passengers from the entity.
     */
    void eject();

    /**
     * Checks if the custom name is visible.
     *
     * @return true if visible, false otherwise
     */
    Boolean getCustomNameVisible();

    /**
     * Sets whether the custom name is visible.
     *
     * @param customNameVisible true to show, false to hide
     */
    void setCustomNameVisible(Boolean customNameVisible);

    /**
     * Checks if the entity is glowing.
     *
     * @return true if glowing, false otherwise
     */
    Boolean getGlowing();

    /**
     * Sets the glowing effect of the entity.
     *
     * @param glowing true to enable, false to disable
     */
    void setGlowing(Boolean glowing);

    /**
     * Checks if the entity is invulnerable.
     *
     * @return true if invulnerable, false otherwise
     */
    Boolean getInvulnerable();

    /**
     * Sets the invulnerability state of the entity.
     *
     * @param invulnerable true to enable, false to disable
     */
    void setInvulnerable(Boolean invulnerable);

    /**
     * Checks if the entity is silent.
     *
     * @return true if silent, false otherwise
     */
    Boolean getSilent();

    /**
     * Sets the silent state of the entity.
     *
     * @param silent true to enable, false to disable
     */
    void setSilent(Boolean silent);

    /**
     * Checks if the entity is affected by gravity.
     *
     * @return true if gravity applies, false otherwise
     */
    Boolean getGravity();

    /**
     * Sets whether the entity is affected by gravity.
     *
     * @param gravity true to enable gravity, false to disable
     */
    void setGravity(Boolean gravity);

    /**
     * Gets the set of scoreboard tags attached to the entity.
     *
     * @return set of scoreboard tags
     */
    @EmptyCollection
    Set<String> getScoreboardTags();

    /**
     * Adds a scoreboard tag to the entity.
     *
     * @param tag the tag to add
     * @return true if added successfully, false otherwise
     */
    boolean addScoreboardTag(String tag);

    /**
     * Removes a scoreboard tag from the entity.
     *
     * @param tag the tag to remove
     * @return true if removed successfully, false otherwise
     */
    boolean removeScoreboardTag(String tag);

    /**
     * Clears all scoreboard tags from the entity.
     */
    void clearScoreboardTags();
}


