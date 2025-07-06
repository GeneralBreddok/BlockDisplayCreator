package me.general_breddok.blockdisplaycreator.data.locator;

import com.google.common.reflect.TypeToken;
import org.bukkit.NamespacedKey;

public class PersistentDataLocator<T> extends DataLocator<NamespacedKey, T> {
    public PersistentDataLocator(NamespacedKey key, TypeToken<T> valueTypeToken) {
        super(key, valueTypeToken);
    }
}
