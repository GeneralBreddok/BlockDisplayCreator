package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.Nullable;

public interface ConfiguredInteraction extends ConfiguredEntity<Interaction>, DeepCloneable<ConfiguredInteraction> {
    @Nullable
    InteractionHandler getInteractionHandler();

    void setInteractionHandler(InteractionHandler interactionHandler);
}
