package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.custom.CommandBundle;
import org.jetbrains.annotations.Nullable;

public interface CustomBlockPlaceSettings {
    PlacementMode getPlacementMode();

    void setPlacementMode(PlacementMode placementMode);

    @Nullable
    CommandBundle getCommandBundle();

    void setCommandBundle(@Nullable CommandBundle commandBundle);

    /**
     * Defines how a custom block can be placed relative to the world surfaces.
     *
     * <p>This enum provides several placement modes, controlling whether the block
     * can be attached only vertically, only horizontally, or with specific
     * combinations of both orientations.</p>
     */
    enum PlacementMode {

        /**
         * Default behavior. Block can be placed on any surface (top, bottom, and sides).
         */
        DEFAULT,

        /**
         * Block can only be placed on vertical faces:
         * <ul>
         * <li> top (ceiling) and</li>
         * <li> bottom (floor)</li>
         * </ul>
         */
        VERTICAL_ONLY,

        /**
         * Block can only be placed on horizontal faces (walls).
         */
        HORIZONTAL_ONLY,

        /**
         * Block can be placed vertically, but horizontal placement is also
         * permitted. When placed on a horizontal face, the block behaves
         * or renders as if it were placed vertically.
         */
        VERTICAL_WITH_HORIZONTAL,

        /**
         * Block can be placed horizontally, but vertical placement is also
         * permitted. When placed on a vertical face, the block behaves
         * or renders as if it were placed horizontally.
         */
        HORIZONTAL_WITH_VERTICAL
    }
}
