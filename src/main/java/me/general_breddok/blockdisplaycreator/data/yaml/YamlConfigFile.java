package me.general_breddok.blockdisplaycreator.data.yaml;

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

public class YamlConfigFile extends CachedFile implements ConfigurationSection {
    private FileConfiguration config;
    private File configFile;


    public YamlConfigFile(@NotNull Path path,  boolean createNewFile, @Nullable InputStream inputStream) throws InvalidFileFormatException {
        super(path, createNewFile);
        if (inputStream != null) {
            loadFromStream(inputStream);
        }
    }

    public YamlConfigFile(@NotNull Path path,  boolean createNewFile) throws InvalidFileFormatException {
        super(path, createNewFile);
    }

    public YamlConfigFile(@NotNull Path path) throws InvalidFileFormatException {
        this(path, true);
    }

    private static boolean isYamlFormat(@NotNull File file) {
        return file.getName().endsWith(".yml") || file.getName().endsWith(".yaml");
    }

    public void set(@NotNull String path, @Nullable Object value) {
        if (value == null) {
            config.set(path, null);
        } else if (value instanceof Map<?, ?> map) {
            map.forEach((key, val) -> config.set(path + key, val));
        } else {
            config.set(path, value);
        }
    }

    private void loadConfiguration() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save " + config.getName() + " to " + configFile, e);
        }
    }

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

    @Override
    public void reload() {
        reload(false);
    }

    @Override
    public void reload(boolean loadNew) {
        if (!this.configFile.exists()) {
            if (loadNew) {
                load(this.configFile.toPath(), true);
            } else {
                throw new IllegalArgumentException("Configuration file does not exist: " + configFile.getAbsolutePath());
            }
        }
        loadConfiguration();
    }

    public void loadFromStream(@NotNull InputStream inputStream) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            FileConfiguration loadedConfig = YamlConfiguration.loadConfiguration(reader);
            updateConfig(this.config, loadedConfig);
            this.save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML configuration from InputStream", e);
        }
    }

    public boolean exists() {
        return configFile.exists();
    }

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

    public void fill(@NotNull ConfigurationSection content) {
        for (String key : content.getKeys(true)) {
            this.config.set(key, content.get(key));
        }

        this.save();
    }

    @NotNull
    @Override
    public String getName() {
        String fileName = configFile.getName();

        int dotIndex = fileName.lastIndexOf('.');

        return fileName.substring(0, dotIndex);
    }

    @Nullable
    @Override
    public Configuration getRoot() {
        return config.getRoot();
    }

    @Nullable
    @Override
    public ConfigurationSection getParent() {
        return config.getParent();
    }

    @Nullable
    @Override
    public Object get(@NotNull String path) {
        return config.get(path);
    }

    @Nullable
    @Override
    public Object get(@NotNull String path, @Nullable Object def) {
        return config.get(path, def);
    }

    @Nullable
    public <C> C get(@NotNull ConfigLocator<C> locator, @Nullable C def) {
        YamlData<C> yamlData = new YamlData<>(this, locator.getValueTypeToken());
        return yamlData.get(locator.getPath(), def);
    }

    @Nullable
    public <C> C get(@NotNull ConfigLocator<C> locator) {
        return this.get(locator, null);
    }

    @NotNull
    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }
    @NotNull
    @Override
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }
    @Override
    public boolean contains(@NotNull String path) {
        return config.contains(path);
    }
    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }
    @Override
    public boolean isSet(@NotNull String path) {
        return config.isSet(path);
    }
    @Nullable
    @Override
    public String getCurrentPath() {
        return config.getCurrentPath();
    }
    @NotNull
    @Override
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }
    @NotNull
    @Override
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }
    @Nullable
    @Override
    public String getString(@NotNull String path) {
        return config.getString(path);
    }
    @Nullable
    @Override
    public String getString(@NotNull String path, @Nullable String def) {
        return config.getString(path, def);
    }
    @Override
    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }
    @Override
    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }
    @Override
    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }
    @Override
    public boolean isInt(@NotNull String path) {
        return config.isInt(path);
    }
    @Override
    public boolean getBoolean(@NotNull String path) {
        return config.getBoolean(path);
    }
    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }
    @Override
    public boolean isBoolean(@NotNull String path) {
        return config.isBoolean(path);
    }
    @Override
    public double getDouble(@NotNull String path) {
        return config.getDouble(path);
    }
    @Override
    public double getDouble(@NotNull String path, double def) {
        return config.getDouble(path, def);
    }
    @Override
    public boolean isDouble(@NotNull String path) {
        return config.isDouble(path);
    }
    @Override
    public long getLong(@NotNull String path) {
        return config.getLong(path);
    }
    @Override
    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }
    @Override
    public boolean isLong(@NotNull String path) {
        return config.isLong(path);
    }
    @Nullable
    @Override
    public List<?> getList(@NotNull String path) {
        return config.getList(path);
    }
    @Nullable
    @Override
    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        return config.getList(path, def);
    }
    @Override
    public boolean isList(@NotNull String path) {
        return config.isList(path);
    }
    @NotNull
    @Override
    public List<String> getStringList(@NotNull String path) {
        return config.getStringList(path);
    }
    @NotNull
    @Override
    public List<Integer> getIntegerList(@NotNull String path) {
        return config.getIntegerList(path);
    }
    @NotNull
    @Override
    public List<Boolean> getBooleanList(@NotNull String path) {
        return config.getBooleanList(path);
    }
    @NotNull
    @Override
    public List<Double> getDoubleList(@NotNull String path) {
        return config.getDoubleList(path);
    }
    @NotNull
    @Override
    public List<Long> getLongList(@NotNull String path) {
        return config.getLongList(path);
    }
    @NotNull
    @Override
    public List<Byte> getByteList(@NotNull String path) {
        return config.getByteList(path);
    }
    @NotNull
    @Override
    public List<Character> getCharacterList(@NotNull String path) {
        return config.getCharacterList(path);
    }

    @NotNull
    @Override
    public List<Short> getShortList(@NotNull String path) {
        return config.getShortList(path);
    }

    @NotNull
    @Override
    public List<Float> getFloatList(@NotNull String path) {
        return config.getFloatList(path);
    }
    @NotNull
    @Override
    public List<Map<?, ?>> getMapList(@NotNull String path) {
        return config.getMapList(path);
    }
    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return config.isConfigurationSection(path);
    }

    @Nullable
    @Override
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        config.addDefault(path, value);
    }
    @Nullable
    @Override
    public ConfigurationSection getConfigurationSection(@NotNull String path) {
        return config.getConfigurationSection(path);
    }
    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        return config.getObject(path, clazz);
    }

    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return config.getObject(path, clazz, def);
    }

    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        return config.getSerializable(path, clazz);
    }

    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return config.getSerializable(path, clazz, def);
    }

    @Nullable
    @Override
    public Vector getVector(@NotNull String path) {
        return config.getVector(path);
    }

    @Nullable
    @Override
    public Vector getVector(@NotNull String path, @Nullable Vector def) {
        return config.getVector(path, def);
    }

    @Override
    public boolean isVector(@NotNull String path) {
        return config.isVector(path);
    }

    @Nullable
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull String path) {
        return config.getOfflinePlayer(path);
    }

    @Nullable
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def) {
        return config.getOfflinePlayer(path, def);
    }

    @Override
    public boolean isOfflinePlayer(@NotNull String path) {
        return config.isOfflinePlayer(path);
    }

    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String path) {
        return config.getItemStack(path);
    }

    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def) {
        return config.getItemStack(path, def);
    }

    @Override
    public boolean isItemStack(@NotNull String path) {
        return config.isItemStack(path);
    }

    @Nullable
    @Override
    public Color getColor(@NotNull String path) {
        return config.getColor(path);
    }

    @Nullable
    @Override
    public Color getColor(@NotNull String path, @Nullable Color def) {
        return config.getColor(path, def);
    }

    @Override
    public boolean isColor(@NotNull String path) {
        return config.isColor(path);
    }

    @Nullable
    @Override
    public Location getLocation(@NotNull String path) {
        return config.getLocation(path);
    }

    @Nullable
    @Override
    public Location getLocation(@NotNull String path, @Nullable Location def) {
        return config.getLocation(path, def);
    }

    @Override
    public boolean isLocation(@NotNull String path) {
        return config.isLocation(path);
    }

    @NotNull
    public List<String> getComments(@NotNull String path) {
        return config.getComments(path);
    }

    @NotNull
    public List<String> getInlineComments(@NotNull String path) {
        return config.getInlineComments(path);
    }

    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        config.setComments(path, comments);
    }

    public void setInlineComments(@NotNull String path, @Nullable List<String> inlineComments) {
        config.setInlineComments(path, inlineComments);
    }
}
