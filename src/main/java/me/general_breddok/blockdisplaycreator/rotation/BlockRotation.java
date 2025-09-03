package me.general_breddok.blockdisplaycreator.rotation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.block.BlockFace;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BlockRotation {

    BlockFace attachedFace;
    BlockFace directionFace;

    public EntityRotation toEntityRotation() {
        float yaw = getYawFromDirectionFace();
        float pitch = getPitchFromAttachedFace();
        return new EntityRotation(yaw, pitch);
    }

    public static BlockRotation fromEntityRotation(EntityRotation entityRotation) {
        BlockFace directionFace = getFaceFromYaw(entityRotation.getYaw());
        BlockFace attachedFace = getFaceFromPitch(entityRotation.getPitch());
        return new BlockRotation(attachedFace, directionFace);
    }

    public float getYawFromDirectionFace() {
        return switch (this.directionFace) {
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

    public float getPitchFromAttachedFace() {
        return switch (this.attachedFace) {
            case UP -> -90;
            case DOWN -> 90;
            default -> -90;
        };
    }

    public static BlockFace getFaceFromYaw(float yaw) {
        if (yaw >= 337.5 || yaw < 22.5) {
            return BlockFace.SOUTH;
        } else if (yaw >= 22.5 && yaw < 67.5) {
            return BlockFace.SOUTH_WEST;
        } else if (yaw >= 67.5 && yaw < 112.5) {
            return BlockFace.WEST;
        } else if (yaw >= 112.5 && yaw < 157.5) {
            return BlockFace.NORTH_WEST;
        } else if (yaw >= 157.5 && yaw < 202.5) {
            return BlockFace.NORTH;
        } else if (yaw >= 202.5 && yaw < 247.5) {
            return BlockFace.NORTH_EAST;
        } else if (yaw >= 247.5 && yaw < 292.5) {
            return BlockFace.EAST;
        } else {
            return BlockFace.SOUTH_EAST;
        }
    }

    public static BlockFace getCardinalFaceFromYaw(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else if (yaw >= 225 && yaw < 315) {
            return BlockFace.EAST;
        } else {
            return BlockFace.SOUTH;
        }
    }

    public static BlockFace getFaceFromPitch(float pitch) {
        if (pitch < -67.5) {
            return BlockFace.UP;
        } else if (pitch > 67.5) {
            return BlockFace.DOWN;
        } else {
            return BlockFace.UP;
        }
    }

    public void toOppositeFace() {
        this.attachedFace = this.attachedFace.getOppositeFace();
        this.directionFace = this.directionFace.getOppositeFace();
    }

    public static boolean isSideFace(BlockFace face) {
        return switch (face) {
            case SOUTH, WEST, NORTH, EAST -> true;
            default -> false;
        };
    }

    public static boolean isVerticalFace(BlockFace face) {
        return switch (face) {
            case UP, DOWN -> true;
            default -> false;
        };
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public BlockRotation clone() {
        return new BlockRotation(this.attachedFace, this.directionFace);
    }
}
