package me.general_breddok.blockdisplaycreator.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Data<C, K> {

    /**
     * Sets the value associated with the specified key.
     *
     * @param key   The key to identify the data.
     * @param value The data to be stored.
     */
    void set(@NotNull K key, @NotNull C value);

    /**
     * Checks if data exists for the given key.
     *
     * @param key The key to identify the data.
     * @return True if data exists for the key, false otherwise.
     */
    boolean has(@NotNull K key);

    /**
     * Retrieves the data associated with the given key.
     *
     * @param key The key to identify the data.
     * @return The data associated with the key, or null if no data exists.
     */
    @Nullable
    C get(@NotNull K key);

    /**
     * Removes the element associated with the given key.
     *
     * @param key The key that identifies the element to be removed.
     */
    void remove(@NotNull K key);

    /**
     * Retrieves the data associated with the given key, or returns a default value if no data exists.
     *
     * @param key The key to identify the data.
     * @param def The default value to be returned if no data exists.
     * @return The data associated with the key, or the default value if no data exists.
     */
    @Nullable
    default C get(@NotNull K key, @Nullable C def) {
        C result = get(key);
        return result == null ? def : result;
    }
}
