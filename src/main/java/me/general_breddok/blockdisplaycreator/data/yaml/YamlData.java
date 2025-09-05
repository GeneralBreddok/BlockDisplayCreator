package me.general_breddok.blockdisplaycreator.data.yaml;

import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.data.BukkitData;
import me.general_breddok.blockdisplaycreator.data.exception.ConfigurationDataTypeMismatchException;
import me.general_breddok.blockdisplaycreator.data.manager.ParameterizedClasses;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeStore;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.util.NumberUtil;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


/**
 * Represents data stored in a configuration file and provides methods to manipulate and access this data.
 *
 * @param <C> The type of data stored in the configuration.
 */
public class YamlData<C> extends BukkitData<YamlConfigFile, C, String> {

    private static final Logger log = LogManager.getLogger(YamlData.class);
    /**
     * The persistent data type manager instance.
     */
    private static PersistentDataTypeManager persistentDataTypeManager = new PersistentDataTypeStore();


    static {
        persistentDataTypeManager.register(TypeTokens.BOOLEAN, PersistentDataTypeStore.newPrimitivePersistentDataType(Boolean.class));
        persistentDataTypeManager.register(TypeTokens.LIST, PersistentDataTypeStore.newPrimitivePersistentDataType(List.class));
        persistentDataTypeManager.register(TypeTokens.STRING_OBJECT_MAP, PersistentDataTypeStore.newPersistentDataType(
                YamlDataClasses.SECTION,
                ParameterizedClasses.STRING_OBJECT_MAP,
                (complex, context) -> {
                    YamlConfiguration yamlConfiguration = new YamlConfiguration();

                    for (Map.Entry<String, Object> stringObjectEntry : complex.entrySet()) {
                        yamlConfiguration.set(stringObjectEntry.getKey(), stringObjectEntry.getValue());
                    }

                    return yamlConfiguration;
                },
                (primitive, context) -> primitive.getValues(true)
        ));

        persistentDataTypeManager.register(TypeTokens.STRING_SET, PersistentDataTypeStore.newPersistentDataType(
                List.class,
                ParameterizedClasses.STRING_SET,
                (complex, context) -> complex.stream().collect(OperationUtil.toArrayList()),
                (primitive, context) -> {
                    Set<String> result = new HashSet<>();
                    for (Object o : primitive) {
                        if (o instanceof String s) {
                            result.add(s);
                        }
                    }
                    return result;
                }
        ));

        persistentDataTypeManager.register(TypeTokens.VECTOR, PersistentDataTypeStore.newConfigurationPersistentDataType(Vector.class, (primitive, context) -> {
            double x = 0;
            double y = 0;
            double z = 0;

            try {
                x = ((Number) primitive.get("x")).doubleValue();

            } catch (ClassCastException | NullPointerException ignore) {
            }
            try {
                y = ((Number) primitive.get("y")).doubleValue();
            } catch (ClassCastException | NullPointerException ignore) {
            }
            try {
                z = ((Number) primitive.get("z")).doubleValue();
            } catch (ClassCastException | NullPointerException ignore) {
            }


            return new Vector(x, y, z);
        }));
        persistentDataTypeManager.register(TypeTokens.BLOCK_VECTOR, PersistentDataTypeStore.newConfigurationPersistentDataType(BlockVector.class, (primitive, context) -> BlockVector.deserialize(primitive.getValues(true))));
        persistentDataTypeManager.register(TypeTokens.ITEM_STACK, PersistentDataTypeStore.newConfigurationPersistentDataType(ItemStack.class, (primitive, context) -> ItemStack.deserialize(primitive.getValues(true))));
        persistentDataTypeManager.register(TypeTokens.COLOR, PersistentDataTypeStore.newConfigurationPersistentDataType(Color.class, (primitive, context) -> Color.deserialize(primitive.getValues(true))));
        persistentDataTypeManager.register(TypeTokens.LOCATION, PersistentDataTypeStore.newConfigurationPersistentDataType(Location.class, (primitive, context) -> Location.deserialize(primitive.getValues(true))));
        persistentDataTypeManager.register(TypeTokens.ATTRIBUTE_MODIFIER, PersistentDataTypeStore.newConfigurationPersistentDataType(AttributeModifier.class, (primitive, context) -> AttributeModifier.deserialize(primitive.getValues(true))));
        persistentDataTypeManager.register(TypeTokens.BOUNDING_BOX, PersistentDataTypeStore.newConfigurationPersistentDataType(BoundingBox.class, (primitive, context) -> BoundingBox.deserialize(primitive.getValues(true))));

        persistentDataTypeManager.register(TypeTokens.FIREWORK_EFFECT, PersistentDataTypeStore.newConfigurationPersistentDataType(FireworkEffect.class, (primitive, context) -> {
            FireworkEffect.Type type = FireworkEffect.Type.valueOf((String) primitive.get("type"));

            return FireworkEffect.builder()
                    .flicker((Boolean) primitive.get("flicker"))
                    .trail((Boolean) primitive.get("trail"))
                    .withColor((Iterable<?>) primitive.get("colors"))
                    .withFade((Iterable<?>) primitive.get("fade-colors"))
                    .with(type)
                    .build();
        }));

        persistentDataTypeManager.register(TypeTokens.STRING_LIST, PersistentDataTypeStore.newPersistentDataType(
                ArrayList.class,
                ParameterizedClasses.STRING_LIST,
                (complex, context) -> new ArrayList<>(complex),
                (primitive, context) -> {
                    ArrayList<String> result = new ArrayList<>(primitive.size());

                    for (Object o : primitive) {
                        result.add(String.valueOf(o));
                    }

                    return result;
                }
        ));
    }

    /**
     * Constructs a ConfigurationData object with the specified FileConfiguration and data type.
     *
     * @param configuration The FileConfiguration to use.
     * @param dataTypeToken The dataTypeToken of the data type to be stored.
     */
    public YamlData(YamlConfigFile configuration, @NotNull TypeToken<C> dataTypeToken) {
        super(configuration, dataTypeToken);
    }

    /**
     * Retrieves the persistent data type manager instance.
     *
     * @return The persistent data type manager.
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
    public void set(@NotNull String path, @NotNull C value) {
        Object primitive = getPDT().toPrimitive(value, ignore);

        dataSource.set(path, primitive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(@NotNull String path) {
        return dataSource.contains(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public C get(@NotNull String path) throws ConfigurationDataTypeMismatchException {
        Object result = dataSource.get(path);
        PersistentDataType<Object, C> pdt = getPDT();
        Class<Object> primitiveType = pdt.getPrimitiveType();

        if (result == null) {
            return null;
        }

        if (!(result instanceof String)) {
            if (result instanceof Number number) {
                if (NumberUtil.isNumberClass(primitiveType)) {
                    result = NumberUtil.convertToType(number, (Class<? extends Number>) (Class<?>) primitiveType);
                }
            }
            if (primitiveType.equals(String.class)) {
                result = String.valueOf(result);
            }
        }

        if (!(result instanceof List<?>) && primitiveType.isAssignableFrom(List.class)) {
            List<Object> list = new ArrayList<>();
            list.add(result);
            result = list;
        }

        if (!primitiveType.isAssignableFrom(result.getClass())) {
            throw new ConfigurationDataTypeMismatchException(String.format("Expected type: %s, but found: %s", primitiveType, result.getClass()), path, primitiveType, result);
        }

        return pdt.fromPrimitive(result, ignore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull String path) {
        dataSource.set(path, null);
    }
}

