package me.general_breddok.blockdisplaycreator.placeholder.universal;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;

public record InteractionPlaceholder(Interaction context) implements UniversalPlaceholder<Interaction> {
    @Override
    public String apply(String template) {
        if (context == null) {
            return template;
        }

        if (template.isEmpty()) {
            return template;
        }

        Location location = context.getLocation();

        return template
                .replace("%interaction_x%", String.valueOf(location.getX()))
                .replace("%interaction_y%", String.valueOf(location.getY()))
                .replace("%interaction_z%", String.valueOf(location.getZ()));
    }
}
