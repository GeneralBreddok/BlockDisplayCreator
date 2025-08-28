package me.general_breddok.blockdisplaycreator;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
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
import me.general_breddok.blockdisplaycreator.listener.skyblock.SkyblockPluginInitializeListener;
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

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BlockDisplayCreator extends JavaPlugin {
    @Getter
    private static BlockDisplayCreator instance;

    BDCDependentPluginsManager dependentPluginsManager;

    ServiceManager<String, CustomBlockService> servicesManager;
    CustomBlockService customBlockService;

    YamlConfigFile yamlConfiguration;
    YamlConfigFile messagesFile;

    BlockDisplayCreatorCAPICommand bdcCommand;
    CustomBlockGiveCAPICommand cbGiveCommand;

    VersionManager versionManager;
    boolean capi = false;

    PluginMetrics pluginMetrics;



    @Override
    public void onLoad() {
        this.dependentPluginsManager = new BDCDependentPluginsManager(this);

        this.versionManager = new VersionManager(this.getServer());

        if (!this.versionManager.isVersion1_19_4()) {
            this.capi = true;
            CommandAPI.onLoad(new CommandAPIBukkitConfig(this).setNamespace("bdc"));
        } else {
            ChatUtil.log("&6[BlockDisplayCreator] &eCommandAPI is not supported on Minecraft 1.19.4 and below, using legacy commands.");
        }

        this.dependentPluginsManager.loadPacketEvents();

        this.dependentPluginsManager.registerWorldGuardFlags();
    }


    @Override
    public void onEnable() {
        this.instance = this;
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
            pluginManager.registerEvents(new SkyblockPluginInitializeListener(), this);
        }
        //pluginManager.registerEvents(new PrepareItemCraftListener(), this);
    }


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
    }

    @Override
    public void reloadConfig() {
        this.yamlConfiguration.reload();
        this.messagesFile.reload();
        this.customBlockService.getStorage().reloadAll();
        this.initializeConfigValues();
    }

    public void initializeConfigValues() {
        new BooleanConfigValue().initialize(new YamlData<>(this.getYamlConfiguration(), TypeTokens.BOOLEAN));
        new LongConfigValue().initialize(new YamlData<>(this.getYamlConfiguration(), TypeTokens.LONG));
        new StringMessagesValue().initialize(new YamlData<>(this.getMessagesFile(), TypeTokens.STRING));
    }

    public void checkForUpdates() {
        BDCUpdateChecker updateChecker = new BDCUpdateChecker(this, 114877);
        updateChecker.updateLastVersion(BDCUpdateChecker::sendNewUpdateMessage);
    }

    @Override
    public void onDisable() {
        if (this.capi) {
            CommandAPI.onDisable();
        }
        this.dependentPluginsManager.terminatePacketEvents();
    }
}
