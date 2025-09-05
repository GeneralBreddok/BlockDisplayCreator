package me.general_breddok.blockdisplaycreator.entity.interaction;

import java.util.UUID;

/**
 * Represents interaction-specific characteristics of an entity with additional
 * NMS (internal Minecraft server) data for tracking attacks and interactions.
 * <p>
 * Extends {@link InteractionCharacteristics}.
 */
public interface NMSInteractionCharacteristics extends InteractionCharacteristics {

    /**
     * Gets the unique identifier (UUID) associated with the last attack.
     *
     * @return the attack UUID
     */
    UUID getAttackUUID();

    /**
     * Sets the unique identifier (UUID) for the last attack.
     *
     * @param uuid the attack UUID to set
     */
    void setAttackUUID(UUID uuid);

    /**
     * Gets the timestamp of the last attack.
     *
     * @return the attack timestamp in ticks or milliseconds
     */
    Integer getAttackTimestamp();

    /**
     * Sets the timestamp of the last attack.
     *
     * @param timestamp the attack timestamp to set
     */
    void setAttackTimestamp(Integer timestamp);

    /**
     * Gets the unique identifier (UUID) associated with the last interaction.
     *
     * @return the interaction UUID
     */
    UUID getInteractionUUID();

    /**
     * Sets the unique identifier (UUID) for the last interaction.
     *
     * @param uuid the interaction UUID to set
     */
    void setInteractionUUID(UUID uuid);

    /**
     * Gets the timestamp of the last interaction.
     *
     * @return the interaction timestamp in ticks or milliseconds
     */
    Integer getInteractionTimestamp();

    /**
     * Sets the timestamp of the last interaction.
     *
     * @param timestamp the interaction timestamp to set
     */
    void setInteractionTimestamp(Integer timestamp);
}