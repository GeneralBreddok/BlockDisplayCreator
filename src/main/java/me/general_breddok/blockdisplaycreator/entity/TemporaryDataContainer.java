package me.general_breddok.blockdisplaycreator.entity;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeStore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class TemporaryDataContainer {
    private static final PersistentDataTypeManager persistentDataTypeManager = new PersistentDataTypeStore();
    private final Map<NamespacedKey, Object> data = new HashMap<>();

    public <Z> void set(@NotNull NamespacedKey key, @NotNull Z value) {
        data.put(key, value);
    }

    public Object get(@NotNull NamespacedKey key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }

    @NotNull
    public Object getOrDefault(@NotNull NamespacedKey key, @NotNull Object defaultValue) {
        if (data.containsKey(key)) {
            return data.get(key);
        }

        return defaultValue;
    }

    @NotNull
    public Set<NamespacedKey> getKeys() {
        return data.keySet();
    }

    public boolean has(NamespacedKey key) {
        return data.containsKey(key);
    }

    public void remove(NamespacedKey key) {
        data.remove(key);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Map<NamespacedKey, Object> values() {
        return data;
    }

    public TemporaryDataContainer copy() {
        TemporaryDataContainer copy = new TemporaryDataContainer();
        copy.data.putAll(this.data);
        return copy;
    }

    public void apply(@NotNull Entity entity) {
        for (Map.Entry<NamespacedKey, Object> entry : data.entrySet()) {
            setPDT(entity, entry.getKey(), entry.getValue());
        }
    }

    private <T, Z> void setPDT(@NotNull Entity entity, @NotNull NamespacedKey key, @NotNull Z value) {
        PersistentDataType<Object, ?> persistentDataType = persistentDataTypeManager.get(TypeToken.of(value.getClass()));

        if (persistentDataType == null) {
            return;
        }

        entity.getPersistentDataContainer().set(key, (PersistentDataType<T, Z>) persistentDataType, value);
    }
}
