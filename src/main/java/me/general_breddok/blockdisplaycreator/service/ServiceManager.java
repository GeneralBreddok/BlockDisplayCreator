package me.general_breddok.blockdisplaycreator.service;

import me.general_breddok.blockdisplaycreator.service.exception.ServiceRegistrationException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Generic service manager interface.
 * <p>
 * Provides registration, unregistration, lookup, and management
 * of services identified by a key.
 *
 * @param <K> the type of the key used to identify services
 * @param <S> the type of the managed service
 */
public interface ServiceManager<K, S> {

    /**
     * Registers a new service in the manager.
     *
     * @param service the service instance to register
     * @throws ServiceRegistrationException if registration fails
     */
    void registerService(@NotNull S service) throws ServiceRegistrationException;

    /**
     * Unregisters a service by its key.
     *
     * @param serviceKey the key of the service to remove
     * @return the removed service instance
     */
    S unregisterService(@NotNull K serviceKey);

    /**
     * Retrieves a service by its key.
     *
     * @param serviceKey the key of the service to look up
     * @return the service instance.
     */
    S getService(@NotNull K serviceKey);

    /**
     * Returns all currently registered services.
     *
     * @return a collection of registered service instances
     */
    @NotNull
    Collection<S> getServices();

    /**
     * Checks whether a service with the given key is registered.
     *
     * @param serviceKey the key to check
     * @return {@code true} if a service is registered for this key
     */
    boolean isServiceRegistered(@NotNull K serviceKey);
}
