package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * Defines how a custom block can be placed relative to the world surfaces.
 *
 * <p>This enum provides flexible placement modes that allow controlling
 * whether the block can be placed only vertically, only on side faces,
 * or vertically with additional support for side faces.</p>
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CustomBlockPlacementMode {
    /**
    * Default mode: Block can be placed on any surface (top, bottom, and sides).
    */
    DEFAULT("default"),
    /**
     * Block can only be placed on vertical surfaces (top and bottom).
     * Side placement is not allowed.
     */
    VERTICAL_ONLY("vertical-only"),

    /**
     * Block can only be placed on walls.
     * Placement on top or bottom is not allowed.
     */
    WALLS_ONLY("walls-only"),

    /**
     * Block can be placed on vertical surfaces (top and bottom),
     * while placement on a wall is technically allowed but the block
     * is rendered and behaves as if it were placed vertically.
     */
    VERTICAL_WITH_WALLS("vertical-with-walls");

    /**
     * The key associated with the placement mode, used for configuration or serialization.
     */
    String key;

    /**
     * Retrieves the CustomBlockPlacementMode corresponding to the given key.
     *
     * @param key The key representing the desired placement mode.
     * @return The matching CustomBlockPlacementMode, or null if the key is null.
     * @throws IllegalArgumentException if the key does not match any placement mode.
     */
    public static CustomBlockPlacementMode getByKey(@NotNull String key) throws IllegalArgumentException {
        for (CustomBlockPlacementMode mode : values()) {
            if (mode.getKey().equalsIgnoreCase(key)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid CustomBlockPlacementMode key: " + key + " valid keys are: " + String.join(", ", java.util.Arrays.stream(values()).map(CustomBlockPlacementMode::getKey).toList()));
    }
}



