package me.general_breddok.blockdisplaycreator.entity.interaction;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.EntitySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InteractionSummoner extends EntitySummoner<Interaction> implements InteractionCharacteristics {
    Float interactionWidth;
    Float interactionHeight;
    Boolean responsive;

    public InteractionSummoner(Float interactionWidth, Float interactionHeight, Boolean responsive) {
        super(Interaction.class);

        this.interactionWidth = interactionWidth;
        this.interactionHeight = interactionHeight;
        this.responsive = responsive;
    }

    public InteractionSummoner(Float interactionWidth, Float interactionHeight) {
        this(interactionWidth, interactionHeight, true);
    }

    public InteractionSummoner(InteractionCharacteristics characteristics) {
        super(Interaction.class, characteristics);

        this.interactionWidth = characteristics.getInteractionWidth();
        this.interactionHeight = characteristics.getInteractionHeight();
        this.responsive = characteristics.getResponsive();
    }

    public InteractionSummoner(BoundingBox box) {
        super(Interaction.class);
        this.interactionHeight = (float) (box.getMaxY() - box.getMinY());
        double widthX = box.getMaxX() - box.getMinX();
        double widthZ = box.getMaxZ() - box.getMinZ();
        this.interactionWidth = (float) Math.max(widthX, widthZ);
    }


    @Override
    public Interaction summon(@NotNull Location location) {
        Interaction interaction = super.summon(location);

        OperationUtil.doIfNotNull(interactionWidth, interaction::setInteractionWidth);
        OperationUtil.doIfNotNull(interactionHeight, interaction::setInteractionHeight);
        OperationUtil.doIfNotNull(responsive, interaction::setResponsive);

        return interaction;
    }

    @Override
    public InteractionSummoner clone() {
        InteractionSummoner cloned = new InteractionSummoner(this.interactionWidth, this.interactionHeight, this.responsive);

        if (velocity != null) {
            cloned.velocity = new Vector(this.velocity.getX(), this.velocity.getY(), this.velocity.getZ());
        }

        cloned.rotation = this.rotation != null ? this.rotation.clone() : null;

        cloned.fireTicks = this.fireTicks;
        cloned.freezeTicks = this.freezeTicks;
        cloned.visualFire = this.visualFire;
        cloned.persistent = this.persistent;

        cloned.passengers = this.passengers.stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        cloned.fallDistance = this.fallDistance;
        cloned.ticksLived = this.ticksLived;
        cloned.customNameVisible = this.customNameVisible;
        cloned.visibleByDefault = this.visibleByDefault;
        cloned.glowing = this.glowing;
        cloned.invulnerable = this.invulnerable;
        cloned.silent = this.silent;
        cloned.gravity = this.gravity;
        cloned.op = this.op;
        cloned.portalCooldown = this.portalCooldown;

        cloned.scoreboardTags = new HashSet<>(this.scoreboardTags);

        cloned.customName = this.customName;

        return cloned;
    }
}
