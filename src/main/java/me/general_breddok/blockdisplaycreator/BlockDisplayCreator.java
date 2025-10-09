package me.general_breddok.blockdisplaycreator;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPISpigotConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.command.BlockDisplayCreatorSpigotCommand;
import me.general_breddok.blockdisplaycreator.command.capi.BlockDisplayCreatorCAPICommand;
import me.general_breddok.blockdisplaycreator.command.capi.CustomBlockGiveCAPICommand;
import me.general_breddok.blockdisplaycreator.common.BDCDependentPluginsManager;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockConfigStorage;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockService;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypeManager;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlData;
import me.general_breddok.blockdisplaycreator.file.config.value.BooleanConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.LongConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.StringMessagesValue;
import me.general_breddok.blockdisplaycreator.listener.block.BlockBreakListener;
import me.general_breddok.blockdisplaycreator.listener.block.BlockExplodeListener;
import me.general_breddok.blockdisplaycreator.listener.block.BlockPlaceListener;
import me.general_breddok.blockdisplaycreator.listener.block.piston.BlockPistonExtendListener;
import me.general_breddok.blockdisplaycreator.listener.block.piston.BlockPistonRetractListener;
import me.general_breddok.blockdisplaycreator.listener.entity.EntityDamageByEntityListener;
import me.general_breddok.blockdisplaycreator.listener.entity.EntityExplodeListener;
import me.general_breddok.blockdisplaycreator.listener.player.PlayerInteractEntityListener;
import me.general_breddok.blockdisplaycreator.listener.player.PlayerInteractListener;
import me.general_breddok.blockdisplaycreator.listener.skyblock.superior.SuperiorSkyblockPluginInitializeListener;
import me.general_breddok.blockdisplaycreator.metrics.BlockDisplayCreatorMetrics;
import me.general_breddok.blockdisplaycreator.metrics.PluginMetrics;
import me.general_breddok.blockdisplaycreator.service.CustomBlockServiceManager;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.sound.SimplePlayableSound;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.version.BDCUpdateChecker;
import me.general_breddok.blockdisplaycreator.version.VersionManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/**
 * Main entry point of the BlockDisplayCreator plugin.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BlockDisplayCreator extends JavaPlugin {
    /**
     * Singleton instance of the plugin.
     */
    @Getter
    private static BlockDisplayCreator instance;

    /**
     * Handles integration with external plugins and libraries.
     */
    BDCDependentPluginsManager dependentPluginsManager;

    /**
     * Manages registered custom block services.
     */
    ServiceManager<String, CustomBlockService> servicesManager;
    /**
     * Provides operations with custom blocks.
     */
    CustomBlockService customBlockService;

    /**
     * Main configuration file (config.yml).
     */
    YamlConfigFile yamlConfiguration;
    /**
     * Messages configuration file (messages.yml).
     */
    YamlConfigFile messagesFile;

    /**
     * Root command (/bdc or /blockdisplaycreator).
     */
    BlockDisplayCreatorCAPICommand bdcCommand;
    /**
     * Simplified specialized command for giving custom blocks (/cb or /custom-block).
     */
    CustomBlockGiveCAPICommand cbGiveCommand;

    /**
     * Detects and validates the running Minecraft server version.
     */
    VersionManager versionManager;
    /**
     * Indicates if CommandAPI is supported on the current server version.
     */
    boolean capi = false;

    /**
     * Collects anonymous plugin usage statistics.
     */
    PluginMetrics pluginMetrics;


    /**
     * Invoked during the plugin load phase, before the server has fully started.
     * <p>
     * Responsibilities:
     * <ul>
     *   <li>Initializes the {@link BDCDependentPluginsManager} for handling
     *       integrations with external plugins and libraries.</li>
     *   <li>Sets up {@link VersionManager} and determines whether CommandAPI
     *       can be used based on the server version.</li>
     *   <li>Prepares CommandAPI if supported, otherwise logs a fallback
     *       to legacy command handling.</li>
     *   <li>Registers WorldGuard flags and loads PacketEvents.</li>
     * </ul>
     * </p>
     */
    @Override
    public void onLoad() {
        this.dependentPluginsManager = new BDCDependentPluginsManager(this);

        this.versionManager = new VersionManager(this.getServer());

        if (!this.versionManager.isVersion1_19_4()) {
            this.capi = true;
            CommandAPI.onLoad(new CommandAPISpigotConfig(this).setNamespace("bdc"));
        } else {
            ChatUtil.log("&6[BlockDisplayCreator] &eCommandAPI is not supported on Minecraft 1.19.4 and below, using legacy commands.");
        }

        this.dependentPluginsManager.loadPacketEvents();

        this.dependentPluginsManager.registerWorldGuardFlags();
    }

    /**
     * Invoked when the plugin is enabled and ready to operate.
     * <p>
     * Responsibilities:
     * <ul>
     *   <li>Sets the static plugin instance reference.</li>
     *   <li>Initializes and registers the {@link CustomBlockService} via
     *       {@link ServiceManager}.</li>
     *   <li>Registers commands either via CommandAPI (modern versions)
     *       or via Bukkit's legacy executor (older versions).</li>
     *   <li>Registers all data adapters and event listeners.</li>
     *   <li>Loads and reloads configuration files (config.yml, messages.yml,
     *       and default custom block - barrel.yml).</li>
     *   <li>Initializes plugin metrics and performs an update check
     *       against SpigotMC.</li>
     * </ul>
     * </p>
     */
    @Override
    public void onEnable() {
        instance = this;
        this.dependentPluginsManager.initPacketEvents();

        this.customBlockService = new BDCCustomBlockService(new BDCCustomBlockConfigStorage(this));
        this.servicesManager = new CustomBlockServiceManager();
        this.servicesManager.registerService(this.customBlockService);

        if (this.capi) {
            CommandAPI.onEnable();

            CommandAPIBukkit.unregister("blockdisplaycreator", true, true);
            CommandAPIBukkit.unregister("bdc", true, true);

            this.bdcCommand = new BlockDisplayCreatorCAPICommand(this);
            this.bdcCommand.register();

            this.cbGiveCommand = new CustomBlockGiveCAPICommand(this);
            this.cbGiveCommand.register();
        } else {
            getCommand("blockdisplaycreator").setExecutor(new BlockDisplayCreatorSpigotCommand(this, this.customBlockService));
        }

        registerDataAdapters();

        this.yamlConfiguration = new YamlConfigFile(
                Path.of(getDataFolder().getPath(), "config.yml"),
                true,
                this.getResource("config.yml")
        );
        this.messagesFile = new YamlConfigFile(
                Path.of(getDataFolder().getPath(), "messages.yml"),
                true,
                this.getResource("messages.yml")
        );
        new YamlConfigFile(
                Path.of(getDataFolder().getPath(), "custom-blocks", "barrel.yml"),
                true,
                this.getResource("custom-blocks/barrel.yml")
        );

        reloadConfig();
        registerEvents();

        pluginMetrics = new BlockDisplayCreatorMetrics(this, 26735);
        checkForUpdates();
    }

    /**
     * Registers all plugin listeners to the Bukkit {@link PluginManager}.
     * <p>
     * Listeners cover player interactions, block placement and destruction,
     * entity damage, piston mechanics, and compatibility hooks with
     * other plugins (e.g. SuperiorSkyblock).
     * </p>
     */
    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(this.servicesManager), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(this.servicesManager), this);
        pluginManager.registerEvents(new BlockPlaceListener(), this);
        pluginManager.registerEvents(new PlayerInteractEntityListener(this.servicesManager), this);
        pluginManager.registerEvents(new BlockBreakListener(this.servicesManager), this);
        pluginManager.registerEvents(new BlockExplodeListener(), this);
        pluginManager.registerEvents(new EntityExplodeListener(), this);
        pluginManager.registerEvents(new BlockPistonExtendListener(), this);
        pluginManager.registerEvents(new BlockPistonRetractListener(), this);
        if (this.dependentPluginsManager.isSuperiorSkyblockAvailable()) {
            pluginManager.registerEvents(new SuperiorSkyblockPluginInitializeListener(), this);
        }
        //pluginManager.registerEvents(new PrepareItemCraftListener(), this);
    }

    /**
     * Registers data adapters for both Persistent Data Container (PDC)
     * and YAML-based storage.
     */
    private void registerDataAdapters() {
        ConfigurationSerialization.registerClass(SimplePlayableSound.class);

        PersistentDataTypeManager persistentStore = PersistentData.getPersistentDataTypeStore();
        persistentStore.register(TypeTokens.UUID, PersistentDataTypes.UUID);
        persistentStore.register(TypeTokens.CLASS, PersistentDataTypes.CLASS);
        persistentStore.register(TypeTokens.UUID_ARRAY, PersistentDataTypes.UUID_ARRAY);
        persistentStore.register(TypeTokens.LOCATION, PersistentDataTypes.LOCATION);
        persistentStore.register(TypeTokens.CUSTOM_BLOCK_ROTATION, PersistentDataTypes.CUSTOM_BLOCK_ROTATION);
        persistentStore.register(TypeTokens.COMMAND_ARRAY, PersistentDataTypes.COMMAND_ARRAY);


        PersistentDataTypeManager yamlStore = YamlData.getPersistentDataTypeStore();
        yamlStore.register(TypeTokens.ITEM_FLAG, PersistentDataTypes.ITEM_FLAG);
        yamlStore.register(TypeTokens.ITEM_FLAG_LIST, PersistentDataTypes.ITEM_FLAG_LIST);
        yamlStore.register(TypeTokens.PLAYABLE_SOUND, PersistentDataTypes.PLAYABLE_SOUND);
        yamlStore.register(TypeTokens.COMMAND_LIST, PersistentDataTypes.COMMAND_LIST);
        yamlStore.register(TypeTokens.ENCHANT_MAP, PersistentDataTypes.ENCHANT);
        yamlStore.register(TypeTokens.BILLBOARD, PersistentDataTypes.BILLBOARD);
        yamlStore.register(TypeTokens.BRIGHTNESS, PersistentDataTypes.BRIGHTNESS);
        yamlStore.register(TypeTokens.MATERIAL, PersistentDataTypes.MATERIAL);
        yamlStore.register(TypeTokens.SOUND, PersistentDataTypes.SOUND);
        yamlStore.register(TypeTokens.COMMAND_SOURCE, PersistentDataTypes.COMMAND_SOURCE);
        yamlStore.register(TypeTokens.COMMAND_BUNDLE, PersistentDataTypes.COMMAND_BUNDLE);
        yamlStore.register(TypeTokens.DYE_COLOR, PersistentDataTypes.DYE_COLOR);
        yamlStore.register(TypeTokens.DIRECTED_VECTOR, PersistentDataTypes.DIRECTED_VECTOR);
        yamlStore.register(TypeTokens.CUSTOM_BLOCK_PLACEMENT_MODE, PersistentDataTypes.CUSTOM_BLOCK_PLACEMENT_MODE);
        yamlStore.register(TypeTokens.CUSTOM_BLOCK_DROP_MODE, PersistentDataTypes.CUSTOM_BLOCK_DROP_MODE);
    }

    /**
     * Reloads all configuration files used by the plugin.
     * <p>
     * This includes the primary {@code config.yml}, the
     * {@code messages.yml}, and all custom block configurations.
     * It also re-initializes configuration-dependent values in memory.
     * </p>
     *
     * @see #initializeConfigValues()
     */
    @Override
    public void reloadConfig() {
        this.yamlConfiguration.reload();
        this.messagesFile.reload();
        this.customBlockService.getStorage().reloadAll();
        this.initializeConfigValues();
    }

    /**
     * Initializes static, strongly typed configuration values.
     * <p>
     * These values (boolean, long, and message strings) are exposed
     * via static fields, allowing global access to immutable
     * configuration data throughout the plugin.
     * </p>
     */
    public void initializeConfigValues() {
        new BooleanConfigValue().initialize(new YamlData<>(this.getYamlConfiguration(), TypeTokens.BOOLEAN));
        new LongConfigValue().initialize(new YamlData<>(this.getYamlConfiguration(), TypeTokens.LONG));
        new StringMessagesValue().initialize(new YamlData<>(this.getMessagesFile(), TypeTokens.STRING));
    }

    /**
     * Checks whether a newer version of the plugin is available on <a href="https://www.spigotmc.org/resources/blockdisplaycreator.114877/">SpigotMC</a>.
     * <p>
     * If a newer release is found, a notification message is sent to
     * the console.
     * </p>
     */
    public void checkForUpdates() {
        BDCUpdateChecker updateChecker = new BDCUpdateChecker(this, 114877);
        updateChecker.updateLastVersion(BDCUpdateChecker::sendNewUpdateMessage);
    }

    /**
     * Called when the plugin is disabled.
     * <p>
     * Performs clean shutdown operations, such as disabling
     * CommandAPI (if active) and terminating packet event handlers.
     * </p>
     */
    @Override
    public void onDisable() {
        if (this.capi) {
            CommandAPI.onDisable();
        }
        this.dependentPluginsManager.terminatePacketEvents();
    }
}
