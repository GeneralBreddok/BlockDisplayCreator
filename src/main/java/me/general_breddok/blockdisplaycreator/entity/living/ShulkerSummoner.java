package me.general_breddok.blockdisplaycreator.entity.living;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Shulker;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ShulkerSummoner extends LivingEntitySummoner<Shulker> {

    Float peek;
    BlockFace attachedFace;
    DyeColor color;

    public ShulkerSummoner() {
        super(Shulker.class);
    }

    public ShulkerSummoner(ShulkerCharacteristics characteristics) {
        super(Shulker.class, characteristics);

        peek = characteristics.getPeek();
        attachedFace = characteristics.getAttachedFace();
        color = characteristics.getColor();
    }

    @Override
    public Shulker summon(@NotNull Location location) {
        Shulker entity = super.summon(location);

        OperationUtil.doIfNotNull(peek, entity::setPeek);
        OperationUtil.doIfNotNull(attachedFace, entity::setAttachedFace);
        OperationUtil.doIfNotNull(color, entity::setColor);

        return entity;
    }

    @Override
    public ShulkerSummoner clone() {
        ShulkerSummoner cloned = new ShulkerSummoner();

        LivingEntitySummoner<Shulker> superCloned = super.clone();

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

        cloned.remainingAir = superCloned.getRemainingAir();
        cloned.maximumAir = superCloned.getMaximumAir();
        cloned.arrowCooldown = superCloned.getArrowCooldown();
        cloned.arrowsInBody = superCloned.getArrowsInBody();
        cloned.maximumNoDamageTicks = superCloned.getMaximumNoDamageTicks();
        cloned.noDamageTicks = superCloned.getNoDamageTicks();
        cloned.removeWhenFarAway = superCloned.getRemoveWhenFarAway();
        cloned.canPickupItems = superCloned.getCanPickupItems();
        cloned.ai = superCloned.getAi();
        cloned.invisible = superCloned.getInvisible();

        cloned.peek = this.peek;
        cloned.attachedFace = this.attachedFace;
        cloned.color = this.color;

        return cloned;
    }
}
