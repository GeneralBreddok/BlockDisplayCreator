package me.general_breddok.blockdisplaycreator.service;

import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.service.exception.ServiceRegistrationException;
import org.bukkit.block.data.type.LightningRod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CustomBlockServiceManager implements ServiceManager<String, CustomBlockService> {

    private final Map<String, CustomBlockService> services = new HashMap<>();

    @Override
    public void registerService(@NotNull CustomBlockService service) {
        Class<? extends CustomBlockService> serviceClass = service.getClass();
        String serviceClassName = serviceClass.getName();
        if (services.containsKey(serviceClassName)) {
            throw new ServiceRegistrationException("A service of this class has already been registered: " + serviceClassName);
        }

        services.put(serviceClassName, service);
    }

    @Override
    @Nullable
    public CustomBlockService unregisterService(@NotNull String serviceClassName) {
        return services.remove(serviceClassName);
    }

    @Override
    @Nullable
    public CustomBlockService getService(@NotNull String serviceClassName) {
        return services.get(serviceClassName);
    }

    @Override
    @NotNull
    public Collection<CustomBlockService> getServices() {
        return services.values();
    }

    @Override
    public boolean isServiceRegistered(@NotNull String serviceClassName) {
        return services.containsKey(serviceClassName);
    }
}
