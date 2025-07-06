package me.general_breddok.blockdisplaycreator.command;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
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
                                .withPermission(DefaultPermissions.BDC.Command.CUSTOM_BLOCK)
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
                                .withPermission("bdc.command.killcbentities")
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
                                .withPermission("bdc.command.custom-block")
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
                                                })/*,
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
                                                                )
                                                ).withSubcommands(
                                                        new CommandAPICommand("central-material")
                                                                .withArguments(
                                                                        new StringArgument("material").replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Material.values()).filter(Material::isBlock).map(Material::toString).toList()))
                                                                ).executes((sender, args) -> {
                                                                    String block = (String) args.get("block");
                                                                    String material = (String) args.get("material");
                                                                    setCustomBlockValue(block, "central-material", material, sender);
                                                                }),
                                                        new CommandAPICommand("sides-count")
                                                                .withArguments(
                                                                        new IntegerArgument("sides-count", 1, 360)
                                                                ),
                                                        new CommandAPICommand("display")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("spawn-command"),
                                                                        new CommandAPICommand("glowing"),
                                                                        new CommandAPICommand("glow-color-override"),
                                                                        new CommandAPICommand("billboard"),
                                                                        new CommandAPICommand("shadow-radius"),
                                                                        new CommandAPICommand("shadow-strength"),
                                                                        new CommandAPICommand("view-range"),
                                                                        new CommandAPICommand("brightness")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("sky"),
                                                                                        new CommandAPICommand("block")
                                                                                )
                                                                ),
                                                        new CommandAPICommand("item")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("material"),
                                                                        new CommandAPICommand("name"),
                                                                        new CommandAPICommand("item-flags"),
                                                                        new CommandAPICommand("enchantments"),
                                                                        new CommandAPICommand("lore"),
                                                                        new CommandAPICommand("skullmeta")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("url"),
                                                                                        new CommandAPICommand("name")
                                                                                )
                                                                ),
                                                        new CommandAPICommand("interactions")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("width"),
                                                                        new CommandAPICommand("height"),
                                                                        new CommandAPICommand("command"),
                                                                        new CommandAPICommand("command-source"),
                                                                        new CommandAPICommand("granted-command-permissions"),
                                                                        new CommandAPICommand("offset")
                                                                ),
                                                        new CommandAPICommand("collisions")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("size"),
                                                                        new CommandAPICommand("offset")
                                                                ),
                                                        new CommandAPICommand("sound")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("place")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("sound-type"),
                                                                                        new CommandAPICommand("volume"),
                                                                                        new CommandAPICommand("pitch")
                                                                                ),
                                                                        new CommandAPICommand("break")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("sound-type"),
                                                                                        new CommandAPICommand("volume"),
                                                                                        new CommandAPICommand("pitch")
                                                                                )
                                                                ),
                                                        new CommandAPICommand("stage-settings")
                                                                .withSubcommands(
                                                                        new CommandAPICommand("place")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("command"),
                                                                                        new CommandAPICommand("command-source"),
                                                                                        new CommandAPICommand("granted-command-permissions")
                                                                                ),
                                                                        new CommandAPICommand("break")
                                                                                .withSubcommands(
                                                                                        new CommandAPICommand("command"),
                                                                                        new CommandAPICommand("command-source"),
                                                                                        new CommandAPICommand("granted-command-permissions")
                                                                                )
                                                                )
                                                )*/
                                )
                ).register();

        /*
        new CommandTree("blockdisplaycreator")
                .withAliases("bdc")
                .then(
                        new LiteralArgument("reload")
                                .then(new StringArgument("block")
                                        .setOptional(true)
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
                                ).executes((sender, args) -> {
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
                ).then(
                        new LiteralArgument("killcbentities")
                                .thenNested(
                                        new LocationArgument("position1"),
                                        new LocationArgument("position2")
                                ).executesPlayer((sender, args) -> {
                                    Location position1 = (Location) args.get("position1");
                                    Location position2 = (Location) args.get("position2");

                                    killBlockEntities(BoundingBox.of(position1, position2), sender);
                                })
                )
                .register()*/;
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
