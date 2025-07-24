package me.general_breddok.blockdisplaycreator.command.capi;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.Tooltip;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.command.capi.tooltip.AbstractCustomBlockTooltip;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.common.ColorConverter;
import me.general_breddok.blockdisplaycreator.custom.*;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.BDCCustomBlockConfigStorage;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockStorage;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockConfigurationFile;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.PlayerSkinBase64Placeholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.util.NumberUtil;
import me.general_breddok.blockdisplaycreator.version.VersionCompat;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockDisplayCreatorCAPICommand {
    final BlockDisplayCreator plugin;
    @Getter
    List<AbstractCustomBlockTooltip> abstractCustomBlockTooltips = new ArrayList<>();
    Map<String, List<String>> interactionSuggestions = new HashMap<>();
    Map<String, List<String>> collisionSuggestions = new HashMap<>();

    public BlockDisplayCreatorCAPICommand(BlockDisplayCreator plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandTree("blockdisplaycreator")
                .withAliases("bdc")
                .withShortDescription("Basic BlockDisplayCreator commands")
                .withFullDescription("BlockDisplayCreator commands for managing custom blocks and their configurations.")
                .withUsage(
                        "/bdc custom-block {give|editfile} <block> ...",
                        "/bdc reload [block]",
                        "/bdc killcbentities <position1> <position2>"
                )
                .then(
                        new LiteralArgument("reload")
                                .withPermission(DefaultPermissions.BDC.Command.RELOAD)
                                .then(
                                        new StringArgument("block")
                                                .replaceSuggestions(getCustomBlockSuggestions())
                                                .setOptional(true)
                                                .executes((sender, args) -> {
                                                    String block = (String) args.get("block");

                                                    if (block != null) {
                                                        CustomBlockStorage storage = this.plugin.getCustomBlockService().getStorage();
                                                        if (storage.getNames().contains(block)) {
                                                            storage.reload(block);
                                                            ChatUtil.sendMessage(sender, "&a%s block has been reloaded!", block);
                                                        } else {
                                                            ChatUtil.sendMessage(sender, "&cBlock %s does not exist!", block);
                                                        }
                                                    } else {
                                                        this.plugin.reloadConfig();
                                                        ChatUtil.sendMessage(sender, "&aConfig has been reloaded!");
                                                    }
                                                })
                                )
                ).then(
                        new LiteralArgument("killcbentities")
                                .withPermission(DefaultPermissions.BDC.Command.KILL_CB_ENTITIES)
                                .then(
                                        new LocationArgument("position1")
                                                .then(
                                                        new LocationArgument("position2")
                                                                .executesPlayer((sender, args) -> {
                                                                    Location position1 = (Location) args.get("position1");
                                                                    Location position2 = (Location) args.get("position2");

                                                                    killBlockEntities(BoundingBox.of(position1, position2), sender);
                                                                })
                                                )
                                )
                ).then(
                        new LiteralArgument("custom-block")
                                .withPermission(DefaultPermissions.BDC.Command.CUSTOM_BLOCK)
                                .then(
                                        new LiteralArgument("give")
                                                .then(
                                                        new TextArgument("block")
                                                                .replaceSuggestions(getCustomBlockSuggestions())
                                                                .then(
                                                                        new EntitySelectorArgument.ManyPlayers("receiver")
                                                                                .setOptional(true)
                                                                                .then(
                                                                                        new IntegerArgument("amount", 1, 127)
                                                                                                .setOptional(true)
                                                                                                .executes((sender, args) -> {
                                                                                                    String block = (String) args.get("block");
                                                                                                    int amount = (int) args.getOrDefault("amount", 1);

                                                                                                    Collection<Player> receivers = (Collection<Player>) args.get("receiver");

                                                                                                    CustomBlockStorage storage = this.plugin.getCustomBlockService().getStorage();

                                                                                                    if (!storage.getNames().contains(block)) {
                                                                                                        ChatUtil.sendMessage(sender, "&cBlock %s does not exist!", block);
                                                                                                        return;
                                                                                                    }

                                                                                                    AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(block);

                                                                                                    ItemStack item = abstractCustomBlock.getItem();
                                                                                                    item.setAmount(amount);

                                                                                                    if (receivers == null) {
                                                                                                        if (sender instanceof Player player) {

                                                                                                            applyPlaceholdersForItem(item, player);
                                                                                                            applyPlaceholdersForCommand(abstractCustomBlock, item, player);

                                                                                                            ItemUtil.distributeItem(player, item);
                                                                                                            ChatUtil.sendMessage(player, "&bYou have received the &l%s&ox%s&r&b block", block, amount);
                                                                                                        } else {
                                                                                                            throw CommandAPI.failWithString("You have not specified the block receiver!");
                                                                                                        }
                                                                                                    } else {
                                                                                                        for (Player receiver : receivers) {
                                                                                                            applyPlaceholdersForItem(item, receiver);
                                                                                                            applyPlaceholdersForCommand(abstractCustomBlock, item, receiver);

                                                                                                            ItemUtil.distributeItem(receiver, item);
                                                                                                            ChatUtil.sendMessage(receiver, "&bYou have received the &l%s&ox%s&r&b block", block, amount);
                                                                                                        }

                                                                                                        if (receivers.size() == 1) {
                                                                                                            Player receiver = receivers.stream().findFirst().get();
                                                                                                            ChatUtil.sendMessage(sender, "&bBlock &l%s&ox%s&r&b was successfully given to the player &l%s!", block, amount, receiver.getName());
                                                                                                        } else {
                                                                                                            ChatUtil.sendMessage(sender, "&bBlock &l%s&ox%s&r&b was successfully given!", block, amount);
                                                                                                        }
                                                                                                    }

                                                                                                })
                                                                                )
                                                                )
                                                )
                                ).then(
                                        new LiteralArgument("editfile")
                                                .then(
                                                        new TextArgument ("block")
                                                                .replaceSuggestions((info, builder) -> {
                                                                            List<AbstractCustomBlock> abstractCustomBlocks = this.plugin.getCustomBlockService().getStorage().getAbstractCustomBlocks();
                                                                            String remaining = builder.getRemainingLowerCase();


                                                                            for (AbstractCustomBlock abstractCustomBlock : abstractCustomBlocks) {
                                                                                String name = abstractCustomBlock.getName();

                                                                                if (name.toLowerCase().startsWith(remaining)) {
                                                                                    AbstractCustomBlockTooltip abstractCustomBlockTooltip = new AbstractCustomBlockTooltip(abstractCustomBlock);

                                                                                    builder.suggest(abstractCustomBlockTooltip.getSuggestion(), abstractCustomBlockTooltip.getTooltip());
                                                                                }
                                                                            }
                                                                            return builder.buildFuture();
                                                                        }
                                                                )
                                                                .then(
                                                                        new LiteralArgument("central-material")
                                                                                .then(
                                                                                        new StringArgument("material")
                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                Arrays.stream(Material.values())
                                                                                                                        .filter(Material::isBlock)
                                                                                                                        .map(Material::toString)
                                                                                                                        .toList()
                                                                                                        )
                                                                                                ).executes((sender, args) -> {
                                                                                                    String block = (String) args.get("block");
                                                                                                    String material = (String) args.get("material");

                                                                                                    Material actualMaterial = Material.getMaterial(material.toUpperCase());
                                                                                                    if (actualMaterial == null || !actualMaterial.isBlock()) {
                                                                                                        throw CommandAPI.failWithString("Invalid material: " + material);
                                                                                                    }

                                                                                                    setCBConfigValue(block, "central-material", material, sender);
                                                                                                })
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("sides-count")
                                                                                .then(
                                                                                        new IntegerArgument("count", 1, 360)
                                                                                                .executes((sender, args) -> {
                                                                                                    String block = (String) args.get("block");
                                                                                                    int sidesCount = (int) args.get("count");

                                                                                                    setCBConfigValue(block, "sides-count", sidesCount, sender);
                                                                                                })
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("save-system")
                                                                                .then(
                                                                                        new StringArgument("save-system-type")
                                                                                                .replaceSuggestions(ArgumentSuggestions.strings("yaml-file", "item"))
                                                                                                .executes((sender, args) -> {
                                                                                                    String block = (String) args.get("block");
                                                                                                    String saveSystemType = (String) args.get("save-system-type");

                                                                                                    if (!saveSystemType.equals("yaml-file") && !saveSystemType.equals("item")) {
                                                                                                        throw CommandAPI.failWithString("Invalid save system type: " + saveSystemType);
                                                                                                    }

                                                                                                    setCBConfigValue(block, "save-system", saveSystemType, sender);
                                                                                                })
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("display")
                                                                                .then(
                                                                                        new MultiLiteralArgument("versioned-command", "spawn-command", "spawn-command-1_19_4", "spawn-command-1_20-1_20_4")
                                                                                                .then(
                                                                                                        new GreedyStringArgument("command")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String commandType = (String) args.get("versioned-command");
                                                                                                                    String commandLine = (String) args.get("command");
                                                                                                                    Object value = commandLine;

                                                                                                                    if (NumberUtil.isDigitString(commandLine)) {
                                                                                                                        value = NumberUtil.parseNumber(commandLine);
                                                                                                                    }

                                                                                                                    setCBConfigValue(block, "display." + commandType, List.of(value), sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("glowing")
                                                                                                .then(
                                                                                                        new BooleanArgument("glowing-value")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    boolean glowing = (boolean) args.get("glowing-value");

                                                                                                                    setCBConfigValue(block, "display.glowing", glowing, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("glow-color-override")
                                                                                                .then(
                                                                                                        new StringArgument("color")
                                                                                                                .replaceSuggestions(
                                                                                                                        ArgumentSuggestions.strings(ColorConverter.NAMED_COLORS.keySet().stream().toList())
                                                                                                                ).executes((sender, args) -> {

                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String color = (String) args.get("color");

                                                                                                                    setCBConfigValue(block, "display.glow-color-override", color, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("billboard")
                                                                                                .then(
                                                                                                        new StringArgument("billboard-type")
                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                        "fixed",
                                                                                                                        "horizontal",
                                                                                                                        "vertical",
                                                                                                                        "center"
                                                                                                                )).executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String billboardType = (String) args.get("billboard-type");

                                                                                                                    try {
                                                                                                                        Display.Billboard.valueOf(billboardType.toUpperCase());
                                                                                                                    } catch (
                                                                                                                            IllegalArgumentException e) {
                                                                                                                        throw CommandAPI.failWithString("Invalid billboard type: " + billboardType);
                                                                                                                    }

                                                                                                                    setCBConfigValue(block, "display.billboard", billboardType, sender);
                                                                                                                })

                                                                                                )
                                                                                ).then(
                                                                                        new MultiLiteralArgument("shadow-parameter", "shadow-radius", "shadow-strength")
                                                                                                .then(
                                                                                                        new FloatArgument("value")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String shadowParameter = (String) args.get("shadow-parameter");
                                                                                                                    float value = (float) args.get("value");

                                                                                                                    setCBConfigValue(block, "display." + shadowParameter, value, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("view-range")
                                                                                                .then(
                                                                                                        new FloatArgument("range")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    float range = (float) args.get("range");

                                                                                                                    setCBConfigValue(block, "display.view-range", range, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("use-placeholder")
                                                                                                .then(
                                                                                                        new BooleanArgument("use-placeholder-value")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    boolean usePlaceholder = (boolean) args.get("use-placeholder-value");

                                                                                                                    setCBConfigValue(block, "display.use-placeholder", usePlaceholder, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("brightness")
                                                                                                .then(
                                                                                                        new MultiLiteralArgument("type", "sky", "block")
                                                                                                                .then(
                                                                                                                        new FloatArgument("level")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String type = (String) args.get("type");
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    float level = (float) args.get("level");

                                                                                                                                    setCBConfigValue(block, "display.brightness." + type, level, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("translation")
                                                                                                .then(
                                                                                                        new MultiLiteralArgument("axis", "x", "y", "z")
                                                                                                                .then(
                                                                                                                        new DoubleArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String axis = (String) args.get("axis");
                                                                                                                                    double value = (double) args.get("value");

                                                                                                                                    setCBConfigValue(block, "display.translation." + axis, value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new MultiLiteralArgument("angle", "yaw", "pitch")
                                                                                                                .then(
                                                                                                                        new FloatArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String angle = (String) args.get("angle");
                                                                                                                                    float value = (float) args.get("value");

                                                                                                                                    setCBConfigValue(block, "display.translation." + angle, value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                )
                                                                                )

                                                                ).then(
                                                                        new LiteralArgument("item")
                                                                                .then(
                                                                                        new LiteralArgument("material")
                                                                                                .then(
                                                                                                        new StringArgument("item-material")
                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                        Arrays.stream(Material.values())
                                                                                                                                .filter(Material::isItem)
                                                                                                                                .map(Material::toString)
                                                                                                                                .toList()
                                                                                                                )).executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String material = (String) args.get("item-material");

                                                                                                                    Material actualMaterial = Material.getMaterial(material.toUpperCase());
                                                                                                                    if (actualMaterial == null || !actualMaterial.isItem()) {
                                                                                                                        throw CommandAPI.failWithString("Invalid material: " + material);
                                                                                                                    }

                                                                                                                    setCBConfigValue(block, "item.material", material, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("name")
                                                                                                .then(
                                                                                                        new GreedyStringArgument("item-name")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String itemName = (String) args.get("item-name");

                                                                                                                    setCBConfigValue(block, "item.name", itemName, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("item-flags")
                                                                                                .then(
                                                                                                        new ListArgumentBuilder<ItemFlag>("item-flags-list")
                                                                                                                .allowDuplicates(false)
                                                                                                                .withList(List.of(ItemFlag.values()))
                                                                                                                .withMapper(ItemFlag::name)
                                                                                                                .buildGreedy()
                                                                                                                .executes((sender, args) -> {
                                                                                                                            String block = (String) args.get("block");
                                                                                                                            List<ItemFlag> itemFlags = (List<ItemFlag>) args.get("item-flags-list");

                                                                                                                            setCBConfigValue(block, "item.item-flags", itemFlags, sender);
                                                                                                                        }
                                                                                                                )
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("enchantments")
                                                                                                .then(
                                                                                                        new MapArgumentBuilder<Enchantment, Integer>("enchantments-list", ':')
                                                                                                                .withKeyMapper(enchantment -> Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantment.toLowerCase())))
                                                                                                                .withValueMapper(Integer::parseInt)
                                                                                                                .withKeyList(List.of(Enchantment.values()).stream().map(enchantment -> enchantment.getKey().getKey()).toList())
                                                                                                                .withValueList(IntStream.rangeClosed(1, 256).sorted().mapToObj(String::valueOf).toList(), true)
                                                                                                                .build()
                                                                                                                .executes((sender, args) -> {
                                                                                                                            String block = (String) args.get("block");
                                                                                                                            Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>) args.get("enchantments-list");

                                                                                                                            setCBConfigValue(block, "item.enchantments", enchantments, sender);
                                                                                                                        }
                                                                                                                )
                                                                                                )

                                                                                ).then(
                                                                                        new LiteralArgument("lore")
                                                                                                .then(
                                                                                                        new GreedyStringArgument("lore-lines")
                                                                                                                .replaceSuggestions((info, builder) -> {
                                                                                                                    builder = builder.createOffset(builder.getStart() + info.currentArg().length());

                                                                                                                    builder.suggest("\"", Tooltip.messageFromString("Opening/closing quote"));

                                                                                                                    return builder.buildFuture();
                                                                                                                })
                                                                                                                .executes((sender, args) -> {
                                                                                                                            String block = (String) args.get("block");
                                                                                                                            String loreLines = (String) args.get("lore-lines");

                                                                                                                            List<String> loreLinesList = parseQuotedStrings(loreLines);

                                                                                                                            setCBConfigValue(block, "item.lore", loreLinesList, sender);
                                                                                                                        }
                                                                                                                )
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("skullmeta")
                                                                                                .then(
                                                                                                        new LiteralArgument("url")
                                                                                                                .then(
                                                                                                                        new StringArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String value = (String) args.get("value");

                                                                                                                                    setCBConfigValue(block, "item.skullmeta.url", value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("name")
                                                                                                                .then(
                                                                                                                        new StringArgument("head-owner-name")
                                                                                                                                .replaceSuggestions((info, builder) -> {
                                                                                                                                    String remaining = builder.getRemainingLowerCase();

                                                                                                                                    Arrays.stream(Bukkit.getOfflinePlayers())
                                                                                                                                            .map(OfflinePlayer::getName)
                                                                                                                                            .filter(Objects::nonNull)
                                                                                                                                            .filter(name -> name.toLowerCase().startsWith(remaining))
                                                                                                                                            .forEach(builder::suggest);

                                                                                                                                    return builder.buildFuture();
                                                                                                                                })
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String headOwnerName = (String) args.get("head-owner-name");

                                                                                                                                    setCBConfigValue(block, "item.skullmeta.name", headOwnerName, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("interactions")
                                                                                .then(
                                                                                        new StringArgument("interaction-name")
                                                                                                .replaceSuggestions(getInteractionSuggestions())
                                                                                                .then(
                                                                                                        new MultiLiteralArgument("measurement", "width", "height")
                                                                                                                .then(
                                                                                                                        new FloatArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String interactionName = (String) args.get("interaction-name");
                                                                                                                                    String measurement = (String) args.get("measurement");
                                                                                                                                    float value = (float) args.get("value");

                                                                                                                                    setCBConfigValue(block, "interactions." + interactionName + "." + measurement, value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("command")
                                                                                                                .then(
                                                                                                                        new CommandArgument("command-line")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String interactionName = (String) args.get("interaction-name");
                                                                                                                                    String commandLine = (String) args.get("command-line");

                                                                                                                                    setCBConfigValue(block, "interactions." + interactionName + ".command", List.of(commandLine), sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("command-source")
                                                                                                                .then(
                                                                                                                        new StringArgument("command-source")
                                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                                        "player",
                                                                                                                                        "console"
                                                                                                                                )).executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String interactionName = (String) args.get("interaction-name");
                                                                                                                                    String commandSource = (String) args.get("command-source");

                                                                                                                                    try {
                                                                                                                                        CommandBundle.CommandSource.valueOf(commandSource.toUpperCase());
                                                                                                                                    } catch (
                                                                                                                                            IllegalArgumentException e) {
                                                                                                                                        throw CommandAPI.failWithString("Invalid command source: " + commandSource);
                                                                                                                                    }

                                                                                                                                    setCBConfigValue(block, "interactions." + interactionName + ".command-source", commandSource, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("granted-command-permission")
                                                                                                                .then(
                                                                                                                        new ListArgumentBuilder<Permission>("granted-command-permission-list")
                                                                                                                                .allowDuplicates(false)
                                                                                                                                .withList(Bukkit.getPluginManager().getPermissions().stream().toList())
                                                                                                                                .withMapper(Permission::getName)
                                                                                                                                .buildGreedy()
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String interactionName = (String) args.get("interaction-name");
                                                                                                                                    List<Permission> permissions = (List<Permission>) args.get("granted-command-permission-list");

                                                                                                                                    setCBConfigValue(block, "interactions." + interactionName + ".granted-command-permission", permissions, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("offset")
                                                                                                                .then(
                                                                                                                        new MultiLiteralArgument("axis", "x", "y", "z")
                                                                                                                                .then(
                                                                                                                                        new DoubleArgument("value")
                                                                                                                                                .executes((sender, args) -> {
                                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                                    String interactionName = (String) args.get("interaction-name");
                                                                                                                                                    String axis = (String) args.get("axis");
                                                                                                                                                    double value = (double) args.get("value");

                                                                                                                                                    setCBConfigValue(block, "interactions." + interactionName + ".offset." + axis, value, sender);
                                                                                                                                                })
                                                                                                                                )
                                                                                                                )
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("collisions")
                                                                                .then(
                                                                                        new StringArgument("collision-name")
                                                                                                .replaceSuggestions(getCollisionSuggestions())
                                                                                                .then(
                                                                                                        new LiteralArgument("size")
                                                                                                                .then(
                                                                                                                        new DoubleArgument("size-value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                            String block = (String) args.get("block");
                                                                                                                                            String collisionName = (String) args.get("collision-name");
                                                                                                                                            double sizeValue = (double) args.get("size-value");

                                                                                                                                            setCBConfigValue(block, "collisions." + collisionName + ".size", sizeValue, sender);
                                                                                                                                        }
                                                                                                                                )
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("offset")
                                                                                                                .then(
                                                                                                                        new MultiLiteralArgument("axis", "x", "y", "z")
                                                                                                                                .then(
                                                                                                                                        new DoubleArgument("value")
                                                                                                                                                .executes((sender, args) -> {
                                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                                    String collisionName = (String) args.get("collision-name");
                                                                                                                                                    String axis = (String) args.get("axis");
                                                                                                                                                    double value = (double) args.get("value");

                                                                                                                                                    setCBConfigValue(block, "collisions." + collisionName + ".offset." + axis, value, sender);
                                                                                                                                                })
                                                                                                                                )
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("invisible")
                                                                                                                .then(
                                                                                                                        new BooleanArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String collisionName = (String) args.get("collision-name");
                                                                                                                                    boolean value = (boolean) args.get("value");

                                                                                                                                    setCBConfigValue(block, "collisions." + collisionName + ".invisible", value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("color")
                                                                                                                .then(
                                                                                                                        new StringArgument("color")
                                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(DyeColor.values()).map(Enum::name).toList()))
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String collisionName = (String) args.get("collision-name");
                                                                                                                                    String color = (String) args.get("color");

                                                                                                                                    try {
                                                                                                                                        DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase());
                                                                                                                                        setCBConfigValue(block, "collisions." + collisionName + ".color", dyeColor.name(), sender);
                                                                                                                                    } catch (IllegalArgumentException e) {
                                                                                                                                        throw CommandAPI.failWithString("Invalid color: " + color);
                                                                                                                                    }
                                                                                                                                })

                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("disable-below-1_20_5")
                                                                                                                .then(
                                                                                                                        new BooleanArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String collisionName = (String) args.get("collision-name");
                                                                                                                                    boolean value = (boolean) args.get("value");

                                                                                                                                    setCBConfigValue(block, "collisions." + collisionName + ".disable-below-1_20_5", value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("stage-settings")
                                                                                .then(
                                                                                        new MultiLiteralArgument("stage", "place", "break")
                                                                                                .then(
                                                                                                        new LiteralArgument("command")
                                                                                                                .then(
                                                                                                                        new CommandArgument("command-line")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String stage = (String) args.get("stage");
                                                                                                                                    String commandLine = (String) args.get("command-line");

                                                                                                                                    setCBConfigValue(block, "stage-settings." + stage + ".command", commandLine, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("command-source")
                                                                                                                .then(
                                                                                                                        new StringArgument("command-source")
                                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                                        "player",
                                                                                                                                        "console"
                                                                                                                                )).executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String stage = (String) args.get("stage");
                                                                                                                                    String commandSource = (String) args.get("command-source");

                                                                                                                                    try {
                                                                                                                                        CommandBundle.CommandSource.valueOf(commandSource.toUpperCase());
                                                                                                                                    } catch (
                                                                                                                                            IllegalArgumentException e) {
                                                                                                                                        throw CommandAPI.failWithString("Invalid command source: " + commandSource);
                                                                                                                                    }

                                                                                                                                    setCBConfigValue(block, "stage-settings." + stage + ".command-source", commandSource, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                ).then(
                                                                                                        new LiteralArgument("granted-command-permission")
                                                                                                                .then(
                                                                                                                        new ListArgumentBuilder<Permission>("granted-command-permission-list")
                                                                                                                                .allowDuplicates(false)
                                                                                                                                .withList(Bukkit.getPluginManager().getPermissions().stream().toList())
                                                                                                                                .withMapper(Permission::getName)
                                                                                                                                .buildGreedy()
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                            String block = (String) args.get("block");
                                                                                                                                            String stage = (String) args.get("stage");
                                                                                                                                            List<Permission> permissions = (List<Permission>) args.get("granted-command-permission-list");

                                                                                                                                            setCBConfigValue(block, "stage-settings." + stage + ".granted-command-permission", permissions, sender);
                                                                                                                                        }
                                                                                                                                )
                                                                                                                )
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("sound")
                                                                                .then(
                                                                                        new MultiLiteralArgument("stage", "place", "break")
                                                                                                .then(
                                                                                                        new LiteralArgument("sound-type")
                                                                                                                .then(
                                                                                                                        new StringArgument("sound-name")
                                                                                                                                .replaceSuggestions(ArgumentSuggestions.strings(
                                                                                                                                        VersionCompat.getSoundNames()
                                                                                                                                )).executes((sender, args) -> {
                                                                                                                                            String block = (String) args.get("block");
                                                                                                                                            String soundName = (String) args.get("sound-name");

                                                                                                                                            if (!VersionCompat.getSoundNames().contains(soundName.toUpperCase())) {
                                                                                                                                                throw CommandAPI.failWithString("Invalid sound type: " + soundName);
                                                                                                                                            }

                                                                                                                                            setCBConfigValue(block, "sound." + args.get("stage") + ".sound-type", soundName, sender);
                                                                                                                                        }
                                                                                                                                )
                                                                                                                )
                                                                                                ).then(
                                                                                                        new MultiLiteralArgument("sound-parameter", "volume", "pitch")
                                                                                                                .then(
                                                                                                                        new FloatArgument("value")
                                                                                                                                .executes((sender, args) -> {
                                                                                                                                    String block = (String) args.get("block");
                                                                                                                                    String stage = (String) args.get("stage");
                                                                                                                                    String parameter = (String) args.get("sound-parameter");
                                                                                                                                    float value = (float) args.get("value");

                                                                                                                                    setCBConfigValue(block, "sound." + stage + "." + parameter, value, sender);
                                                                                                                                })
                                                                                                                )
                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                ).register();
    }

    public ArgumentSuggestions<CommandSender> getCustomBlockSuggestions() {
        return (info, builder) -> {
            String remaining = builder.getRemainingLowerCase();
            this.abstractCustomBlockTooltips.forEach(tooltip -> {
                String name = tooltip.getAbstractCustomBlock().getName();

                if (name.toLowerCase().startsWith(remaining)) {
                    builder.suggest(tooltip.getSuggestion(), tooltip.getTooltip());
                }
            });
            return builder.buildFuture();
        };
    }

    public ArgumentSuggestions<CommandSender> getInteractionSuggestions() {
        return (info, builder) -> {
            CommandArguments commandArguments = info.previousArgs();
            String remaining = builder.getRemainingLowerCase();

            String block = ((String) commandArguments.get("block")).toLowerCase();

            this.interactionSuggestions.forEach((name, suggestions) -> {
                if (block.equals(name.toLowerCase())) {
                    suggestions.forEach(suggestion -> {
                        if (suggestion.toLowerCase().startsWith(remaining)) {
                            builder.suggest(suggestion);
                        }
                    });
                }
            });
            return builder.buildFuture();
        };
    }

    public ArgumentSuggestions<CommandSender> getCollisionSuggestions() {
        return (info, builder) -> {
            CommandArguments commandArguments = info.previousArgs();
            String remaining = builder.getRemainingLowerCase();

            String block = ((String) commandArguments.get("block")).toLowerCase();

            this.collisionSuggestions.forEach((name, suggestions) -> {
                if (block.equals(name.toLowerCase())) {
                    suggestions.forEach(suggestion -> {
                        if (suggestion.toLowerCase().startsWith(remaining)) {
                            builder.suggest(suggestion);
                        }
                    });
                }
            });

            return builder.buildFuture();
        };
    }


    private void setCBConfigValue(String block, String path, Object value, CommandSender sender) {
        CustomBlockStorage storage = this.plugin.getCustomBlockService().getStorage();

        if (!(storage instanceof BDCCustomBlockConfigStorage configStorage)) {
            return;
        }

        CustomBlockConfigurationFile file = configStorage.getCustomBlockRepository().getFile(block);

        if (file == null) {
            ChatUtil.sendMessage(sender, "&cBlock &l%s&r&c does not exist!", block);
            return;
        }

        String valueName = value.toString();

        if (path.endsWith("enchantments")) {
            try {
                Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>) value;

                value = PersistentDataTypes.ENCHANT.toPrimitive(enchantments, null);

                valueName = enchantments.entrySet().stream()
                        .map(entry -> entry.getKey().getKey().getKey() + ":" + entry.getValue())
                        .collect(Collectors.joining(", "));
            } catch (ClassCastException ignore) {
            }
        }

        if (value instanceof List<?> list) {

            value = list.stream().map(o -> {
                if (o instanceof Permission permission) {
                    return permission.getName();
                } else if (o instanceof ItemFlag itemFlag) {
                    return itemFlag.name();
                }
                return o.toString();
            }).toList();

            valueName = "\n" + list.stream().map(o -> {
                if (o instanceof Permission permission) {
                    return permission.getName();
                }
                return o.toString();
            }).collect(Collectors.joining("\n")) + "\n";
        }

        file.set(path, value);
        ChatUtil.sendMessage(sender, "&aBlock &l%s&r&a parameter &l%s&r&a was successfully set to \"&r%s&r&a\".", block, path, valueName);
    }


    public void reloadSuggestions() {
        abstractCustomBlockTooltips.clear();
        interactionSuggestions.clear();
        List<AbstractCustomBlock> abstractCustomBlocks = this.plugin.getCustomBlockService().getStorage().getAbstractCustomBlocks();

        for (AbstractCustomBlock abstractCustomBlock : abstractCustomBlocks) {

            AbstractCustomBlockTooltip abstractCustomBlockTooltip = new AbstractCustomBlockTooltip(abstractCustomBlock);

            abstractCustomBlockTooltips.add(abstractCustomBlockTooltip);


            List<ConfiguredInteraction> interactions = abstractCustomBlock.getConfiguredInteractions();
            List<ConfiguredCollision> collisions = abstractCustomBlock.getConfiguredCollisions();

            if (!interactions.isEmpty()) {
                interactionSuggestions.put(abstractCustomBlock.getName(), interactions.stream().map(ConfiguredEntity::getIdentifier).toList());
            }

            if (!collisions.isEmpty()) {
                collisionSuggestions.put(abstractCustomBlock.getName(), collisions.stream().map(ConfiguredEntity::getIdentifier).toList());
            }
        }
    }


    public void applyPlaceholdersForItem(ItemStack item, Player player) {
        ItemMeta itemMeta = item.getItemMeta();
        Plugin placeholderApi = BlockDisplayCreator.getPlaceholderApi();

        if (placeholderApi == null) {
            return;
        }

        itemMeta.setDisplayName(ChatUtil.setPlaceholders(player, itemMeta.getDisplayName(), placeholderApi));
        List<String> itemMetaLore = itemMeta.getLore();
        if (itemMetaLore != null) {
            List<String> lore = new ArrayList<>();
            itemMetaLore.forEach(line -> lore.add(ChatUtil.setPlaceholders(player, line, placeholderApi)));
            itemMeta.setLore(lore);
        }


        item.setItemMeta(itemMeta);
    }

    public void applyPlaceholdersForCommand(AbstractCustomBlock abstractCustomBlock, ItemStack item, Player player) {
        Plugin placeholderApi = BlockDisplayCreator.getPlaceholderApi();
        if (placeholderApi == null) {
            return;
        }

        GroupSummoner<Display> displaySummoner = abstractCustomBlock.getDisplaySummoner();

        if (displaySummoner instanceof AutomaticCommandDisplaySummoner displayCommandSummoner) {
            if (!displayCommandSummoner.isUsePlaceholder()) {
                return;
            }


            CommandLine[] parsed = displayCommandSummoner.getCommands()
                    .stream()
                    .map(commandLine ->
                            {
                                String lineString = commandLine.toString();
                                String base64Formatted = new PlayerSkinBase64Placeholder(player).applyPlaceholders(lineString);

                                return (CommandLine) new MCCommandLine(ChatUtil.setPlaceholders(
                                        player,
                                        base64Formatted,
                                        placeholderApi));
                            }
                    ).toArray(CommandLine[]::new);

            ItemMeta itemMeta = item.getItemMeta();
            PersistentData<CommandLine[]> commandListPD = new PersistentData<>(itemMeta, TypeTokens.COMMAND_ARRAY);

            commandListPD.set(CustomBlockKey.DISPLAY_SPAWN_COMMAND, parsed);

            item.setItemMeta(itemMeta);
        }
    }

    private void killBlockEntities(BoundingBox boundingBox, Player sender) {
        WorldSelection worldSelection = new WorldSelection(boundingBox, sender.getWorld());

        List<Entity> displayEntities = new ArrayList<>();
        List<Entity> interactions = new ArrayList<>();
        List<Entity> collisions = new ArrayList<>();


        sender.getWorld().getNearbyEntities(
                boundingBox,
                entity -> entity instanceof Display || entity instanceof Interaction || entity instanceof Shulker
        ).forEach(entity -> {
            if (entity instanceof Display) {
                displayEntities.add(entity);
            } else if (entity instanceof Interaction) {
                interactions.add(entity);
            } else if (entity instanceof Shulker) {
                collisions.add(entity);
            }
        });


        final int[] blocksCount = {0};

        worldSelection.getLocations().forEach(location -> {
                    CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

                    if (customBlockData.has(CustomBlockKey.NAME)) {
                        customBlockData.remove(CustomBlockKey.NAME);
                        customBlockData.remove(CustomBlockKey.SERVICE_CLASS);
                        customBlockData.remove(CustomBlockKey.LOCATION);
                        customBlockData.remove(CustomBlockKey.CUSTOM_BLOCK_UUID);
                        customBlockData.remove(CustomBlockKey.DISPLAY_UUID);
                        customBlockData.remove(CustomBlockKey.INTERACTION_UUID);
                        customBlockData.remove(CustomBlockKey.COLLISION_UUID);
                        customBlockData.remove(CustomBlockKey.INTERACTION_IDENTIFIER);
                        customBlockData.remove(CustomBlockKey.COLLISION_IDENTIFIER);
                        customBlockData.remove(CustomBlockKey.BLOCK_ROTATION);
                        customBlockData.remove(CustomBlockKey.ITEM);
                        customBlockData.remove(CustomBlockKey.DISPLAY_SPAWN_COMMAND);
                        blocksCount[0]++;
                    }
                }
        );

        displayEntities.forEach(Entity::remove);
        interactions.forEach(Entity::remove);
        collisions.forEach(Entity::remove);

        ChatUtil.sendMessage(sender, "&6Killed %d display entities", displayEntities.size());
        ChatUtil.sendMessage(sender, "&6Killed %d interaction entities", interactions.size());
        ChatUtil.sendMessage(sender, "&6Killed %d collision entities", collisions.size());
        ChatUtil.sendMessage(sender, "&6Cleared %d custom blocks", blocksCount[0]);
    }

    public static List<String> parseQuotedStrings(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inQuotes = false;
        boolean escapeNext = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escapeNext) {
                current.append(c);
                escapeNext = false;
            } else if (c == '\\') {
                escapeNext = true;
            } else if (c == '"') {
                if (inQuotes) {
                    result.add(current.toString());
                    current.setLength(0);
                    inQuotes = false;
                } else {
                    inQuotes = true;
                }
            } else {
                if (inQuotes) {
                    current.append(c);
                } else if (!Character.isWhitespace(c)) {
                    throw new IllegalArgumentException("Unexpected character outside quotes at position " + i + ": " + c);
                }
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException("Unclosed quote in input.");
        }

        return result;
    }
}
