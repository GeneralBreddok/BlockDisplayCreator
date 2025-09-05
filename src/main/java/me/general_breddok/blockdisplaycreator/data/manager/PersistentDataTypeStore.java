package me.general_breddok.blockdisplaycreator.data.manager;

import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.data.exception.IllegalEnumNameException;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentDataConverter;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlDataClasses;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class manages different types of data used in the plugin.
 * It provides methods to register, check, and retrieve PersistentDataType instances.
 */
public class PersistentDataTypeStore implements PersistentDataTypeManager {

    /**
     * A map to store registered PersistentDataType instances.
     * The key is the class of the complex type, and the value is the corresponding PersistentDataType instance.
     */
    private final Map<TypeToken<?>, PersistentDataType<?, ?>> registeredPDT = new HashMap<>();

    {
        register(TypeTokens.STRING, PersistentDataType.STRING);

        register(TypeTokens.BOOLEAN, newPrimitivePersistentDataType(Boolean.class));

        register(TypeTokens.BYTE, PersistentDataType.BYTE);
        register(TypeTokens.SHORT, PersistentDataType.SHORT);
        register(TypeTokens.INTEGER, PersistentDataType.INTEGER);
        register(TypeTokens.LONG, PersistentDataType.LONG);

        register(TypeTokens.FLOAT, PersistentDataType.FLOAT);
        register(TypeTokens.DOUBLE, PersistentDataType.DOUBLE);

        register(TypeTokens.BYTE_ARRAY, PersistentDataType.BYTE_ARRAY);
        register(TypeTokens.SHORT_ARRAY, newPrimitivePersistentDataType(short[].class));
        register(TypeTokens.INTEGER_ARRAY, PersistentDataType.INTEGER_ARRAY);
        register(TypeTokens.LONG_ARRAY, PersistentDataType.LONG_ARRAY);
        register(TypeTokens.STRING_ARRAY, newPrimitivePersistentDataType(String[].class));
    }

    /**
     * A static method to create a new PersistentDataType instance for same types.
     *
     * @param type The class of the primitive type.
     * @return A new PersistentDataType instance for the give type.
     */
    public static <C> PersistentDataType<C, C> newPrimitivePersistentDataType(@NotNull Class<C> type) {
        return newPersistentDataType(type, type, (complex, context) -> complex, (primitive, context) -> primitive);
    }

    public static <C> PersistentDataType<String, C> newStringPersistentDataType(@NotNull Class<C> type, @NotNull PersistentDataConverter<String, C> fromPrimitive) {
        return newPersistentDataType(String.class, type, (c, d) -> c.toString(), fromPrimitive);
    }

    public static <C extends Enum<C>> PersistentDataType<String, C> newEnumPersistentDataType(Class<C> enumClass) {
        return newPersistentDataType(
                String.class,
                enumClass,
                (complex, context) -> complex.toString(),
                (primitive, context) -> {
                    try {
                        return Enum.valueOf(enumClass, primitive.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalEnumNameException(primitive + " is not a " + enumClass.getSimpleName() + " enum constant", e, (Class<Enum<? extends Enum<?>>>) enumClass, primitive);
                    }
                });
    }

    public static <C extends ConfigurationSerializable> PersistentDataType<MemorySection, C> newConfigurationPersistentDataType(@NotNull Class<C> complexClass, @NotNull PersistentDataConverter<MemorySection, C> fromPrimitive) {
        return newPersistentDataType(
                YamlDataClasses.SECTION,
                complexClass,
                (complex, context) -> {
                    YamlConfiguration yamlConfiguration = new YamlConfiguration();

                    complex.serialize().forEach(yamlConfiguration::set);

                    return yamlConfiguration;
                },
                fromPrimitive
        );
    }

    public static <C extends List<?>> PersistentDataType<ArrayList<?>, C> newListPersistentDataType(@NotNull Class<C> complexClass, @NotNull PersistentDataConverter<ArrayList<?>, C> fromPrimitive) {
        return newPersistentDataType(
                YamlDataClasses.LIST,
                complexClass,
                (complex, context) -> new ArrayList<>(complex),
                fromPrimitive
        );
    }

    public static <P, C> PersistentDataType<P, C> newPersistentDataType(@NotNull Class<P> primitiveClass, @NotNull Class<C> complexClass, @NotNull PersistentDataConverter<C, P> toPrimitive, @NotNull PersistentDataConverter<P, C> fromPrimitive) {
        return new PersistentDataType<>() {
            @NotNull
            @Override
            public Class<P> getPrimitiveType() {
                return primitiveClass;
            }

            @NotNull
            @Override
            public Class<C> getComplexType() {
                return complexClass;
            }

            @NotNull
            @Override
            public P toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
                return toPrimitive.convert(complex, context);
            }

            @NotNull
            @Override
            public C fromPrimitive(@NotNull P primitive, @NotNull PersistentDataAdapterContext context) {
                return fromPrimitive.convert(primitive, context);
            }
        };
    }

    /**
     * Registers a new PersistentDataType instance for a given class.
     *
     * @param typeToken The typeToken of the complex type.
     * @param pdt       The PersistentDataType instance to register.
     * @param <P>       The type of the primitive type.
     * @param <C>       The type of the complex type.
     */
    @Override
    public <P, C> void register(TypeToken<C> typeToken, PersistentDataType<P, C> pdt) {
        registeredPDT.put(typeToken, pdt);
    }

    /**
     * Checks if a PersistentDataType instance is registered for a given class.
     *
     * @param typeToken The typeToken to check.
     * @param <C>       The type of the complex type.
     * @return True if a PersistentDataType instance is registered for the given class, false otherwise.
     */
    @Override
    public <C> boolean isRegistered(TypeToken<C> typeToken) {
        return registeredPDT.containsKey(typeToken);
    }

    @Override
    public <P, C> PersistentDataType<P, C> unregister(TypeToken<C> typeToken) {
        PersistentDataType<?, ?> remove = registeredPDT.remove(typeToken);
        return remove == null ? null : (PersistentDataType<P, C>) remove;
    }

    /**
     * Retrieves a registered PersistentDataType instance for a given class.
     *
     * @param typeToken The typeToken to retrieve the PersistentDataType instance for.
     * @param <P>       The type of the primitive type.
     * @param <C>       The type of the complex type.
     * @return The registered PersistentDataType instance for the given class, or null if no instance is registered.
     */
    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <P, C> PersistentDataType<P, C> get(TypeToken<C> typeToken) {
        if (typeToken == null) {
            return null;
        }
        PersistentDataType<?, ?> result = registeredPDT.get(typeToken);
        if (result == null) {
            return null;
        } else {
            try {
                return (PersistentDataType<P, C>) result;
            } catch (ClassCastException e) {
                return null;
            }
        }
    }

    /**
     * Retrieves the set of tokens for which PersistentDataType instances are registered.
     *
     * @return The set of tokens for which PersistentDataType instances are registered.
     */
    public Set<TypeToken<?>> getTokens() {
        return registeredPDT.keySet();
    }
}
