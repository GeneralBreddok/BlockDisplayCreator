package me.general_breddok.blockdisplaycreator.entity.living;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;

/**
 * Extends {@link EntityCharacteristics} to include attributes specific to living entities.
 * <p>
 * Provides access to air levels, arrow status, damage ticks, AI behavior, visibility, and item pickup capability.
 * </p>
 */
public interface LivingEntityCharacteristics extends EntityCharacteristics {

    /**
     * Gets the remaining air (breath) of the entity in ticks.
     *
     * @return remaining air ticks
     */
    Integer getRemainingAir();

    /**
     * Sets the remaining air (breath) of the entity in ticks.
     *
     * @param ticks remaining air ticks
     */
    void setRemainingAir(Integer ticks);

    /**
     * Gets the maximum air (breath) capacity of the entity in ticks.
     *
     * @return maximum air ticks
     */
    Integer getMaximumAir();

    /**
     * Sets the maximum air (breath) capacity of the entity in ticks.
     *
     * @param ticks maximum air ticks
     */
    void setMaximumAir(Integer ticks);

    /**
     * Gets the cooldown for arrows stuck in the entity.
     *
     * @return arrow cooldown in ticks
     */
    Integer getArrowCooldown();

    /**
     * Sets the cooldown for arrows stuck in the entity.
     *
     * @param ticks arrow cooldown in ticks
     */
    void setArrowCooldown(Integer ticks);

    /**
     * Gets the number of arrows currently in the entity's body.
     *
     * @return number of arrows
     */
    Integer getArrowsInBody();

    /**
     * Sets the number of arrows currently in the entity's body.
     *
     * @param count number of arrows
     */
    void setArrowsInBody(Integer count);

    /**
     * Gets the maximum number of ticks for which the entity can avoid taking damage.
     *
     * @return maximum no-damage ticks
     */
    Integer getMaximumNoDamageTicks();

    /**
     * Sets the maximum number of ticks for which the entity can avoid taking damage.
     *
     * @param ticks maximum no-damage ticks
     */
    void setMaximumNoDamageTicks(Integer ticks);

    /**
     * Gets the current number of no-damage ticks for the entity.
     *
     * @return current no-damage ticks
     */
    Integer getNoDamageTicks();

    /**
     * Sets the current number of no-damage ticks for the entity.
     *
     * @param ticks current no-damage ticks
     */
    void setNoDamageTicks(Integer ticks);

    /**
     * Determines whether the entity should be removed when far away from players.
     *
     * @return true if the entity should be removed, false otherwise
     */
    Boolean getRemoveWhenFarAway();

    /**
     * Sets whether the entity should be removed when far away from players.
     *
     * @param remove true to remove, false otherwise
     */
    void setRemoveWhenFarAway(Boolean remove);

    /**
     * Determines whether the entity can pick up items.
     *
     * @return true if the entity can pick up items, false otherwise
     */
    Boolean getCanPickupItems();

    /**
     * Sets whether the entity can pick up items.
     *
     * @param pickup true to allow item pickup, false otherwise
     */
    void setCanPickupItems(Boolean pickup);

    /**
     * Checks if the entity has AI enabled.
     *
     * @return true if AI is enabled, false otherwise
     */
    Boolean hasAI();

    /**
     * Sets whether the entity has AI enabled.
     *
     * @param ai true to enable AI, false to disable
     */
    void setAI(Boolean ai);

    /**
     * Determines whether the entity is invisible.
     *
     * @return true if invisible, false otherwise
     */
    boolean getInvisible();

    /**
     * Sets whether the entity is invisible.
     *
     * @param invisible true to make invisible, false to make visible
     */
    void setInvisible(boolean invisible);
}
