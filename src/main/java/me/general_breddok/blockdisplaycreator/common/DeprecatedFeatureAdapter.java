package me.general_breddok.blockdisplaycreator.common;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockService;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.UUID;

@ApiStatus.Internal
@UtilityClass
@Deprecated
public final class DeprecatedFeatureAdapter {
    public UUID checkMissingCustomBlockUUID(UUID uuid) {
        if (uuid == null) {
            return UUID.randomUUID();
        }
        return uuid;
    }

    public String checkMissingServiceClass(String serviceClass) {
        if (serviceClass == null) {
            return BDCCustomBlockService.class.getName();
        }
        return serviceClass;
    }
}
