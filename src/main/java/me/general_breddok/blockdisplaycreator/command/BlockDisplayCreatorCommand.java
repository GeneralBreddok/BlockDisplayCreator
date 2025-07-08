package me.general_breddok.blockdisplaycreator.command;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.common.ColorConverter;
import me.general_breddok.blockdisplaycreator.custom.AutomaticCommandDisplaySummoner;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockStorage;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.PlayerSkinBase64Placeholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockDisplayCreatorCommand {
    final BlockDisplayCreator plugin;
    List<AbstractCustomBlockTooltip> abstractCustomBlockTooltips = new ArrayList<>();

    public BlockDisplayCreatorCommand(BlockDisplayCreator plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("blockdisplaycreator")
                .withAliases("bdc")
                .withSubcommands(
                        new CommandAPICommand("reload")
                                .withPermission(DefaultPermissions.BDC.Command.RELOAD)
                                .withOptionalArguments(
                                        new StringArgument("block")
                                                .replaceSuggestions((info, builder) -> {
                                                            String remaining = builder.getRemainingLowerCase();
                                                            this.abstractCustomBlockTooltips.forEach(tooltip -> {
                                                                String name = tooltip.getAbstractCustomBlock().getName();

                                                                if (name.toLowerCase().startsWith(remaining)) {
                                                                    builder.suggest(tooltip.getSuggestion(), tooltip.getTooltip());
                                                                }
                                                            });
                                                            return builder.buildFuture();
                                                        }
                                                )
                                )
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
                                }),
                        new CommandAPICommand("killcbentities")
                                .withPermission(DefaultPermissions.BDC.Command.KILL_CB_ENTITIES)
                                .withArguments(
                                        new LocationArgument("position1"),
                                        new LocationArgument("position2")
                                )
                                .executesPlayer((sender, args) -> {
                                    Location position1 = (Location) args.get("position1");
                                    Location position2 = (Location) args.get("position2");

                                    killBlockEntities(BoundingBox.of(position1, position2), sender);
                                }),
                        new CommandAPICommand("custom-block")
                                .withPermission(DefaultPermissions.BDC.Command.CUSTOM_BLOCK)
                                .withSubcommands(
                                        new CommandAPICommand("give")
                                                .withArguments(
                                                        new StringArgument("block")
                                                                .replaceSuggestions((info, builder) -> {
                                                                            String remaining = builder.getRemainingLowerCase();
                                                                            this.abstractCustomBlockTooltips.forEach(tooltip -> {
                                                                                String name = tooltip.getAbstractCustomBlock().getName();

                                                                                if (name.toLowerCase().startsWith(remaining)) {
                                                                                    builder.suggest(tooltip.getSuggestion(), tooltip.getTooltip());
                                                                                }
                                                                            });
                                                                            return builder.buildFuture();
                                                                        }
                                                                )
                                                ).withOptionalArguments(
                                                        new EntitySelectorArgument.ManyPlayers("receiver"),
                                                        new IntegerArgument("amount", 1, 127)
                                                ).executes((sender, args) -> {
                                                    String block = (String) args.get("block");
                                                    int amount = (int) args.getOrDefault("amount", 1);

                                                    Collection<Player> receivers = (Collection<Player>) args.get("receiver");

                                                    CustomBlockStorage storage = this.plugin.getCustomBlockService().getStorage();
                                                    if (!storage.getNames().contains(block)) {
                                                        ChatUtil.sendMessage(sender, "&cBlock %s does not exist!", block);
                                                    } else {
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
                                                    }
                                                }),
                                        new CommandAPICommand("set")
                                                .withArguments(
                                                        new StringArgument("block")
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
                                                                ).then(
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

                                                                                                    setCustomBlockValue(block, "central-material", material, sender);
                                                                                                })
                                                                                ).then(
                                                                                        new LiteralArgument("sides-count")
                                                                                                .then(
                                                                                                        new IntegerArgument("count", 1, 360)
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    int sidesCount = (int) args.get("count");

                                                                                                                    setCustomBlockValue(block, "sides-count", sidesCount, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("display")
                                                                                                .then(
                                                                                                        new LiteralArgument("spawn-command")
                                                                                                                .then(
                                                                                                                        new GreedyStringArgument("command")

                                                                                                                ).executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String commandLine = (String) args.get("command");

                                                                                                                    setCustomBlockValue(block, "display.spawn-command", commandLine, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("glowing")
                                                                                                .then(
                                                                                                        new BooleanArgument("glowing")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    boolean glowing = (boolean) args.get("glowing");

                                                                                                                    setCustomBlockValue(block, "display.glowing", glowing, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("glow-color-override")
                                                                                                .then(
                                                                                                        new StringArgument("color")
                                                                                                                .replaceSuggestions(((info, builder) -> {
                                                                                                                    String remaining = builder.getRemainingLowerCase();

                                                                                                                    for (String colorName : ColorConverter.NAMED_COLORS.keySet()) {
                                                                                                                        if (colorName.toLowerCase().startsWith(remaining)) {
                                                                                                                            builder.suggest(colorName);
                                                                                                                        }
                                                                                                                    }

                                                                                                                    return builder.buildFuture();
                                                                                                                })).executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    String color = (String) args.get("color");

                                                                                                                    setCustomBlockValue(block, "display.glow-color-override", color, sender);
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

                                                                                                                    setCustomBlockValue(block, "display.billboard", billboardType, sender);
                                                                                                                })

                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("shadow-radius")
                                                                                                .then(
                                                                                                        new FloatArgument("radius")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    float radius = (float) args.get("radius");

                                                                                                                    setCustomBlockValue(block, "display.shadow-radius", radius, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("shadow-strength")
                                                                                                .then(
                                                                                                        new FloatArgument("strength")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    float strength = (float) args.get("strength");

                                                                                                                    setCustomBlockValue(block, "display.shadow-strength", strength, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("view-range")
                                                                                                .then(
                                                                                                        new FloatArgument("range")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    String block = (String) args.get("block");
                                                                                                                    float range = (float) args.get("range");

                                                                                                                    setCustomBlockValue(block, "display.view-range", range, sender);
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

                                                                                                                                    setCustomBlockValue(block, "display.brightness." + type, level, sender);
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

                                                                                                                    setCustomBlockValue(block, "item.material", material, sender);
                                                                                                                })
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("name")
                                                                                ).then(
                                                                                        new LiteralArgument("item-flags")
                                                                                ).then(
                                                                                        new LiteralArgument("enchantments")
                                                                                ).then(
                                                                                        new LiteralArgument("lore")
                                                                                ).then(
                                                                                        new LiteralArgument("skullmeta")
                                                                                                .then(
                                                                                                        new LiteralArgument("url")
                                                                                                ).then(
                                                                                                        new LiteralArgument("name")
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("interactions")
                                                                                .then(
                                                                                        new LiteralArgument("width")
                                                                                ).then(
                                                                                        new LiteralArgument("height")
                                                                                ).then(
                                                                                        new LiteralArgument("command")
                                                                                ).then(
                                                                                        new LiteralArgument("command-source")
                                                                                ).then(
                                                                                        new LiteralArgument("granted-command-permissions")
                                                                                ).then(
                                                                                        new LiteralArgument("offset")
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("collisions")
                                                                                .then(
                                                                                        new LiteralArgument("size")
                                                                                ).then(
                                                                                        new LiteralArgument("offset")
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("sound")
                                                                                .then(
                                                                                        new LiteralArgument("place")
                                                                                                .then(
                                                                                                        new LiteralArgument("sound-type")
                                                                                                ).then(
                                                                                                        new LiteralArgument("volume")
                                                                                                ).then(
                                                                                                        new LiteralArgument("pitch")
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("break")
                                                                                                .then(
                                                                                                        new LiteralArgument("sound-type")
                                                                                                ).then(
                                                                                                        new LiteralArgument("volume")
                                                                                                ).then(
                                                                                                        new LiteralArgument("pitch")
                                                                                                )
                                                                                )
                                                                ).then(
                                                                        new LiteralArgument("stage-settings")
                                                                                .then(
                                                                                        new LiteralArgument("place")
                                                                                                .then(
                                                                                                        new LiteralArgument("command")
                                                                                                ).then(
                                                                                                        new LiteralArgument("command-source")
                                                                                                ).then(
                                                                                                        new LiteralArgument("granted-command-permissions")
                                                                                                )
                                                                                ).then(
                                                                                        new LiteralArgument("break")
                                                                                                .then(
                                                                                                        new LiteralArgument("command")
                                                                                                ).then(
                                                                                                        new LiteralArgument("command-source")
                                                                                                ).then(
                                                                                                        new LiteralArgument("granted-command-permissions")
                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                ).register();

    }


    private void setCustomBlockValue(String block, String path, Object value, CommandSender sender) {
        YamlConfigFile file = new YamlConfigFile(Path.of(block + ".yml"));

        file.set(path, value);
        ChatUtil.sendMessage(sender, "&aBlock &l%s&r&a parameter &l%s&r&a was successfully set to \"&l%s&r&a\".", block, path, value);
    }


    public void reloadSuggestions() {
        abstractCustomBlockTooltips.clear();
        List<AbstractCustomBlock> abstractCustomBlocks = this.plugin.getCustomBlockService().getStorage().getAbstractCustomBlocks();

        for (AbstractCustomBlock abstractCustomBlock : abstractCustomBlocks) {

            AbstractCustomBlockTooltip abstractCustomBlockTooltip = new AbstractCustomBlockTooltip(abstractCustomBlock);

            abstractCustomBlockTooltips.add(abstractCustomBlockTooltip);
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
        Collection<Entity> displayEntities = sender.getWorld().getNearbyEntities(
                boundingBox,
                Display.class::isInstance);

        Collection<Entity> interactions = sender.getWorld().getNearbyEntities(
                boundingBox,
                Interaction.class::isInstance);


        WorldSelection worldSelection = new WorldSelection(boundingBox, sender.getWorld());

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
                        customBlockData.remove(CustomBlockKey.BLOCK_ROTATION);
                        blocksCount[0]++;
                    }
                }
        );

        ChatUtil.sendMessage(sender, "&6Killed %d display entities", displayEntities.size());
        ChatUtil.sendMessage(sender, "&6Killed %d interaction entities", interactions.size());
        ChatUtil.sendMessage(sender, "&6Cleared %d custom blocks", blocksCount[0]);

        displayEntities.forEach(Entity::remove);
        interactions.forEach(Entity::remove);
    }
}
