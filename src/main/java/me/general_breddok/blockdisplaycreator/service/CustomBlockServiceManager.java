package me.general_breddok.blockdisplaycreator.service;

import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.service.exception.ServiceRegistrationException;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link ServiceManager} for managing
 * {@link CustomBlockService} instances.
 * <p>
 * Services are identified and stored by their fully qualified
 * class name ({@link Class#getName()}), ensuring that only one instance of a particular
 * service class may be registered at a time.
 * </p>
 */
public class CustomBlockServiceManager implements ServiceManager<String, CustomBlockService> {
    /**
     * Map of registered services, keyed by their class name.
     */
    private final Map<String, CustomBlockService> services = new HashMap<>();

    /**
     * {@inheritDoc}
     * <p>
     * In this implementation, the service is keyed by its class name.
     * Attempts to register another instance of the same class will result
     * in a {@link ServiceRegistrationException}.
     * </p>
     */
    @Override
    public void registerService(@NotNull CustomBlockService service) throws ServiceRegistrationException {
        Class<? extends CustomBlockService> serviceClass = service.getClass();
        String serviceClassName = serviceClass.getName();
        if (services.containsKey(serviceClassName)) {
            throw new ServiceRegistrationException("A service of this class has already been registered: " + serviceClassName);
        }

        services.put(serviceClassName, service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomBlockService unregisterService(@NotNull String serviceClassName) throws UnregisteredServiceException {
        CustomBlockService customBlockService = services.remove(serviceClassName);
        if (customBlockService == null) {
            throw new UnregisteredServiceException("Custom block service " + serviceClassName + " is not registered", serviceClassName);
        }
        return customBlockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomBlockService getService(@NotNull String serviceClassName) throws UnregisteredServiceException {
        CustomBlockService customBlockService = services.get(serviceClassName);
        if (customBlockService == null) {
            throw new UnregisteredServiceException("Custom block service " + serviceClassName + " is not registered", serviceClassName);
        }
        return customBlockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Collection<CustomBlockService> getServices() {
        return services.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isServiceRegistered(@NotNull String serviceClassName) {
        return services.containsKey(serviceClassName);
    }
}
