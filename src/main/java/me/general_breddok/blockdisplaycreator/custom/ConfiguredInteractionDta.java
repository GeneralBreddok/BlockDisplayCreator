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
    final String identifier;
    @Nullable
    Vector offset;
    @Nullable
    InteractionHandler interactionHandler;

    public ConfiguredInteractionDta(Summoner<Interaction> summoner, String identifier, @Nullable Vector offset) {
        this(summoner, identifier, offset, null);
    }

    public ConfiguredInteractionDta(Summoner<Interaction> summoner, String identifier) {
        this(summoner, identifier, null, null);
    }

    @Override
    public Interaction summon(@NotNull Location location) {
        Location summonLocation = location.clone();

        if (this.offset != null) {
            summonLocation.add(this.offset);
        }

        Interaction interaction = summoner.summon(summonLocation);

        interaction.setPersistent(true);
        interaction.setInvulnerable(true);

        return interaction;
    }

    @Override
    public ConfiguredInteraction clone() {
        Summoner<Interaction> clonedSummoner = DeepCloneable.tryClone(this.summoner);

        Vector clonedOffset = null;

        if (this.offset != null) {
            clonedOffset = this.offset.clone();
        }
        return new ConfiguredInteractionDta(clonedSummoner, this.identifier, clonedOffset, this.interactionHandler);
    }
}
