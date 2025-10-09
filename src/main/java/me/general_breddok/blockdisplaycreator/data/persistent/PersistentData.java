package me.general_breddok.blockdisplaycreator.data.persistent;

import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.data.BukkitData;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeStore;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Represents persistent data stored in a PersistentDataContainer and provides methods to manipulate and access this data.
 *
 * @param <C> The type of data stored in the PersistentDataContainer.
 */
public class PersistentData<C> extends BukkitData<PersistentDataContainer, C, NamespacedKey> {

    /**
     * The persistent data type storage instance.
     */
    private static PersistentDataTypeManager persistentDataTypeManager = new PersistentDataTypeStore();

    static {
        persistentDataTypeManager.register(TypeTokens.PERSISTENT_DATA_CONTAINER_ARRAY, PersistentDataType.TAG_CONTAINER_ARRAY);
        persistentDataTypeManager.register(TypeTokens.PERSISTENT_DATA_CONTAINER, PersistentDataType.TAG_CONTAINER);
        persistentDataTypeManager.register(TypeTokens.BOOLEAN, PersistentDataType.BOOLEAN);
    }

    /**
     * Constructs a PersistentData object with the specified PersistentDataContainer and data type.
     *
     * @param pdc      The PersistentDataContainer to use.
     * @param dataTypeToken The type token of the data type to be stored.
     */
    public PersistentData(PersistentDataContainer pdc, @NotNull TypeToken<C> dataTypeToken) {
        super(pdc, dataTypeToken);
    }

    /**
     * Constructs a PersistentData object with the PersistentDataContainer retrieved from the specified PersistentDataHolder and data type.
     *
     * @param pdh      The PersistentDataHolder.
     * @param dataTypeToken The type token of the data type to be stored.
     */
    public PersistentData(PersistentDataHolder pdh, @NotNull TypeToken<C> dataTypeToken) {
        super(pdh.getPersistentDataContainer(), dataTypeToken);
        this.dataSource = pdh.getPersistentDataContainer();
    }

    /**
     * Retrieves the persistent data type store instance.
     *
     * @return The persistent data type store.
     */
    public static PersistentDataTypeManager getPersistentDataTypeStore() {
        return persistentDataTypeManager;
    }

    /**
     * Sets an instance of the persistent data type manager.
     *
     * @param pdtm New persistent data type manager.
     */
    public static void setPersistentDataTypeStore(PersistentDataTypeManager pdtm) {
        persistentDataTypeManager = pdtm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistentDataTypeManager getPersistentDataTypeManager() {
        return persistentDataTypeManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull NamespacedKey key, @NotNull C value) {
        dataSource.set(key, getPDT(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(@NotNull NamespacedKey key) {
        return dataSource.has(key, getPDT());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public C get(@NotNull NamespacedKey key) {
        PersistentDataType<Object, C> pdt = getPDT();
        Class<C> complexType = pdt.getComplexType();
        C result = dataSource.get(key, pdt);

        if (result == null && complexType.isArray()) {
            Class<?> componentType = complexType.getComponentType();

            PersistentDataType<Object, ?> arrayComponentPDT = persistentDataTypeManager.get(TypeToken.of(componentType));

            if (arrayComponentPDT == null) {
                return null;
            }

            Object singleton = dataSource.get(key, arrayComponentPDT);

            if (singleton == null) {
                return null;
            }

            try {
                result = (C) new Object[]{singleton};
            } catch (ClassCastException e) {
                return null;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull NamespacedKey key) {
        dataSource.remove(key);
    }
}

