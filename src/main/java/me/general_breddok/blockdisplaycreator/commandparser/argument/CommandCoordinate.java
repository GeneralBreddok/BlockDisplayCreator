package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.util.LocationUtil;
import me.general_breddok.blockdisplaycreator.util.MathUtil;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandCoordinate extends CommandArgument {
    PositionModifier getModifier();
    void setModifier(PositionModifier modifier);
    double getAddition();
    void setAddition(double addition);

    double applyOffset(double coordinateAxisValue);
    double applyOffset(double coordinateAxisValue, @Nullable Axis axis, @Nullable EntityRotation rotation);
    double applyOffset(@NotNull Vector vector, @NotNull Axis axis, @Nullable EntityRotation rotation);
    double applyOffset(@NotNull Location location, @NotNull Axis axis, @Nullable EntityRotation rotation);
    double applyOffset(@NotNull Entity entity, @NotNull Axis axis, @Nullable EntityRotation rotation);
    double applyOffset(@NotNull Vector vector, @NotNull Axis axis);
    double applyOffset(@NotNull Location location, @NotNull Axis axis);
    double applyOffset(@NotNull Entity entity, @NotNull Axis axis);

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    enum PositionModifier {
        RELATIVE("~"),
        LOCAL("^"),
        EMPTY("");

        String symbol;

        public static PositionModifier getModifier(char c) {
            if (c == '~') {
                return PositionModifier.RELATIVE;
            } else if (c == '^') {
                return PositionModifier.LOCAL;
            }
            return PositionModifier.EMPTY;
        }


        @Override
        public String toString() {
            return symbol;
        }
    }
}
