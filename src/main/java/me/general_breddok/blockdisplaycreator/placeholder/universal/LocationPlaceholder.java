package me.general_breddok.blockdisplaycreator.placeholder.universal;

import org.bukkit.Location;

public record LocationPlaceholder(Location context) implements UniversalPlaceholder<Location> {
    @Override
    public String apply(String template) {
        if (this.context == null) {
            return template;
        }

        if (template.isEmpty()) {
            return template;
        }

        return template
                .replace("%x%", String.valueOf(this.context.getX()))
                .replace("%y%", String.valueOf(this.context.getY()))
                .replace("%z%", String.valueOf(this.context.getZ()))
                .replace("%world%", this.context.getWorld().getName());
    }
}
