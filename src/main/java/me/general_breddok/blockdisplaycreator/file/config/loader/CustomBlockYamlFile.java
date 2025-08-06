package me.general_breddok.blockdisplaycreator.file.config.loader;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCFunctionCommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.SummonDisplayCommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.argument.NbtCommandArgument;
import me.general_breddok.blockdisplaycreator.commandparser.exception.CommandParseException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandNameException;
import me.general_breddok.blockdisplaycreator.common.ColorConverter;
import me.general_breddok.blockdisplaycreator.custom.*;
import me.general_breddok.blockdisplaycreator.custom.block.*;
import me.general_breddok.blockdisplaycreator.data.exception.ConfigurationDataTypeMismatchException;
import me.general_breddok.blockdisplaycreator.data.exception.IllegalEnumNameException;
import me.general_breddok.blockdisplaycreator.data.locator.ConfigLocator;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.entity.Summoner;
import me.general_breddok.blockdisplaycreator.entity.interaction.InteractionSummoner;
import me.general_breddok.blockdisplaycreator.entity.living.ShulkerSummoner;
import me.general_breddok.blockdisplaycreator.file.exception.CustomBlockLoadException;
import me.general_breddok.blockdisplaycreator.nbt.entity.display.NbtDisplay;
import me.general_breddok.blockdisplaycreator.nbt.entity.display.NbtDisplayObject;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.sound.ConfigurableSound;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.version.MinecraftVersion;
import me.general_breddok.blockdisplaycreator.version.VersionManager;
import me.general_breddok.blockdisplaycreator.web.bdengine.BDEngineModel;
import me.general_breddok.blockdisplaycreator.web.bdengine.NetworkBDEngineModel;
import me.general_breddok.blockdisplaycreator.web.exception.InvalidResponseException;
import me.general_breddok.blockdisplaycreator.web.player.PlayerProfiles;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class CustomBlockYamlFile implements CustomBlockConfigurationFile {
    private final YamlConfigFile file;

    public CustomBlockYamlFile(@NotNull YamlConfigFile file) {
        this.file = file;
    }

    public AbstractCustomBlock getAbstractCustomBlock() {
        if (this.getName().contains(" ")) {
            throw new CustomBlockLoadException("&cUnable to load block &6%s&r&c, file name must not contain spaces!", getName());
        }

        GroupSummoner<Display> displaySummoner = this.getDisplaySummoner();
        List<ConfiguredInteraction> configuredInteractions = this.getConfiguredInteractions();
        List<ConfiguredCollision> configuredCollisions = this.getConfiguredCollisions();
        ItemStack item = this.getItem();
        Material centralMaterial = this.getCentralMaterial();
        short sidesCount = this.getSidesCount();
        CustomBlockPermissions permissions = this.getCustomBlockPermissions();
        CustomBlockSoundGroup customBlockSoundGroup = this.getCustomBlockSoundGroup();
        CustomBlockStageSettings customBlockStageSettings = this.getCustomBlockStageSettings();


        if (WorldSelection.isEmptyBlock(centralMaterial) && configuredInteractions.isEmpty()  && configuredCollisions.isEmpty()) {
            configuredInteractions.add(new ConfiguredInteractionDta(
                    new InteractionSummoner(1.001f, 1.001f),
                    "interaction"
            ));
        }

        return new BDCAbstractCustomBlock(this.getName(), displaySummoner, configuredInteractions, configuredCollisions, item, centralMaterial, sidesCount, permissions, customBlockSoundGroup, customBlockStageSettings, getSaveSystem(), BDCCustomBlockService.class.getName());
    }

    @Override
    public void set(String key, Object value) {
        this.file.set(key, value);
        this.file.save();
    }

    @Override
    public Object get(String key) {
        return file.get(key);
    }

    public List<ConfiguredInteraction> getConfiguredInteractions() {
        List<ConfiguredInteraction> configuredInteractions = new ArrayList<>();

        YamlData<Float> floatYamlData = new YamlData<>(file, TypeTokens.FLOAT);
        YamlData<Vector> vectorYamlData = new YamlData<>(file, TypeTokens.VECTOR);

        ConfigurationSection interactionSection = file.getConfigurationSection(ParameterLocators.INTERACTION_PATH);

        if (interactionSection == null) {
            return new ArrayList<>();
        }

        Set<String> interactionNames = interactionSection.getValues(false).keySet();

        for (String interactionIdentifier : interactionNames) {
            String interactionPath = ParameterLocators.INTERACTION_PATH + "." + interactionIdentifier;

            if (!file.isConfigurationSection(interactionPath))
                continue;

            removeRightSection(interactionPath);

            float height;
            float width;
            Vector offset;

            try {
                height = floatYamlData.get(interactionPath + ".height", 1.001f);
            } catch (ConfigurationDataTypeMismatchException e) {
                height = 1.001f;
            }


            try {
                width = floatYamlData.get(interactionPath + ".width", 1.001f);
            } catch (ConfigurationDataTypeMismatchException e) {
                width = 1.001f;
            }

            try {
                offset = vectorYamlData.get(interactionPath + ".offset", null);
            } catch (ConfigurationDataTypeMismatchException e) {
                offset = null;
            }


            InteractionSummoner interactionSummoner = new InteractionSummoner(width, height);
            InteractionHandlerDta interactionHandlerDta = null;
            CommandBundle commandBundle = getCommandBundle(interactionPath);
            int cooldown = getCooldown(interactionPath);

            if (commandBundle != null) {
                interactionHandlerDta = new InteractionHandlerDta(commandBundle, cooldown);
            }

            configuredInteractions.add(
                    new ConfiguredInteractionDta(
                            interactionSummoner,
                            interactionIdentifier,
                            offset,
                            interactionHandlerDta
                    )
            );
        }

        return configuredInteractions;
    }

    public List<ConfiguredCollision> getConfiguredCollisions() {
        List<ConfiguredCollision> configuredCollisions = new ArrayList<>();

        ConfigurationSection collisionSection = file.getConfigurationSection(ParameterLocators.COLLISION_PATH);

        if (collisionSection == null) {
            return new ArrayList<>();
        }

        Set<String> collisionNames = collisionSection.getValues(false).keySet();

        YamlData<Double> doubleYamlData = new YamlData<>(file, TypeTokens.DOUBLE);
        YamlData<Vector> vectorYamlData = new YamlData<>(file, TypeTokens.VECTOR);
        YamlData<Boolean> booleanYamlData = new YamlData<>(file, TypeTokens.BOOLEAN);
        YamlData<DyeColor> colorYamlData = new YamlData<>(file, TypeTokens.DYE_COLOR);

        for (String collisionIdentifier : collisionNames) {
            String collisionPath = ParameterLocators.COLLISION_PATH + "." + collisionIdentifier;

            if (!file.isConfigurationSection(collisionPath))
                continue;

            double size;
            Vector offset;
            boolean invisible;
            DyeColor color;
            boolean disableBelow1_20_5;

            try {
                size = doubleYamlData.get(collisionPath + ".size", 0d);
            } catch (ConfigurationDataTypeMismatchException e) {
                size = 0;
            }

            try {
                offset = vectorYamlData.get(collisionPath + ".offset", null);
            } catch (ConfigurationDataTypeMismatchException e) {
                offset = null;
            }

            try {
                invisible = booleanYamlData.get(collisionPath + ".invisible", true);
            } catch (ConfigurationDataTypeMismatchException e) {
                invisible = false;
            }

            try {
                color = colorYamlData.get(collisionPath + ".color");
            } catch (ConfigurationDataTypeMismatchException | IllegalEnumNameException e) {
                color = null;
            }


            try {
                disableBelow1_20_5 = booleanYamlData.get(collisionPath + ".disable-below-1_20_5", false);
            } catch (ConfigurationDataTypeMismatchException e) {
                disableBelow1_20_5 = false;
            }

            ShulkerSummoner summoner = new ShulkerSummoner();
            summoner.setInvisible(invisible);
            summoner.setColor(color);
            configuredCollisions.add(new ConfiguredCollisionDta(summoner, collisionIdentifier, offset, size, disableBelow1_20_5));
        }

        return configuredCollisions;
    }


    private CommandBundle getCommandBundle(String path) {
        YamlData<CommandBundle> commandBundleYD = new YamlData<>(file, TypeTokens.COMMAND_BUNDLE);

        CommandBundle commandBundle;

        try {
            commandBundle = commandBundleYD.get(path);
        } catch (CommandParseException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter %s.command: &4%s&r&c is not a &7command!", getName(), path, e.getCommandLine());
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter %s.command-source: &4%s&r&c is not a &7CommandSource", getName(), path, e.getInvalidName());
        }

        return commandBundle;
    }

    private int getCooldown(String path) {
        YamlData<Integer> intYD = new YamlData<>(file, TypeTokens.INTEGER);

        int cooldown;

        try {
            String cooldownPath = path + ".cooldown";

            cooldown = intYD.get(cooldownPath, 0);
        } catch (ConfigurationDataTypeMismatchException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter %s.cooldown: &4%s&r&c is not a &7integer!", getName(), path, e.getReceivedValue());
        }

        return cooldown;
    }


    private String getVersionSpecificSpawnCommandPath() {
        VersionManager versionManager = BlockDisplayCreator.getInstance().getVersionManager();

        if (versionManager.isVersion1_19_4()) {
            return ParameterLocators.Display.SPAWN_COMMAND_1_19_4_PATH;
        } else if (versionManager.getCurrentVersion().isInRange(MinecraftVersion.V1_20, MinecraftVersion.V1_20_4)) {
            return ParameterLocators.Display.SPAWN_COMMAND_1_20_1_20_4_PATH;
        }
        return null;
    }


    public GroupSummoner<Display> getDisplaySummoner() {
        List<CommandLine> displaySummonCommands = new ArrayList<>();
        String valueName = ParameterLocators.Display.SPAWN_COMMAND_PATH;

        Object displayCommandsLink = file.get(ParameterLocators.Display.SPAWN_COMMAND_PATH);

        if (displayCommandsLink == null) {
            throw new CustomBlockLoadException(String.format("&cUnable to load block &6%s&r&c, parameter %s is not set", getName(), valueName));
        }

        String versionSpecificSpawnCommandPath = getVersionSpecificSpawnCommandPath();
        if (versionSpecificSpawnCommandPath != null) {
            Object versionSpecificDisplayCommandsLink = file.get(versionSpecificSpawnCommandPath);
            if (versionSpecificDisplayCommandsLink != null) {
                displayCommandsLink = versionSpecificDisplayCommandsLink;
                valueName = versionSpecificSpawnCommandPath;
            }
        }

        SpawnCommandFormat spawnCommandFormat = null;

        if (displayCommandsLink instanceof List<?> list) {
            for (Object o : list) {
                SpawnCommandFormat nextSpawnCommandFormat = processCommand(o, displaySummonCommands, valueName);
                if (spawnCommandFormat != null && spawnCommandFormat != nextSpawnCommandFormat) {
                    break;
                }
                spawnCommandFormat = nextSpawnCommandFormat;
            }
        } else {
            spawnCommandFormat = processCommand(displayCommandsLink, displaySummonCommands, valueName);
        }

        if (spawnCommandFormat == SpawnCommandFormat.UNKNOWN) {
            throw new CustomBlockLoadException(String.format("&cUnable to load block &6%s&r&c, parameter %s has an unknown format", getName(), valueName));
        }


        if (spawnCommandFormat == SpawnCommandFormat.SUMMON_COMMAND || spawnCommandFormat == SpawnCommandFormat.MODEL_ID) {
            List<SummonDisplayCommandLine> summonDisplayCommandLines = this.setDisplayParameters(displaySummonCommands.stream().map(SummonDisplayCommandLine.class::cast).collect(OperationUtil.toArrayList()));
            displaySummonCommands.clear();
            displaySummonCommands.addAll(summonDisplayCommandLines);
        }

        DirectedVector translation = file.get(ParameterLocators.Display.TRANSLATION, null);

        AutomaticCommandDisplaySummoner displaySummoner = new AutomaticCommandDisplaySummoner(displaySummonCommands, translation);

        displaySummoner.setUsePlaceholder(file.get(ParameterLocators.Display.USE_PLACEHOLDER, false));

        return displaySummoner;
    }


    private SpawnCommandFormat processCommand(Object command, List<CommandLine> displaySummonCommands, String valueName) {
        if (command instanceof String s) {
            if (s.trim().contains("function")) {
                MCFunctionCommandLine mcFunctionCommandLine = new MCFunctionCommandLine(s);

                try {
                    displaySummonCommands.add(mcFunctionCommandLine);

                } catch (CommandParseException e) {
                    throw new CustomBlockLoadException(e,
                            "&cUnable to load block &6%s&r&c, parameter %s: &4%s&r&c is not a &7command!",
                            getName(),
                            valueName,
                            e.getCommandLine());
                }

                return SpawnCommandFormat.CREATE_FUNCTION_COMMAND;

            } else if (s.trim().contains("summon")) {
                try {
                    displaySummonCommands.add(new SummonDisplayCommandLine(s));

                } catch (CommandParseException e) {
                    throw new CustomBlockLoadException(e,
                            "&cUnable to load block &6%s&r&c, parameter %s: &4%s&r&c is not a &7command!",
                            getName(),
                            valueName,
                            e.getCommandLine());
                }
                return SpawnCommandFormat.SUMMON_COMMAND;
            }
        } else if (command instanceof Integer modelId) {
            try {
                BDEngineModel model = new NetworkBDEngineModel(String.valueOf(modelId));

                displaySummonCommands.addAll(model.decodeCommands());
            } catch (InvalidResponseException | NullPointerException e) {
                throw new CustomBlockLoadException(e,
                        "&cUnable to load block &6%s&r&c, " + e.getMessage(),
                        getName()
                );
            } catch (InvalidCommandNameException e) {
                throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter %s: " + e.getMessage(), getName(), valueName);
            }
        }
        return SpawnCommandFormat.UNKNOWN;
    }

    public List<SummonDisplayCommandLine> setDisplayParameters(List<SummonDisplayCommandLine> displaySummonCommands) {
        Color glowColorOverride = this.getDisplayColor();

        boolean glowing = file.get(ParameterLocators.Display.GLOWING, false);
        Float viewRange = file.get(ParameterLocators.Display.VIEW_RANGE);
        Float shadowRadius = file.get(ParameterLocators.Display.SHADOW_RADIUS);
        Float shadowStrength = file.get(ParameterLocators.Display.SHADOW_STRENGTH);
        Display.Brightness brightness = file.get(ParameterLocators.Display.BRIGHTNESS);
        Display.Billboard billboard;
        try {
            billboard = file.get(ParameterLocators.Display.BILLBOARD);
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter display.billboard: is not a &3Billboard!", getName());
        }


        for (SummonDisplayCommandLine displaySummonCommand : displaySummonCommands) {
            NbtCommandArgument nbtArg = displaySummonCommand.getNbtArg();
            NbtDisplay nbtDisplay = new NbtDisplayObject(nbtArg.getContainer().asCompound());
            List<NbtDisplayObject> nbtPassengers = nbtDisplay.getNbtPassengers().stream().map(passenger -> new NbtDisplayObject(passenger.asCompound())).collect(OperationUtil.toArrayList());
            boolean changed = false;

            if (glowing) {
                nbtDisplay.setGlowing(true);
                nbtPassengers.forEach(passenger -> passenger.setGlowing(true));
                changed = true;
            }

            if (viewRange != null) {
                nbtDisplay.setViewRange(viewRange);
                nbtPassengers.forEach(passenger -> passenger.setViewRange(viewRange));
                changed = true;
            }

            if (shadowRadius != null) {
                nbtDisplay.setShadowRadius(shadowRadius);
                nbtPassengers.forEach(passenger -> passenger.setShadowRadius(shadowRadius));
                changed = true;
            }

            if (shadowStrength != null) {
                nbtDisplay.setShadowStrength(shadowRadius);
                nbtPassengers.forEach(passenger -> passenger.setShadowStrength(shadowStrength));
                changed = true;
            }

            if (brightness != null) {
                nbtDisplay.setBrightness(brightness);
                nbtPassengers.forEach(passenger -> passenger.setBrightness(brightness));
                changed = true;
            }

            if (billboard != null) {
                nbtDisplay.setBillboard(billboard);
                Display.Billboard finalBillboard = billboard;
                nbtPassengers.forEach(passenger -> passenger.setBillboard(finalBillboard));
                changed = true;
            }

            if (glowColorOverride != null) {
                nbtDisplay.setGlowColorOverride(glowColorOverride);
                nbtPassengers.forEach(passenger -> passenger.setGlowColorOverride(glowColorOverride));
                changed = true;
            }

            if (changed) {
                nbtDisplay.setNbtPassengers(nbtPassengers);
                nbtArg.setContainer(nbtDisplay);
                displaySummonCommand.setNbtArg(nbtArg);
            }
        }

        return displaySummonCommands;
    }

    public Color getDisplayColor() {
        Object abstractGco = this.file.get("display.glow-color-override");

        if (abstractGco != null) {
            if (abstractGco instanceof Integer intGco) {
                return ColorConverter.fromDecimalABGR(intGco);
            } else if (abstractGco instanceof String strGco) {
                if (ColorConverter.isNamedColor(strGco)) {
                    return ColorConverter.fromName(strGco);
                } else if (ColorConverter.isHex(strGco)) {
                    return ColorConverter.fromHex(strGco);
                }
            }
        }
        return null;
    }

    @Deprecated
    private void removeRightSection(String interactionPath) {
        String rightCommandSectionPath = interactionPath + ".right";
        ConfigurationSection rightCommandSection = file.getConfigurationSection(rightCommandSectionPath);


        if (rightCommandSection == null)
            return;

        Map<String, Object> values = rightCommandSection.getValues(true);
        for (Map.Entry<String, Object> value : values.entrySet()) {
            file.set(interactionPath + "." + value.getKey(), value.getValue());
        }
        file.set(rightCommandSectionPath, null);
        file.save();
    }

    public CustomBlockStageSettings getCustomBlockStageSettings() {
        CommandBundle placeCommandBundle;
        CommandBundle breakCommandBundle;

        placeCommandBundle = getCommandBundle(ParameterLocators.StageSettings.PLACE_COMMAND_BUNDLE.getPath());
        breakCommandBundle = getCommandBundle(ParameterLocators.StageSettings.BREAK_COMMAND_BUNDLE.getPath());

        return new BDCCustomBlockStageSettings(placeCommandBundle, breakCommandBundle);
    }

    public CustomBlockPermissions getCustomBlockPermissions() {
        Set<String> placePermissions = this.file.get(ParameterLocators.Permission.PLACE);
        Set<String> breakPermissions = this.file.get(ParameterLocators.Permission.BREAK);
        Set<String> interactPermissions = this.file.get(ParameterLocators.Permission.INTERACT);

        if (placePermissions == null) {
            placePermissions = new HashSet<>();
        }
        if (breakPermissions == null) {
            breakPermissions = new HashSet<>();
        }
        if (interactPermissions == null) {
            interactPermissions = new HashSet<>();
        }

        return new BDCCustomBlockPermissions(placePermissions, breakPermissions, interactPermissions);
    }


    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(getItemMaterial());

        if (itemStack.getItemMeta() == null) {
            throw new CustomBlockLoadException("&cUnable to load block &6%s&r&c, parameter item.material: item &4%s&r&c cannot be set to meta", getName(), itemStack.getType());
        }

        itemStack.setItemMeta(this.getItemMeta(itemStack));

        if (itemStack.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            PlayerProfile headOwnerProfile = this.getHeadOwnerProfile();

            if (headOwnerProfile != null) {
                skullMeta.setOwnerProfile(headOwnerProfile);

                itemStack.setItemMeta(skullMeta);
            }
        }

        return itemStack;
    }

    public PlayerProfile getHeadOwnerProfile() {
        String url = this.file.get(ParameterLocators.Item.HEAD_URL);
        String playerName = this.file.get(ParameterLocators.Item.HEAD_NAME);
        if (url != null) {
            if (PlayerProfiles.isBase64(url)) {
                return PlayerProfiles.getProfileByBase64(url);
            } else {
                return PlayerProfiles.getProfileByUrl(url);
            }
        } else if (playerName != null) {
            return PlayerProfiles.getProfileByName(playerName);
        }

        return null;
    }

    private ItemMeta getItemMeta(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        CustomBlockKey.holder(itemMeta)
                .setName(getName())
                .setServiceClass(BDCCustomBlockService.class.getName());

        String name = this.file.get(ParameterLocators.Item.NAME, getName());
        Map<Enchantment, Integer> enchantments = this.file.get(ParameterLocators.Item.ENCHANTMENTS);
        List<String> lore = this.file.get(ParameterLocators.Item.LORE);
        List<ItemFlag> itemFlags = this.file.get(ParameterLocators.Item.ITEM_FLAGS);

        OperationUtil.doIfNotNull(name, displayName -> itemMeta.setDisplayName(ChatUtil.color(displayName)));
        OperationUtil.doIfNotNull(lore, itemLore -> itemMeta.setLore(itemLore.stream().map(ChatUtil::color).toList()));
        OperationUtil.doIfNotNull(itemFlags, flags -> flags.forEach(itemMeta::addItemFlags));
        OperationUtil.doIfNotNull(enchantments, enchants -> enchants.forEach((enchant, level) -> itemMeta.addEnchant(enchant, level, false)));

        itemStack.setItemMeta(itemMeta);
        return itemMeta;
    }

    public Material getItemMaterial() {
        Material itemMaterial;

        try {
            itemMaterial = file.get(ParameterLocators.Item.MATERIAL);
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter item.material: &4%s&r&c is not a &7Material", getName(), e.getInvalidName());
        }

        if (itemMaterial == null) {
            throw new CustomBlockLoadException("&cUnable to load block &6%s&r&c, parameter item.material is not set", getName());
        }

        return itemMaterial;
    }

    public CustomBlockSoundGroup getCustomBlockSoundGroup() {
        ConfigurableSound placeSound;
        ConfigurableSound breakSound;

        try {
            placeSound = file.get(ParameterLocators.Sound.PLACE_SOUND);
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter sound.place.sound-type: &4%s&r&c is not a &3Sound", getName(), e.getInvalidName());
        }

        try {
            breakSound = file.get(ParameterLocators.Sound.BREAK_SOUND);
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, parameter sound.break.sound-type: &4%s&r&c is not a &3Sound", getName(), e.getInvalidName());
        }

        return new BDCCustomBlockSoundGroup(placeSound, breakSound);
    }

    public short getSidesCount() {
        Short sidesCount;

        try {
            sidesCount = file.get(ParameterLocators.SIDES_COUNT, (short) 4);
        } catch (ConfigurationDataTypeMismatchException e) {
            sidesCount = 4;
        }

        if (sidesCount < 1 || sidesCount > 360) {
            sidesCount = 360;
        }

        return sidesCount;
    }

    public Material getCentralMaterial() {
        Material centralMaterial;

        try {
            centralMaterial = file.get(ParameterLocators.CENTRAL_MATERIAL, Material.BARRIER);
        } catch (IllegalEnumNameException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, central-material: &4%s&r&c is not a &7Material", getName(), e.getInvalidName());
        } catch (ConfigurationDataTypeMismatchException e) {
            throw new CustomBlockLoadException(e, "&cUnable to load block &6%s&r&c, central-material: &4%s&r&c is not a &7Material", getName(), e.getReceivedValue());
        }

        if (!centralMaterial.isBlock()) {
            throw new CustomBlockLoadException("&cUnable to load block &6%s&r&c, central-material: &4%s&r&c is not a &8Block", getName(), centralMaterial);
        }

        return centralMaterial;
    }

    public String getSaveSystem() {
        return file.get(ParameterLocators.SAVE_SYSTEM, "yaml-file");
    }

    public boolean exists() {
        return file.exists();
    }

    public void saveConfiguration() {
        this.file.save();
    }

    @Override
    public void reload() {
        this.file.reload();
    }

    public String getPath() {
        return file.getPath().toString();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    private enum SpawnCommandFormat {
        SUMMON_COMMAND,
        MODEL_ID,
        CREATE_FUNCTION_COMMAND,
        UNKNOWN
    }

    @UtilityClass
    private class ParameterLocators {
        ConfigLocator<Material> CENTRAL_MATERIAL = new ConfigLocator<>(("central-material"), TypeTokens.MATERIAL);
        ConfigLocator<Short> SIDES_COUNT = new ConfigLocator<>("sides-count", TypeTokens.SHORT);
        ConfigLocator<String> SAVE_SYSTEM = new ConfigLocator<>("save-system", TypeTokens.STRING);

        String INTERACTION_PATH = "interactions";
        String COLLISION_PATH = "collisions";

        @UtilityClass
        class Sound {
            String SOUND_PATH = "sound";
            ConfigLocator<ConfigurableSound> BREAK_SOUND = new ConfigLocator<>(SOUND_PATH + ".break", TypeTokens.PLAYABLE_SOUND);
            ConfigLocator<ConfigurableSound> PLACE_SOUND = new ConfigLocator<>(SOUND_PATH + ".place", TypeTokens.PLAYABLE_SOUND);
        }

        @UtilityClass
        class Permission {
            String PERMISSION_PATH = "permission";

            ConfigLocator<Set<String>> PLACE = new ConfigLocator<>(PERMISSION_PATH + ".place", TypeTokens.STRING_SET);
            ConfigLocator<Set<String>> BREAK = new ConfigLocator<>(PERMISSION_PATH + ".break", TypeTokens.STRING_SET);
            ConfigLocator<Set<String>> INTERACT = new ConfigLocator<>(PERMISSION_PATH + ".interact", TypeTokens.STRING_SET);
        }


        @UtilityClass
        class Display {
            String DISPLAY_PATH = "display";
            String SPAWN_COMMAND_PATH = DISPLAY_PATH + ".spawn-command";
            String SPAWN_COMMAND_1_19_4_PATH = DISPLAY_PATH + ".spawn-command-1_19_4";
            String SPAWN_COMMAND_1_20_1_20_4_PATH = DISPLAY_PATH + ".spawn-command-1_20-1_20_4";
            ConfigLocator<Boolean> USE_PLACEHOLDER = new ConfigLocator<>(DISPLAY_PATH + ".use-placeholder", TypeTokens.BOOLEAN);
            ConfigLocator<Boolean> GLOWING = new ConfigLocator<>(DISPLAY_PATH + ".glowing", TypeTokens.BOOLEAN);
            ConfigLocator<Float> VIEW_RANGE = new ConfigLocator<>(DISPLAY_PATH + ".view-range", TypeTokens.FLOAT);
            ConfigLocator<DirectedVector> TRANSLATION = new ConfigLocator<>(DISPLAY_PATH + ".translation", TypeTokens.DIRECTED_VECTOR);
            ConfigLocator<org.bukkit.entity.Display.Billboard> BILLBOARD = new ConfigLocator<>(DISPLAY_PATH + ".billboard", TypeTokens.BILLBOARD);
            ConfigLocator<org.bukkit.entity.Display.Brightness> BRIGHTNESS = new ConfigLocator<>(DISPLAY_PATH + ".brightness", TypeTokens.BRIGHTNESS);
            ConfigLocator<Float> SHADOW_RADIUS = new ConfigLocator<>(DISPLAY_PATH + ".shadow-radius", TypeTokens.FLOAT);
            ConfigLocator<Float> SHADOW_STRENGTH = new ConfigLocator<>(DISPLAY_PATH + ".shadow-strength", TypeTokens.FLOAT);
        }

        @UtilityClass
        class Item {
            String ITEM_PATH = "item";
            ConfigLocator<Material> MATERIAL = new ConfigLocator<>(ITEM_PATH + ".material", TypeTokens.MATERIAL);
            ConfigLocator<String> NAME = new ConfigLocator<>(ITEM_PATH + ".name", TypeTokens.STRING);
            ConfigLocator<Map<Enchantment, Integer>> ENCHANTMENTS = new ConfigLocator<>(ITEM_PATH + ".enchantments", TypeTokens.ENCHANT_MAP);
            ConfigLocator<List<ItemFlag>> ITEM_FLAGS = new ConfigLocator<>(ITEM_PATH + ".item-flags", TypeTokens.ITEM_FLAG_LIST);
            ConfigLocator<List<String>> LORE = new ConfigLocator<>(ITEM_PATH + ".lore", TypeTokens.STRING_LIST);
            ConfigLocator<String> HEAD_URL = new ConfigLocator<>(ITEM_PATH + ".skullmeta.url", TypeTokens.STRING);
            ConfigLocator<String> HEAD_NAME = new ConfigLocator<>(ITEM_PATH + ".skullmeta.name", TypeTokens.STRING);
        }

        @UtilityClass
        class StageSettings {

            String STAGE_SETTINGS_PATH = "stage-settings";

            ConfigLocator<CommandBundle> PLACE_COMMAND_BUNDLE = new ConfigLocator<>(STAGE_SETTINGS_PATH + ".place", TypeTokens.COMMAND_BUNDLE);
            ConfigLocator<CommandBundle> BREAK_COMMAND_BUNDLE = new ConfigLocator<>(STAGE_SETTINGS_PATH + ".break", TypeTokens.COMMAND_BUNDLE);
        }
    }
}
