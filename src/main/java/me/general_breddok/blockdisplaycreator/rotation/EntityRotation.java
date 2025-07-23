package me.general_breddok.blockdisplaycreator.rotation;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityRotation implements Serializable {

    float yaw;
    float pitch;

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityRotation(@NotNull Entity entity) {
        this(entity.getLocation().getYaw(), entity.getLocation().getPitch());
    }

    public EntityRotation(float yaw, float pitch) {
        this.yaw = Location.normalizeYaw(yaw);
        this.pitch = Location.normalizePitch(pitch);
    }

    public void toOppositeRotation() {
        this.yaw = getOppositeYaw();
        this.pitch = getOppositePitch();
    }

    public void addYaw(float yaw) {
        this.yaw = Location.normalizeYaw(this.yaw + yaw);
    }

    public void addPitch(float pitch) {
        this.pitch = Location.normalizePitch(this.pitch + pitch);
    }

    public void addRotation(float yaw, float pitch) {
        this.yaw = Location.normalizeYaw(this.yaw + yaw);
        this.pitch = Location.normalizePitch(this.pitch + pitch);
    }

    public EntityRotation getOppositeRotation() {
        return new EntityRotation(getOppositeYaw(), getOppositePitch());
    }

    public float getOppositeYaw() {
        return (this.yaw + 180) % 360;
    }

    public float getOppositePitch() {
        return -this.pitch;
    }

    public static float toOppositeYaw(float yaw) {
        return (yaw + 180) % 360;
    }

    public static float toOppositePitch(float pitch) {
        return -pitch;
    }

    public BlockRotation toBlockRotation() {
        BlockFace directionFace = getDirectionFace();
        BlockFace attachedFace = getAttachedFace();

        return new BlockRotation(attachedFace, directionFace);
    }

    public static EntityRotation fromBlockRotation(@NotNull BlockRotation blockRotation) {

        BlockFace directionFace = blockRotation.getDirectionFace();
        BlockFace attachedFace = blockRotation.getAttachedFace();

        float yaw = getYawFromDirectionFace(directionFace);
        float pitch = getPitchFromAttachedFace(attachedFace);

        return new EntityRotation(yaw, pitch);
    }

    public static float getPitchFromAttachedFace(BlockFace attachedFace) {
        return switch (attachedFace) {
            case UP -> -90;
            case DOWN -> 90;
            default -> -90;
        };
    }

    public static float getYawFromDirectionFace(BlockFace directionFace) {
        return switch (directionFace) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case EAST -> 90;
            case WEST -> 270;
            case NORTH_EAST -> 135;
            case NORTH_WEST -> 225;
            case SOUTH_EAST -> 45;
            case SOUTH_WEST -> 315;
            default -> 180;
        };
    }

    public BlockFace getDirectionFace() {
        if (this.pitch < -67.5) {
            return BlockFace.UP;
        } else if (this.pitch > 67.5) {
            return BlockFace.DOWN;
        } else {
            if (this.yaw >= 337.5 || this.yaw < 22.5) {
                return BlockFace.SOUTH;
            } else if (this.yaw >= 22.5 && this.yaw < 67.5) {
                return BlockFace.SOUTH_WEST;
            } else if (this.yaw >= 67.5 && this.yaw < 112.5) {
                return BlockFace.WEST;
            } else if (this.yaw >= 112.5 && this.yaw < 157.5) {
                return BlockFace.NORTH_WEST;
            } else if (this.yaw >= 157.5 && this.yaw < 202.5) {
                return BlockFace.NORTH;
            } else if (this.yaw >= 202.5 && this.yaw < 247.5) {
                return BlockFace.NORTH_EAST;
            } else if (this.yaw >= 247.5 && this.yaw < 292.5) {
                return BlockFace.EAST;
            } else {
                return BlockFace.SOUTH_EAST;
            }
        }
    }

    public BlockFace getAttachedFace() {
        if (this.pitch < -67.5) {
            return BlockFace.UP;
        } else if (this.pitch > 67.5) {
            return BlockFace.DOWN;
        } else {
            return BlockFace.UP;
        }
    }

    public Vector toVector() {
        double yawRad = Math.toRadians(this.yaw);
        double pitchRad = Math.toRadians(this.pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vector(x, y, z).normalize();
    }

    public static EntityRotation fromVector(@NotNull Vector vector) {
        double yaw = Math.toDegrees(Math.atan2(-vector.getX(), vector.getZ()));
        double pitch = Math.toDegrees(Math.asin(-vector.getY()));

        return new EntityRotation((float) yaw, (float) pitch);
    }

    public void applyToEntity(@NotNull Entity entity) {
        entity.setRotation(this.yaw, this.pitch);
    }

    public float getYawDifference(@NotNull EntityRotation other) {
        return this.yaw - other.yaw;
    }

    public float getPitchDifference(@NotNull EntityRotation other) {
        return this.pitch - other.pitch;
    }

    public Vector toCartesian(double radius) {
        double azimuthRadians = Math.toRadians(this.yaw);
        double zenithRadians = Math.toRadians(this.pitch);

        double x = radius * Math.sin(zenithRadians) * Math.cos(azimuthRadians);
        double y = radius * Math.cos(zenithRadians);
        double z = radius * Math.sin(zenithRadians) * Math.sin(azimuthRadians);

        return new Vector(x, y, z);
    }

    public static void setYaw(@NotNull Entity entity, float yaw) {
        entity.setRotation(yaw, entity.getLocation().getPitch());
    }

    public static void setPitch(@NotNull Entity entity, float pitch) {
        entity.setRotation(entity.getLocation().getYaw(), pitch);
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public EntityRotation clone() {
        return new EntityRotation(this.yaw, this.pitch);
    }
}
