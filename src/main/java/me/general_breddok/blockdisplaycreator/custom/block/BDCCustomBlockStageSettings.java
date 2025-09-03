package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BDCCustomBlockStageSettings implements CustomBlockStageSettings {
    CustomBlockPlaceSettings placeSettings;
    CustomBlockBreakSettings breakSettings;

    public BDCCustomBlockStageSettings() {
        this(new BDCCustomBlockPlaceSettings(), new BDCCustomBlockBreakSettings());
    }

    public BDCCustomBlockStageSettings(CustomBlockPlaceSettings placeSettings) {
        this(placeSettings, new BDCCustomBlockBreakSettings());
    }

    public BDCCustomBlockStageSettings(CustomBlockBreakSettings breakSettings) {
        this(new BDCCustomBlockPlaceSettings(), breakSettings);
    }
}
