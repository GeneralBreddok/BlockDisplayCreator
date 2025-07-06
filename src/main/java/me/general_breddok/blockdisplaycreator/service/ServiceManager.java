package me.general_breddok.blockdisplaycreator.service;

import me.general_breddok.blockdisplaycreator.service.exception.ServiceRegistrationException;
import org.bukkit.block.data.type.LightningRod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ServiceManager<K, S> {
    void registerService(@NotNull S service) throws ServiceRegistrationException;

    @Nullable
    S unregisterService(@NotNull K serviceKey);

    @Nullable
    S getService(@NotNull K serviceKey);

    @NotNull
    Collection<S> getServices();

    boolean isServiceRegistered(@NotNull K serviceKey);
}
