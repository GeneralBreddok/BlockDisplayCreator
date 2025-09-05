package me.general_breddok.blockdisplaycreator.data.yaml;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.data.locator.ConfigLocator;
import me.general_breddok.blockdisplaycreator.file.CachedFile;
import me.general_breddok.blockdisplaycreator.file.exception.InvalidFileFormatException;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a YAML configuration file that extends the functionality of a cached file
 * and implements the {@link ConfigurationSection} interface.
 * <p>
 * This class provides methods to load, save, and manipulate YAML configuration data.
 * </p>
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YamlConfigFile extends CachedFile implements ConfigurationSection {
    FileConfiguration config;
    File configFile;

    /**
     * Constructs a YamlConfigFile object and loads the configuration from the specified path.
     * <p>
     * If the file does not exist, it will be created if createNewFile is true.
     * Additionally, if an InputStream is provided, the configuration will be loaded from it.
     *
     * @param path
     * @param createNewFile whether to create a new file if it does not exist
     * @param inputStream   an optional InputStream to load the configuration from
     * @throws InvalidFileFormatException
     */
    public YamlConfigFile(@NotNull Path path, boolean createNewFile, @Nullable InputStream inputStream) throws InvalidFileFormatException {
        super(path, createNewFile);
        if (inputStream != null) {
            loadFromStream(inputStream);
        }
    }

    /**
     * Constructs a YamlConfigFile object and loads the configuration from the specified path.
     * <p>
     * If the file does not exist, it will be created if createNewFile is true.
     *
     * @param path
     * @param createNewFile whether to create a new file if it does not exist
     * @throws InvalidFileFormatException
     */
    public YamlConfigFile(@NotNull Path path, boolean createNewFile) throws InvalidFileFormatException {
        super(path, createNewFile);
    }


    /**
     * Constructs a YamlConfigFile object and loads the configuration from the specified path.
     * <p>
     * If the file does not exist, it will be created.
     *
     * @param path
     * @throws InvalidFileFormatException
     */
    public YamlConfigFile(@NotNull Path path) throws InvalidFileFormatException {
        this(path, true);
    }

    /**
     * Check if the file is in YAML format based on its extension.
     *
     * @param file the file to check
     * @return true if the file has a .yml or .yaml extension, false otherwise
     */
    private static boolean isYamlFormat(@NotNull File file) {
        return file.getName().endsWith(".yml") || file.getName().endsWith(".yaml");
    }

    /**
     * Update the current configuration with values and comments from the default configuration.
     * This method adds any missing keys from the defaults to the current configuration
     * and updates comments and inline comments for existing keys.
     *
     * @param current  the current configuration to be updated
     * @param defaults the default configuration to copy values and comments from
     */
    public static void updateConfig(FileConfiguration current, FileConfiguration defaults) {
        Set<String> keys = defaults.getKeys(true);


        for (String key : keys) {
            if (!current.contains(key)) {
                current.set(key, defaults.get(key));
            }
            List<String> comments = defaults.getComments(key);
            if (!comments.isEmpty()) {
                current.setComments(key, comments);
            }
            List<String> inlineComments = defaults.getInlineComments(key);
            if (!inlineComments.isEmpty()) {
                current.set(key, inlineComments);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value == null) {
            config.set(path, null);
        } else if (value instanceof Map<?, ?> map) {
            map.forEach((key, val) -> config.set(path + key, val));
        } else {
            config.set(path, value);
        }
    }

    /**
     * Load the configuration from the file.
     */
    private void loadConfiguration() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save " + config.getName() + " to " + configFile, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load(@NotNull Path path, boolean createNewFile) {
        File file = path.toFile();

        if (!isYamlFormat(file)) {
            throw new InvalidFileFormatException("File is not a YAML configuration file.");
        }

        if (!file.exists()) {
            if (createNewFile) {
                try {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            } else {
                throw new IllegalArgumentException("Configuration file does not exist: " + configFile.getAbsolutePath());
            }
        }

        this.configFile = file;
        loadConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload() {
        reload(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload(boolean createNewIfNotExist) {
        if (!this.configFile.exists()) {
            if (createNewIfNotExist) {
                load(this.configFile.toPath(), true);
            } else {
                throw new IllegalArgumentException("Configuration file does not exist: " + configFile.getAbsolutePath());
            }
        }
        loadConfiguration();
    }

    /**
     * Load configuration from an InputStream.
     * This method reads the YAML configuration from the provided InputStream,
     * updates the current configuration with the loaded values, and saves the changes.
     *
     * @param inputStream the InputStream to read the YAML configuration from
     */
    public void loadFromStream(@NotNull InputStream inputStream) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            FileConfiguration loadedConfig = YamlConfiguration.loadConfiguration(reader);
            updateConfig(this.config, loadedConfig);
            this.save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML configuration from InputStream", e);
        }
    }

    /**
     * Check if the configuration file exists.
     *
     * @return true if the file exists, false otherwise
     */
    public boolean exists() {
        return configFile.exists();
    }

    /**
     * Get a file name without extension.
     *
     * @return file name without extension
     */
    @NotNull
    @Override
    public String getName() {
        String fileName = configFile.getName();

        int dotIndex = fileName.lastIndexOf('.');

        return fileName.substring(0, dotIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Configuration getRoot() {
        return config.getRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ConfigurationSection getParent() {
        return config.getParent();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object get(@NotNull String path) {
        return config.get(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object get(@NotNull String path, @Nullable Object def) {
        return config.get(path, def);
    }

    /**
     * Retrieves a configuration value using the specified locator.
     * If the value is not found, it returns the provided default value.
     *
     * @param locator the configuration locator
     * @param def     the default value to return if the configuration value is not found
     * @param <C>     the type of the configuration value
     * @return the configuration value, or the default value if not found
     */
    @Nullable
    public <C> C get(@NotNull ConfigLocator<C> locator, @Nullable C def) {
        YamlData<C> yamlData = new YamlData<>(this, locator.getValueTypeToken());
        return yamlData.get(locator.getPath(), def);
    }

    /**
     * Retrieves a configuration value using the specified locator.
     * If the value is not found, it returns null.
     *
     * @param locator the configuration locator
     * @param <C>     the type of the configuration value
     * @return the configuration value, or null if not found
     */
    @Nullable
    public <C> C get(@NotNull ConfigLocator<C> locator) {
        return this.get(locator, null);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull String path) {
        return config.contains(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSet(@NotNull String path) {
        return config.isSet(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getString(@NotNull String path) {
        return config.getString(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getString(@NotNull String path, @Nullable String def) {
        return config.getString(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInt(@NotNull String path) {
        return config.isInt(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull String path) {
        return config.getBoolean(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoolean(@NotNull String path) {
        return config.isBoolean(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull String path) {
        return config.getDouble(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(@NotNull String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDouble(@NotNull String path) {
        return config.isDouble(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull String path) {
        return config.getLong(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLong(@NotNull String path) {
        return config.isLong(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public List<?> getList(@NotNull String path) {
        return config.getList(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        return config.getList(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isList(@NotNull String path) {
        return config.isList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<String> getStringList(@NotNull String path) {
        return config.getStringList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Integer> getIntegerList(@NotNull String path) {
        return config.getIntegerList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Boolean> getBooleanList(@NotNull String path) {
        return config.getBooleanList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Double> getDoubleList(@NotNull String path) {
        return config.getDoubleList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Long> getLongList(@NotNull String path) {
        return config.getLongList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Byte> getByteList(@NotNull String path) {
        return config.getByteList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Character> getCharacterList(@NotNull String path) {
        return config.getCharacterList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Short> getShortList(@NotNull String path) {
        return config.getShortList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Float> getFloatList(@NotNull String path) {
        return config.getFloatList(path);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<Map<?, ?>> getMapList(@NotNull String path) {
        return config.getMapList(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return config.isConfigurationSection(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        config.addDefault(path, value);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ConfigurationSection getConfigurationSection(@NotNull String path) {
        return config.getConfigurationSection(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        return config.getObject(path, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return config.getObject(path, clazz, def);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        return config.getSerializable(path, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return config.getSerializable(path, clazz, def);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Vector getVector(@NotNull String path) {
        return config.getVector(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Vector getVector(@NotNull String path, @Nullable Vector def) {
        return config.getVector(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVector(@NotNull String path) {
        return config.isVector(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull String path) {
        return config.getOfflinePlayer(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def) {
        return config.getOfflinePlayer(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOfflinePlayer(@NotNull String path) {
        return config.isOfflinePlayer(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String path) {
        return config.getItemStack(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def) {
        return config.getItemStack(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemStack(@NotNull String path) {
        return config.isItemStack(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Color getColor(@NotNull String path) {
        return config.getColor(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Color getColor(@NotNull String path, @Nullable Color def) {
        return config.getColor(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColor(@NotNull String path) {
        return config.isColor(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Location getLocation(@NotNull String path) {
        return config.getLocation(path);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Location getLocation(@NotNull String path, @Nullable Location def) {
        return config.getLocation(path, def);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocation(@NotNull String path) {
        return config.isLocation(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getComments(@NotNull String path) {
        return config.getComments(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<String> getInlineComments(@NotNull String path) {
        return config.getInlineComments(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        config.setComments(path, comments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInlineComments(@NotNull String path, @Nullable List<String> inlineComments) {
        config.setInlineComments(path, inlineComments);
    }
}
