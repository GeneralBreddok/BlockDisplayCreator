package me.general_breddok.blockdisplaycreator.entity.interaction;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;

/**
 * Represents the characteristics of an entity involved in interactions.
 * <p>
 * Extends {@link EntityCharacteristics} to include properties specific
 * to interactive entities, such as interaction dimensions and responsiveness.
 */
public interface InteractionCharacteristics extends EntityCharacteristics {

    /**
     * Gets the width of the interaction area.
     *
     * @return the interaction width as a {@link Float}
     */
    Float getInteractionWidth();

    /**
     * Sets the width of the interaction area.
     *
     * @param interactionWidth the width to set
     */
    void setInteractionWidth(Float interactionWidth);

    /**
     * Gets the height of the interaction area.
     *
     * @return the interaction height as a {@link Float}
     */
    Float getInteractionHeight();

    /**
     * Sets the height of the interaction area.
     *
     * @param interactionHeight the height to set
     */
    void setInteractionHeight(Float interactionHeight);

    /**
     * Returns whether the entity is responsive to interactions.
     *
     * @return {@code true} if responsive, {@code false} otherwise
     */
    Boolean getResponsive();

    /**
     * Sets whether the entity should respond to interactions.
     *
     * @param responsive {@code true} to make the entity responsive, {@code false} otherwise
     */
    void setResponsive(Boolean responsive);
}

