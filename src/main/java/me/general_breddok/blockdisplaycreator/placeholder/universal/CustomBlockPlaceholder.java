package me.general_breddok.blockdisplaycreator.placeholder.universal;

import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.Location;

public record CustomBlockPlaceholder(CustomBlock context) implements UniversalPlaceholder<CustomBlock> {
    @Override
    public String applyPlaceholders(String template) {
        if (this.context == null) {
            return template;
        }

        Location location = context.getLocation();

        return template
                .replace("%customblock_x%", String.valueOf(location.getX()))
                .replace("%customblock_y%", String.valueOf(location.getY()))
                .replace("%customblock_y%", String.valueOf(location.getY()))
                .replace("%customblock_name%", context.getName());
    }
}
