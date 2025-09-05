package me.general_breddok.blockdisplaycreator.common;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockService;
import org.jetbrains.annotations.ApiStatus;

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
