package me.general_breddok.blockdisplaycreator.custom;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.Summoner;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguredInteractionDta implements ConfiguredInteraction {
    Summoner<Interaction> summoner;
    Vector offset;
    String identifier;
    @Nullable
    InteractionHandler interactionHandler;

    @Override
    public Interaction summon(@NotNull Location location) {
        Interaction interaction = summoner.summon(location.clone().add(offset));

        interaction.setPersistent(true);
        interaction.setInvulnerable(true);

        return interaction;
    }

    @Override
    public ConfiguredInteraction clone() {
        Summoner<Interaction> clonedSummoner = DeepCloneable.tryClone(this.summoner);
        return new ConfiguredInteractionDta(clonedSummoner, this.offset.clone(), this.identifier, this.interactionHandler);
    }
}
