package me.general_breddok.blockdisplaycreator.common;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.UUID;

@ApiStatus.Internal
@UtilityClass
@Deprecated
public final class DeprecatedFeatureAdapter {
    public String checkMissingInteractionIdentifier(String identifier) {
        if (identifier == null) {
            return "interaction";
        }
        return identifier;
    }

    public String checkMissingServiceClass(String serviceClass) {
        if (serviceClass == null) {
            return BlockDisplayCreator.getInstance().getCustomBlockService().getClass().getName();
        }
        return serviceClass;
    }

    public UUID checkMissingCustomBlockUUID(UUID uuid) {
        if (uuid == null) {
            return UUID.randomUUID();
        }
        return uuid;
    }
}
