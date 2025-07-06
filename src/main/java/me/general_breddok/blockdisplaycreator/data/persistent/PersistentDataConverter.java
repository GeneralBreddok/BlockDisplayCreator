package me.general_breddok.blockdisplaycreator.data.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PersistentDataConverter<From, To> {
    To convert(From data, @NotNull PersistentDataAdapterContext context);
}
