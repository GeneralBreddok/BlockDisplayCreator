package me.general_breddok.blockdisplaycreator.entity.living;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.EntitySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LivingEntitySummoner<E extends LivingEntity> extends EntitySummoner<E> {

    Integer remainingAir;
    Integer maximumAir;
    Integer arrowCooldown;
    Integer arrowsInBody;
    Integer maximumNoDamageTicks;
    Integer noDamageTicks;
    Boolean removeWhenFarAway;
    Boolean canPickupItems;
    Boolean ai;
    Boolean invisible;

    public LivingEntitySummoner(@NotNull Class<? extends E> entityClass) {
        super(entityClass);
    }

    public LivingEntitySummoner(@NotNull Class<? extends E> entityClass, LivingEntityCharacteristics characteristics) {
        super(entityClass, characteristics);

        remainingAir = characteristics.getRemainingAir();
        maximumAir = characteristics.getMaximumAir();
        arrowCooldown = characteristics.getArrowCooldown();
        arrowsInBody = characteristics.getArrowsInBody();
        maximumNoDamageTicks = characteristics.getMaximumNoDamageTicks();
        noDamageTicks = characteristics.getNoDamageTicks();
        removeWhenFarAway = characteristics.getRemoveWhenFarAway();
        canPickupItems = characteristics.getCanPickupItems();
        ai = characteristics.hasAI();
        invisible = characteristics.getInvisible();
    }

    @Override
    public E summon(@NotNull Location location) {
        E entity = super.summon(location);

        OperationUtil.doIfNotNull(remainingAir, entity::setRemainingAir);
        OperationUtil.doIfNotNull(maximumAir, entity::setMaximumAir);
        OperationUtil.doIfNotNull(arrowCooldown, entity::setArrowCooldown);
        OperationUtil.doIfNotNull(arrowsInBody, entity::setArrowsInBody);
        OperationUtil.doIfNotNull(maximumNoDamageTicks, entity::setMaximumNoDamageTicks);
        OperationUtil.doIfNotNull(noDamageTicks, entity::setNoDamageTicks);
        OperationUtil.doIfNotNull(removeWhenFarAway, entity::setRemoveWhenFarAway);
        OperationUtil.doIfNotNull(canPickupItems, entity::setCanPickupItems);
        OperationUtil.doIfNotNull(ai, entity::setAI);
        OperationUtil.doIfNotNull(invisible, entity::setInvisible);

        return entity;
    }

    @Override
    public LivingEntitySummoner<E> clone() {
        LivingEntitySummoner<E> cloned = new LivingEntitySummoner<>(this.entityClass);

        EntitySummoner<E> superCloned = super.clone();

        cloned.velocity = superCloned.getVelocity();
        cloned.rotation = superCloned.getRotation();
        cloned.fireTicks = superCloned.getFireTicks();
        cloned.freezeTicks = superCloned.getFreezeTicks();
        cloned.visualFire = superCloned.getVisualFire();
        cloned.persistent = superCloned.getPersistent();
        cloned.passengers = superCloned.getPassengers();
        cloned.fallDistance = superCloned.getFallDistance();
        cloned.ticksLived = superCloned.getTicksLived();
        cloned.customNameVisible = superCloned.getCustomNameVisible();
        cloned.visibleByDefault = superCloned.getVisibleByDefault();
        cloned.glowing = superCloned.getGlowing();
        cloned.invulnerable = superCloned.getInvulnerable();
        cloned.silent = superCloned.getSilent();
        cloned.gravity = superCloned.getGravity();
        cloned.op = superCloned.getOp();
        cloned.portalCooldown = superCloned.getPortalCooldown();
        cloned.scoreboardTags = new HashSet<>(superCloned.getScoreboardTags());
        cloned.customName = superCloned.getCustomName();

        cloned.remainingAir = this.remainingAir;
        cloned.maximumAir = this.maximumAir;
        cloned.arrowCooldown = this.arrowCooldown;
        cloned.arrowsInBody = this.arrowsInBody;
        cloned.maximumNoDamageTicks = this.maximumNoDamageTicks;
        cloned.noDamageTicks = this.noDamageTicks;
        cloned.removeWhenFarAway = this.removeWhenFarAway;
        cloned.canPickupItems = this.canPickupItems;
        cloned.ai = this.ai;
        cloned.invisible = this.invisible;

        return cloned;
    }
}