package me.general_breddok.blockdisplaycreator.data.manager;

import com.google.common.reflect.TypeToken;
import org.bukkit.persistence.PersistentDataType;

/**
 * Interface for managing persistent data types in a Bukkit plugin.
 */
public interface PersistentDataTypeManager {

    /**
     * Registers a persistent data type with its corresponding class.
     *
     * @param <P>   The type of the persistent data.
     * @param <C>   The type of the class.
     * @param clazz The class to register.
     * @param pdt   The persistent data type to associate with the class.
     */
    <P, C> void register(TypeToken<C> typeToken, PersistentDataType<P, C> pdt);

    /**
     * Checks if a persistent data type is already registered for a given class.
     *
     * @param <C>   The type of the class.
     * @param clazz The class to check.
     * @return True if the class is registered, false otherwise.
     */
    <C> boolean isRegistered(TypeToken<C> typeToken);

    /**
     * Unregisters a persistent data type with its corresponding class.
     *
     * @param <C>   The type of the class.
     * @param clazz The class to unregister.
     */
    <P, C> PersistentDataType<P, C> unregister(TypeToken<C> typeToken);

    /**
     * Retrieves the persistent data type associated with a given class.
     *
     * @param <P>   The type of the persistent data.
     * @param <C>   The type of the class.
     * @param clazz The class to retrieve the persistent data type for.
     * @return The persistent data type associated with the class, or null if not found.
     */
    <P, C> PersistentDataType<P, C> get(TypeToken<C> typeToken);
}
