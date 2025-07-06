package me.general_breddok.blockdisplaycreator.entity.interaction;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;

public interface InteractionCharacteristics extends EntityCharacteristics {
    Float getInteractionWidth();

    void setInteractionWidth(Float interactionWidth);

    Float getInteractionHeight();

    void setInteractionHeight(Float interactionHeight);

    Boolean getResponsive();

    void setResponsive(Boolean responsive);
}
