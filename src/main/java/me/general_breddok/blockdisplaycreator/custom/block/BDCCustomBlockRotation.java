package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.rotation.snapper.YawSnapper;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.EntityUtil;
import me.general_breddok.blockdisplaycreator.util.MathUtil;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PACKAGE)
@AllArgsConstructor
public class BDCCustomBlockRotation implements CustomBlockRotation {
    BlockFace attachedFace;
    float direction;

    @Override
    public void adjustDisplayRotation(@NotNull Display display, int sidesCount) {

        EntityRotation displayRotation = new EntityRotation(display);

        if (attachedFace == BlockFace.UP || attachedFace == BlockFace.DOWN) {
            YawSnapper yawSnapper = new YawSnapper(sidesCount);
            float closestAngle = yawSnapper.getClosestAngle(direction);
            displayRotation.setYaw(closestAngle);
        } else {
            float yaw = switch (attachedFace) {
                case SOUTH -> 0;
                case WEST -> 90;
                case NORTH -> 180;
                case EAST -> 270;
                default -> 0;
            };

            displayRotation.setPitch(90);
            displayRotation.setYaw(yaw);

            if (!display.isInsideVehicle()) {
                withDirectionalOffset(this.attachedFace, 0.5, 0.499, (x, y, z) -> {
                    Location newLoc = display.getLocation().add(x, y, z);
                    EntityUtil.teleportEntityWithPassengers(display, newLoc);
                });
            }
        }

        displayRotation.applyToEntity(display);
    }

    @Override
    public void adjustInteractionRotation(@NotNull Interaction interaction, Vector offset, int sidesCount) {
        if (offset == null) {
            return;
        }

        Location location = interaction.getLocation();

        Vector calculatedOffset = calculateOffset(interaction, offset, sidesCount);

        Vector subtracted = calculatedOffset.subtract(offset);
        interaction.teleport(location.clone().add(subtracted));
    }

    @Override
    public void adjustCollisionRotation(@NotNull Shulker collision, Vector offset, int sidesCount) {
        if (offset == null) {
            return;
        }

        Entity vehicle = collision.getVehicle();

        Location location = Objects.requireNonNullElse(vehicle, collision).getLocation();

        Vector calculatedOffset = calculateOffset(collision, offset, sidesCount);

        Vector subtracted = calculatedOffset.subtract(offset);
        EntityUtil.teleportWithVehicle(collision, location.clone().add(subtracted));
    }

    public Vector calculateOffset(Entity entity, Vector originalOffset, int sidesCount) {
        return switch (attachedFace) {
            case UP, DOWN -> rotateForVertical(originalOffset, sidesCount);
            case NORTH, SOUTH, EAST, WEST -> rotateForCardinal(entity, originalOffset);
            default -> rotateForDiagonal(entity, originalOffset, sidesCount);
        };
    }

    private Vector rotateForVertical(Vector originalOffset, int sidesCount) {
        if (MathUtil.isYOnly(originalOffset)) {
            return originalOffset;
        }

        float snappedAngle = new YawSnapper(sidesCount).getClosestAngle(direction);
        return originalOffset.clone().rotateAroundY(-Math.toRadians(snappedAngle));
    }

    private Vector rotateForCardinal(Entity entity, Vector originalOffset) {
        Vector rotated = originalOffset.clone()
                .rotateAroundY(getYawRadians(attachedFace));

        switch (attachedFace) {
            case NORTH, SOUTH -> rotated.rotateAroundX(Math.toRadians(attachedFace == BlockFace.SOUTH ? 90 : 270));
            case WEST, EAST -> rotated.rotateAroundZ(Math.toRadians(attachedFace == BlockFace.WEST ? 90 : 270));
        }


        applyStandardSideOffset(entity, rotated);
        return rotated;
    }

    private Vector rotateForDiagonal(Entity entity, Vector originalOffset, int sidesCount) {
        Vector rotated = originalOffset.clone();
        float snappedYaw = new YawSnapper(sidesCount).getClosestAngle(this.direction);
        rotated.rotateAroundY(-Math.toRadians(snappedYaw));

        applyStandardSideOffset(entity, rotated);
        return rotated;
    }

    private void applyStandardSideOffset(Entity entity, Vector originalOffset) {
        double halfWidth = entity.getWidth() / 2;
        double halfHeight = entity.getHeight() / 2;

        if (entity instanceof Interaction interaction) {
            halfWidth = interaction.getInteractionWidth() / 2;
            halfHeight = interaction.getInteractionHeight() / 2;
        } else {
            Attribute scaleAttribute = getScaleAttribute();
            if (scaleAttribute != null && entity instanceof Attributable attributable) {
                AttributeInstance attributeInstance = attributable.getAttribute(scaleAttribute);

                if (attributeInstance != null) {
                    double baseValue = attributeInstance.getBaseValue();
                    halfWidth = baseValue / 2;
                    halfHeight = baseValue / 2;
                }
            }
        }

        withDirectionalOffset(attachedFace, 0.5, 0.5, (x, y, z) -> originalOffset.add(new Vector(x, y, z)));
        if (halfWidth != 0 || halfHeight != 0) {
            withDirectionalOffset(attachedFace, -halfHeight, -halfWidth, (x, y, z) -> originalOffset.add(new Vector(x, y, z)));
        }
    }

    private double getYawRadians(BlockFace face) {
        return switch (face) {
            case SOUTH -> Math.toRadians(0);
            case EAST -> Math.toRadians(90);
            case NORTH -> Math.toRadians(180);
            case WEST -> Math.toRadians(270);
            default -> 0.0;
        };
    }

    private void withDirectionalOffset(BlockFace face, double y, double axisOffset, TriConsumer<Double, Double, Double> action) {
        double x = switch (face) {
            case WEST -> axisOffset;
            case EAST -> -axisOffset;
            default -> 0.0;
        };

        double z = switch (face) {
            case NORTH -> axisOffset;
            case SOUTH -> -axisOffset;
            default -> 0.0;
        };

        action.accept(x, y, z);
    }

    private static boolean isEarlier1_20_5(String version) {
        return switch (version) {
            case "1.19.4", "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4" -> true;
            default -> false;
        };
    }

    private static boolean isEarlier1_21_3(String version) {
        return switch (version) {
            case "1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.2" -> true;
            default -> false;
        };
    }

    private Attribute getScaleAttribute() {
        String rawVersion = Bukkit.getBukkitVersion();
        String cleanVersion = rawVersion.split("-")[0];

        if (isEarlier1_20_5(cleanVersion)) {
            return null;
        } else if (isEarlier1_21_3(cleanVersion)) {
            return Attribute.valueOf("GENERIC_SCALE");
        } else {
            return Registry.ATTRIBUTE.get(NamespacedKey.minecraft("scale"));
        }
    }
}
