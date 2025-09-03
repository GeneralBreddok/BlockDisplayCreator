package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BDCCustomBlockBreakSettings implements CustomBlockBreakSettings {
    DropMode dropMode;
    CommandBundle commandBundle;

    public BDCCustomBlockBreakSettings(DropMode dropMode) {
        this(dropMode, null);
    }

    public BDCCustomBlockBreakSettings() {
        this(DropMode.ON_GROUND, null);
    }
}
