package me.general_breddok.blockdisplaycreator.entity.interaction;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;

import java.util.UUID;

public interface NMSInteractionCharacteristics extends InteractionCharacteristics, EntityCharacteristics {
    UUID getAttackUUID();

    void setAttackUUID(UUID uuid);

    Integer getAttackTimestamp();

    void setAttackTimestamp(Integer timestamp);

    UUID getInteractionUUID();

    void setInteractionUUID(UUID uuid);

    Integer getInteractionTimestamp();

    void setInteractionTimestamp(Integer timestamp);
}
