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


/**
 * Class responsible for summoning Minecraft entities with configurable characteristics.
 * <p>
 * Implements {@link Summoner}, {@link SpigotEntityCharacteristics} and {@link DeepCloneable}.
 * Allows setting entity properties such as velocity, rotation, fire ticks, passengers, scoreboard tags, and more.
 * </p>
 *
 * @param <E> type of the entity to be summoned
 */
@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EntitySummoner<E extends Entity> implements Summoner<E>, SpigotEntityCharacteristics, DeepCloneable<EntitySummoner<E>> {
    /**
     * The class type of the entity to summon.
     */
    Class<? extends E> entityClass;

    /**
     * Custom name of the entity.
     */
    String customName;

    /**
     * Velocity of the entity.
     */
    Vector velocity;

    /**
     * Rotation of the entity.
     */
    EntityRotation rotation;

    /**
     * Fire ticks applied to the entity.
     */
    Integer fireTicks;

    /**
     * Freeze ticks applied to the entity.
     */
    Integer freezeTicks;

    /**
     * Indicates whether the entity visually appears on fire.
     */
    Boolean visualFire;

    /**
     * Indicates whether the entity is persistent (does not despawn).
     */
    Boolean persistent;

    /**
     * List of passengers to be attached to the entity.
     */
    List<Map.Entry<EntityType, EntityCharacteristics>> passengers;

    /**
     * Total ticks the entity has lived.
     */
    Integer ticksLived;

    /**
     * Whether the custom name is visible.
     */
    Boolean customNameVisible;

    /**
     * Whether the entity is visible by default.
     */
    Boolean visibleByDefault;

    /**
     * Whether the entity is glowing.
     */
    Boolean glowing;

    /**
     * Whether the entity is invulnerable.
     */
    Boolean invulnerable;

    /**
     * Whether the entity is silent.
     */
    Boolean silent;

    /**
     * Whether gravity affects the entity.
     */
    Boolean gravity;

    /**
     * Operator status of the entity (if applicable).
     */
    Boolean op;

    /**
     * Cooldown for portals.
     */
    Integer portalCooldown;

    /**
     * Distance the entity has fallen.
     */
    Float fallDistance;

    /**
     * Scoreboard tags associated with the entity.
     */
    Set<String> scoreboardTags;

    /**
     * Container for temporary data during summoning.
     */
    TemporaryDataContainer temporaryDataContainer;

    /**
     * Constructs an EntitySummoner for a specific entity class with default characteristics.
     *
     * @param entityClass the class of entity to summon
     */
    public EntitySummoner(@NotNull Class<? extends E> entityClass) {
        this.entityClass = entityClass;

        passengers = new ArrayList<>();
        scoreboardTags = new HashSet<>();
        temporaryDataContainer = new TemporaryDataContainer();
    }

    /**
     * Constructs an EntitySummoner from an existing {@link EntityCharacteristics} instance.
     *
     * @param entityClass     the class of entity to summon
     * @param characteristics characteristics to apply to the entity
     */
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

    /**
     * Summons the entity at a given location applying all configured characteristics.
     *
     * @param location the spawn location
     * @return the summoned entity instance
     * @throws SummonException if spawning fails
     */
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

    /**
     * Adds a passenger to this entity.
     *
     * @param passengerType            the type of passenger entity
     * @param passengerCharacteristics characteristics of the passenger
     * @return true if successfully added
     */
    @Override
    public boolean addPassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        return passengers.remove(Map.entry(passengerType, passengerCharacteristics));
    }

    /**
     * Removes a passenger from this entity.
     *
     * @param passengerType            the type of passenger entity
     * @param passengerCharacteristics characteristics of the passenger
     * @return true if successfully removed
     */
    @Override
    public boolean removePassenger(EntityType passengerType, EntityCharacteristics passengerCharacteristics) {
        return passengers.remove(Map.entry(passengerType, passengerCharacteristics));
    }

    /**
     * Ejects all passengers from this entity.
     */
    @Override
    public void eject() {
        passengers.clear();
    }

    /**
     * Adds a scoreboard tag to the entity.
     *
     * @param tag the tag to add
     * @return true if successfully added
     */
    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        return scoreboardTags.add(tag);
    }

    /**
     * Removes a scoreboard tag from the entity.
     *
     * @param tag the tag to remove
     * @return true if successfully removed
     */
    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        return scoreboardTags.remove(tag);
    }

    /**
     * Clears all scoreboard tags from the entity.
     */
    @Override
    public void clearScoreboardTags() {
        scoreboardTags.clear();
    }

    /**
     * Creates a clone of this EntitySummoner with all current characteristics.
     *
     * @return a cloned instance of EntitySummoner
     */
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
                .collect(OperationUtil.toArrayList());

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
