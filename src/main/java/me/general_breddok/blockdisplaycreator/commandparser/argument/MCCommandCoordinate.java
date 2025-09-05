package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandArgumentException;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.util.LocationUtil;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Represents a coordinate in a 3D space.
 * Supports relative coordinates (~) and local coordinates (^).
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MCCommandCoordinate extends MCCommandArgument implements CommandCoordinate {
    PositionModifier modifier;
    double addition;

    /**
     * Constructs a new CommandCoordinate object.
     *
     * @param coordinate The coordinate string. Must be a valid coordinate string.
     * @throws IllegalArgumentException If the coordinate string is not valid.
     */
    public MCCommandCoordinate(@NotNull String coordinate, @NotNull CommandLine commandLine) {
        super(coordinate, commandLine);
        if (!stringArg.matches("^(~|\\^|~?-?\\d+(\\.\\d+)?|\\^-?\\d+(\\.\\d+)?)$")) {
            throw new InvalidCommandArgumentException("The " + coordinate + " string is not a command coordinate!", commandLine.toString());
        }

        char first = stringArg.charAt(0); //~
        this.modifier = PositionModifier.getModifier(first); //RELATIVE


        String strAddition = stringArg; //~-0.5

        if (this.modifier != PositionModifier.EMPTY) {
            strAddition = stringArg.substring(1); //-0.5
        }

        if (!strAddition.isEmpty()) {
            addition = Double.parseDouble(strAddition); //-0.5
        }
    }

    public MCCommandCoordinate(@NotNull CommandArgument argument, @NotNull CommandLine commandLine) {
        this(argument.toString(), commandLine);
    }

    public MCCommandCoordinate(double coordinate, @NotNull CommandLine commandLine) {
        this(String.valueOf(coordinate), commandLine);
    }

    public double applyOffset(double coordinateAxisValue) {
        addition = applyOffset(coordinateAxisValue, null, null);

        return addition;
    }

    @Override
    public double applyOffset(@NotNull Vector vector, @NotNull Axis axis, @Nullable EntityRotation rotation) {
        return applyOffset(LocationUtil.getCoordinateAxis(vector, axis), axis, rotation);
    }

    @Override
    public double applyOffset(@NotNull Location location, @NotNull Axis axis, @Nullable EntityRotation rotation) {
        return applyOffset(location.toVector(), axis, rotation);
    }

    @Override
    public double applyOffset(@NotNull Entity entity, @NotNull Axis axis, @Nullable EntityRotation rotation) {
        return applyOffset(entity.getLocation(), axis, rotation);
    }

    @Override
    public double applyOffset(double coordinateAxisValue, @Nullable Axis axis, @Nullable EntityRotation rotation) {
        this.addition = switch (this.modifier) {
            case EMPTY -> this.addition;
            case RELATIVE -> this.addition + coordinateAxisValue;

            case LOCAL -> {
                if (rotation != null && axis != null) {
                    Vector rotationOffset = rotation.toCartesian(this.addition);

                    yield switch (axis) {
                        case X -> rotationOffset.getX();
                        case Y -> rotationOffset.getY();
                        case Z -> rotationOffset.getZ();
                    };
                }
                yield this.addition;
            }
        };

        return this.addition;
    }

    public double applyOffset(@NotNull Vector vector, @NotNull Axis axis) {
        return applyOffset(LocationUtil.getCoordinateAxis(vector, axis));
    }

    public double applyOffset(@NotNull Location location, @NotNull Axis axis) {
        return applyOffset(location.toVector(), axis);
    }

    public double applyOffset(@NotNull Entity entity, @NotNull Axis axis) {
        return applyOffset(entity.getLocation(), axis);
    }

    @Override
    public String toString() {
        return modifier.toString() + addition;
    }
}
