package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BDCCustomBlockPlaceSettings implements CustomBlockPlaceSettings {
    PlacementMode placementMode;
    CommandBundle commandBundle;

    public BDCCustomBlockPlaceSettings(PlacementMode placementMode) {
        this(placementMode, null);
    }

    public BDCCustomBlockPlaceSettings() {
        this(PlacementMode.DEFAULT, null);
    }
}
