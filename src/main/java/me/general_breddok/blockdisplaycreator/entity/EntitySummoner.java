package me.general_breddok.blockdisplaycreator.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.exception.SummonException;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EntitySummoner<E extends Entity> implements Summoner<E>, SpigotEntityCharacteristics, DeepCloneable<EntitySummoner<E>> {
    Class<? extends E> entityClass;

    String customName;
    Vector velocity;
    EntityRotation rotation;
    Integer fireTicks;
    Integer freezeTicks;
    Boolean visualFire;
    Boolean persistent;
    List<Map.Entry<EntityType, EntityCharacteristics>> passengers;
    Integer ticksLived;
    Boolean customNameVisible;
    Boolean visibleByDefault;
    Boolean glowing;
    Boolean invulnerable;
    Boolean silent;
    Boolean gravity;
    Boolean op;
    Integer portalCooldown;
    Float fallDistance;
    Set<String> scoreboardTags;
    TemporaryDataContainer temporaryDataContainer;


    public EntitySummoner(@NotNull Class<? extends E> entityClass) {
        this.entityClass = entityClass;

        passengers = new ArrayList<>();
        scoreboardTags = new HashSet<>();
        temporaryDataContainer = new TemporaryDataContainer();
    }

    public EntitySummoner(@NotNull Class<? extends E> entityClass, EntityCharacteristics characteristics) {
        this.entityClass = entityClass;

        customName = characteristics.getCustomName();
        velocity = characteristics.getVelocity();
        rotation = characteristics.getRotation();
        fireTicks = characteristics.getFireTicks();
        freezeTicks = characteristics.getFreezeTicks();
        visualFire = characteristics.getVisualFire();
        passengers = characteristics.getPassengers();
        customNameVisible = characteristics.getCustomNameVisible();
        glowing = characteristics.getGlowing();
        invulnerable = characteristics.getGlowing();
        silent = characteristics.getSilent();
        gravity = characteristics.getGravity();
        portalCooldown = characteristics.getPortalCooldown();
        fallDistance = characteristics.getFallDistance();
        scoreboardTags = characteristics.getScoreboardTags();

        if (characteristics instanceof SpigotEntityCharacteristics spigotCharacteristics) {
            persistent = spigotCharacteristics.getPersistent();
            ticksLived = spigotCharacteristics.getTicksLived();
            visibleByDefault = spigotCharacteristics.getVisibleByDefault();
            op = spigotCharacteristics.getOp();
        }
    }

    public E summon(@NotNull Location location) {
        E entity;

        try {
            entity = location.getWorld().spawn(location, entityClass);
        } catch (IllegalArgumentException e) {
            throw new SummonException(e);
        }

        temporaryDataContainer.apply(entity);

        OperationUtil.doIfNotNull(rotation, entRot -> entRot.applyToEntity(entity));
        OperationUtil.doIfNotNull(customName, entity::setCustomName);
        OperationUtil.doIfNotNull(freezeTicks, entity::setFreezeTicks);
        OperationUtil.doIfNotNull(fireTicks, entity::setFireTicks);
        OperationUtil.doIfNotNull(portalCooldown, entity::setPortalCooldown);
        OperationUtil.doIfNotNull(fallDistance, entity::setFallDistance);

        OperationUtil.doIfNotNull(op, entity::setOp);
        OperationUtil.doIfNotNull(customNameVisible, entity::setCustomNameVisible);
        OperationUtil.doIfNotNull(glowing, entity::setGlowing);
        OperationUtil.doIfNotNull(gravity, entity::setGravity);
        OperationUtil.doIfNotNull(invulnerable, entity::setInvulnerable);
        OperationUtil.doIfNotNull(persistent, entity::setPersistent);
        OperationUtil.doIfNotNull(silent, entity::setSilent);
        OperationUtil.doIfNotNull(visualFire, entity::setVisualFire);
        scoreboardTags.forEach(entity::addScoreboardTag);

        OperationUtil.doIfNotNull(velocity, entity::setVelocity);


        double entityHeight = entity.getHeight();
        for (Map.Entry<EntityType, EntityCharacteristics> passenger : passengers) {

            EntitySummoner<Entity> summoner = new EntitySummoner<>(passenger.getKey().getEntityClass(), passenger.getValue());

            Entity summonedPassenger = summoner.summon(entity.getLocation().add(0, entityHeight, 0));
            entity.addPassenger(summonedPassenger);
        }

        return entity;
    }

    @Override
    public boolean addPassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        return passengers.remove(Map.entry(passengerType, passengerCharacteristics));
    }

    @Override
    public boolean removePassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        return passengers.remove(Map.entry(passengerType, passengerCharacteristics));
    }

    @Override
    public void eject() {
        passengers.clear();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        return scoreboardTags.add(tag);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        return scoreboardTags.remove(tag);
    }

    @Override
    public void clearScoreboardTags() {
        scoreboardTags.clear();
    }

    @Override
    public EntitySummoner<E> clone() {
        EntitySummoner<E> cloned = new EntitySummoner<>(this.entityClass);

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
