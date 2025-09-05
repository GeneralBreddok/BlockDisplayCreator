package me.general_breddok.blockdisplaycreator.entity;

/**
 * Extends {@link EntityCharacteristics} to include additional properties specific
 * to Spigot entities.
 * <p>
 * Provides methods for controlling persistence, operator status, lifetime ticks,
 * and default visibility.
 * </p>
 */
public interface SpigotEntityCharacteristics extends EntityCharacteristics {

    /**
     * Checks whether the entity is persistent (does not despawn naturally).
     *
     * @return true if persistent, false otherwise
     */
    Boolean getPersistent();

    /**
     * Sets the persistence of the entity.
     *
     * @param persistent true to make the entity persistent, false otherwise
     */
    void setPersistent(Boolean persistent);

    /**
     * Checks whether the entity has operator (op) privileges.
     *
     * @return true if the entity is op, false otherwise
     */
    Boolean getOp();

    /**
     * Sets the operator (op) status of the entity.
     *
     * @param op true to grant op status, false to revoke
     */
    void setOp(Boolean op);

    /**
     * Gets the total number of ticks the entity has existed.
     *
     * @return ticks lived
     */
    Integer getTicksLived();

    /**
     * Sets the total number of ticks the entity has existed.
     *
     * @param ticksLived number of ticks to set
     */
    void setTicksLived(Integer ticksLived);

    /**
     * Checks whether the entity is visible by default.
     *
     * @return true if visible by default, false otherwise
     */
    Boolean getVisibleByDefault();

    /**
     * Sets the default visibility of the entity.
     *
     * @param visibleByDefault true to make visible by default, false otherwise
     */
    void setVisibleByDefault(Boolean visibleByDefault);
}

