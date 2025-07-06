package me.general_breddok.blockdisplaycreator.data.meta;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeStore;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Represents metadata associated with a Metadatable object and provides methods to manipulate and access this metadata.
 *
 * @param <C> The type of data stored as metadata.
 */
public class MetaData<C> extends me.general_breddok.blockdisplaycreator.data.BukkitData<Metadatable, C, String> {

    /**
     * The persistent data type storage instance.
     */
    private static PersistentDataTypeManager persistentDataTypeManager = new PersistentDataTypeStore();

    /**
     * The owning plugin instance.
     */
    private static Plugin owningPlugin;

    /**
     * Constructs a MetaData object with the specified Metadatable and data type.
     *
     * @param metadatable The Metadatable object.
     * @param dataType    The class of the data type to be stored.
     */
    public MetaData(Metadatable metadatable, @NotNull TypeToken<C> dataTypeToken) {
        super(metadatable, dataTypeToken);
        Preconditions.checkArgument(owningPlugin != null, "owningPlugin is null!");
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
     * Retrieves the owning plugin instance.
     *
     * @return The owning plugin instance.
     */
    public static Plugin getOwningPlugin() {
        return owningPlugin;
    }

    /**
     * Sets the owning plugin instance.
     *
     * @param owningPlugin The owning plugin instance.
     */
    public static void setOwningPlugin(@NotNull Plugin owningPlugin) {
        MetaData.owningPlugin = owningPlugin;
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
    public void set(@NotNull String key, @NotNull C value) {
        if (has(key)) {
            remove(key);
        }
        dataSource.setMetadata(key, new FixedMetadataValue(owningPlugin, getPDT().toPrimitive(value, ignore)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(@NotNull String key) {
        return dataSource.hasMetadata(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public C get(@NotNull String key) {
        List<MetadataValue> metadata = dataSource.getMetadata(key);
        return metadata.isEmpty() ? null : getPDT().fromPrimitive(metadata.get(0).value(), ignore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull String key) {
        dataSource.removeMetadata(key, owningPlugin);
    }
}
