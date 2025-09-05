package me.general_breddok.blockdisplaycreator.data;


import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.data.exception.UnregisteredDataTypeException;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManagerProvider;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * This is an abstract class that provides a common interface for handling data in a Bukkit plugin.
 * It uses generics to allow for different types of data sources (S), data types (C), and keys (K).
 *
 * @param <S> The type of the data source.
 * @param <C> The type of the data to be stored.
 * @param <K> The type of the key used to identify the data.
 */
public abstract class BukkitData<S, C, K> implements PersistentDataTypeManagerProvider, Data<C, K> {

    /**
     * A context used when ignoring data adapter context.
     */
    protected static final PersistentDataAdapterContext ignore = () -> null;
    /**
     * The token representing the data type.
     */
    protected final TypeToken<C> dataTypeToken;
    /**
     * The data source.
     */
    protected S dataSource;

    /**
     * Constructs a BukkitData object with the specified data source and data type token.
     *
     * @param dataSource    The data source to be used.
     * @param dataTypeToken The token of the data type.
     * @throws UnregisteredDataTypeException If the data type is not registered with the data manager.
     */
    public BukkitData(@NotNull S dataSource, @NotNull TypeToken<C> dataTypeToken) {
        this.dataSource = dataSource;
        this.dataTypeToken = dataTypeToken;

        PersistentDataTypeManager manager = getPersistentDataTypeManager();
        if (manager == null) {
            throw new IllegalStateException("PersistentDataTypeManager is not initialized");
        }

        if (!manager.isRegistered(dataTypeToken)) {
            throw new UnregisteredDataTypeException(buildExceptionMessage(dataTypeToken, manager), dataTypeToken.getRawType());
        }
    }

    /**
     * Builds a detailed exception message for unregistered data types.
     *
     * @param dataTypeToken The unregistered data type token.
     * @param manager       The data type manager being used.
     * @return A detailed exception message.
     */
    private @NotNull String buildExceptionMessage(@NotNull TypeToken<C> dataTypeToken, @NotNull PersistentDataTypeManager manager) {
        return String.format("Type '%s' is not registered with '%s'",
                dataTypeToken.getType().getTypeName(),
                manager.getClass().getSimpleName());
    }

    /**
     * Retrieves the persistent data type associated with the data type.
     *
     * @param <P> The type of the primitive data.
     * @return The persistent data type.
     */
    protected final <P> PersistentDataType<P, C> getPDT() {
        return getPersistentDataTypeManager().get(dataTypeToken);
    }
}
