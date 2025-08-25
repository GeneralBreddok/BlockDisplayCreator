package me.general_breddok.blockdisplaycreator.command;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.argument.TargetSelectorType;
import me.general_breddok.blockdisplaycreator.custom.AutomaticCommandDisplaySummoner;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockConfigurationFile;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockFileRepository;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockRepository;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.PlayerSkinBase64Placeholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.StringUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @deprecated
 */
@Deprecated(since = "MC1.19.4")
public class BlockDisplayCreatorSpigotCommand implements TabExecutor {

    private final BlockDisplayCreator instance;
    private final CustomBlockService service;

    public BlockDisplayCreatorSpigotCommand(BlockDisplayCreator instance, CustomBlockService service) {
        this.instance = instance;
        this.service = service;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String arg1 = null;
        String arg2 = null;
        String arg3 = null;
        String arg4 = null;
        String arg5 = null;
        String arg6 = null;
        String arg7 = null;
        Player player = null;

        try {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
            arg4 = args[3];
            arg5 = args[4];
            arg6 = args[5];
            arg7 = args[6];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (arg1 == null) {
            ChatUtil.sendMessage(sender, "&cYou did not enter any arguments");
            return true;
        }

        switch (arg1) {
            case "custom-block" -> {

                if (!sender.hasPermission("bdc.command.custom-block")) {
                    ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                    return true;
                }

                if (arg2 == null) {
                    return false;
                }

                switch (arg2) {

                    case "give" -> {
                        if (arg3 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a block!");
                            return true;
                        }

                        if (!service.getStorage().containsAbstractCustomBlock(arg3)) {
                            ChatUtil.sendMessage(sender, "&cThis block does not exist!");
                            return true;
                        }

                        AbstractCustomBlock abstractCustomBlock = service.getStorage().getAbstractCustomBlock(arg3);
                        ItemStack customBlockItem = abstractCustomBlock.getItem();

                        byte amount = 1;

                        if (arg5 != null) {
                            try {
                                amount = Byte.parseByte(arg5);
                            } catch (NumberFormatException ignore) {
                                ChatUtil.sendMessage(sender, "&cAmount must be a number and must be in the range 1-127");
                                return true;
                            }

                            if (amount < 1) {
                                ChatUtil.sendMessage(sender, "&cAmount must be a number and must be in the range 1-127");
                                return true;
                            }
                        }

                        customBlockItem.setAmount(amount);

                        if (arg4 == null) {
                            if (player == null) {
                                ChatUtil.sendMessage(sender, "&cYou didn't specify the block recipients!");
                                return true;
                            } else {
                                ItemUtil.distributeItem(player, customBlockItem);
                                ChatUtil.sendMessage(sender, "&bYou have received the &l%s&ox%s&r&b block", arg3, amount);
                            }
                        } else {
                            switch (arg4) {
                                case "@a" -> {
                                    for (Player recipient : Bukkit.getOnlinePlayers()) {
                                        applyPlaceholdersForItem(customBlockItem, recipient);
                                        applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);
                                        ItemUtil.distributeItem(recipient, customBlockItem);
                                    }
                                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to all players", arg3, amount);
                                    return true;
                                }
                                case "@s" -> {
                                    if (player == null) {
                                        ChatUtil.sendMessage(sender, "&cThe sender of the command must be a player!");
                                        return true;
                                    }

                                    applyPlaceholdersForItem(customBlockItem, player);
                                    applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, player);
                                    ItemUtil.distributeItem(player, customBlockItem);
                                    ChatUtil.sendMessage(sender, "&bYou have received the &l%s&ox%s&r&b block", arg3, amount);
                                    return true;
                                }
                                case "@r" -> {
                                    Player randomPlayer = TargetSelectorType.getRandomPlayer();

                                    if (randomPlayer == null) {
                                        ChatUtil.sendMessage(sender, "&cThe server is empty!");
                                        return true;
                                    }

                                    applyPlaceholdersForItem(customBlockItem, randomPlayer);
                                    applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, randomPlayer);
                                    ItemUtil.distributeItem(randomPlayer, customBlockItem);
                                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to random player %s", arg3, amount, randomPlayer.getName());
                                    return true;
                                }
                                case "@p" -> {
                                    if (player == null) {
                                        ChatUtil.sendMessage(sender, "&cThe sender of the command must be a player!");
                                        return true;
                                    }

                                    Player nearestPlayer = TargetSelectorType.getNearestPlayer(player.getLocation(), player);
                                    applyPlaceholdersForItem(customBlockItem, nearestPlayer);
                                    applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, nearestPlayer);

                                    ItemUtil.distributeItem(nearestPlayer, customBlockItem);
                                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to nearest player %s", arg3, amount, nearestPlayer.getName());
                                    return true;
                                }
                                default -> {
                                    Player recipient = Bukkit.getPlayer(arg4);

                                    if (recipient == null) {
                                        ChatUtil.sendMessage(sender, "&cYou specified a non-existent selector or player name", arg4);
                                        return true;
                                    }

                                    applyPlaceholdersForItem(customBlockItem, recipient);
                                    applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);
                                    ItemUtil.distributeItem(recipient, customBlockItem);
                                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to player %s", arg3, amount, recipient.getName());
                                    return true;
                                }
                            }
                        }
                    }
                    /*case "editfile" -> {

                        if (arg3 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a block!");
                            return true;
                        }

                        if (arg4 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a parameter!");
                            return true;
                        }

                        switch (arg4) {
                            case "sides-count" -> {
                                if (arg5 == null) {
                                    return false;
                                }

                                int value = 4;

                                try {
                                    value = Integer.parseInt(arg5);
                                } catch (NumberFormatException ignored) {
                                }

                                setCustomBlockParameter(arg3, List.of(arg4), value, sender);
                            }
                            case "display" -> {
                                if (arg5 == null) {
                                    return false;
                                }

                                switch (arg5) {
                                    case "glowing" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), arg6, sender);
                                    }
                                    case "glow-color-override" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        int value = 1;

                                        try {
                                            value = Integer.parseInt(arg6);
                                        } catch (NumberFormatException ignored) {
                                        }

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), value, sender);
                                    }
                                }
                            }
                            case "central-material" -> {

                                if (arg5 == null) {
                                    return false;
                                }

                                setCustomBlockParameter(arg3, List.of(arg4), arg5, sender);

                            }
                            case "item" -> {

                                if (arg5 == null) {
                                    return false;
                                }

                                switch (arg5) {
                                    case "material" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), arg6, sender);
                                    }
                                    case "name" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        String name = String.join(" ", Arrays.copyOfRange(args, 5, args.length));

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), name, sender);
                                    }
                                    case "enchantments" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        int level = 1;

                                        try {
                                            level = Integer.parseInt(
                                                    Optional.ofNullable(arg7).orElse("1")
                                            );
                                        } catch (NumberFormatException ignored) {
                                        }

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5, arg6), level, sender);
                                    }
                                    case "item-flags" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), List.of(arg6), sender);
                                    }
                                    case "lore" -> {

                                        if (arg6 == null) {
                                            return false;
                                        }

                                        String lore = String.join(" ", Arrays.copyOfRange(args, 5, args.length));

                                        setCustomBlockParameter(arg3, List.of(arg4, arg5), List.of(lore), sender);
                                    }
                                    case "skullmeta" -> {

                                        if (arg6 == null) {
                                            return false;
                                        }

                                        switch (arg6) {
                                            case "url" -> {

                                                if (arg7 == null) {
                                                    return false;
                                                }

                                                setCustomBlockParameter(arg3, List.of(arg4, arg5, arg6), arg7, sender);
                                            }
                                            default -> {
                                                return false;
                                            }
                                        }
                                    }
                                    default -> {
                                        return false;
                                    }
                                }
                            }
                            *//*case "interaction" -> {

                                if (arg5 == null) {
                                    return false;
                                }

                                switch (arg5) {
                                    case "width", "height" -> {

                                        if (arg6 == null) {
                                            return false;
                                        }

                                        float value = 1.001F;

                                        try {
                                            value = Float.parseFloat(arg6);
                                        } catch (NumberFormatException ignored) {
                                        }

                                        setCustomBlockParameter(List.of(arg1, arg3, arg4, arg5), value, sender);
                                    }
                                    case "command" -> {

                                        if (arg6 == null) {
                                            return false;
                                        }

                                        String interactionCommand = String.join(" ", Arrays.copyOfRange(args, 5, args.length));

                                        setCustomBlockParameter(List.of(arg1, arg3, arg4, arg5), List.of(interactionCommand), sender);
                                    }
                                    case "command-source" -> {

                                        if (arg6 == null) {
                                            return false;
                                        }

                                        setCustomBlockParameter(List.of(arg1, arg3, arg4, arg5), arg6, sender);
                                    }
                                }
                            }*//*
                            case "sound" -> {
                                if (arg5 == null) {
                                    return false;
                                }

                                switch (arg5) {
                                    case "break", "place" -> {
                                        if (arg6 == null) {
                                            return false;
                                        }

                                        switch (arg6) {
                                            case "volume", "pitch" -> {
                                                if (arg7 == null) {
                                                    return false;
                                                }

                                                float value = 1F;

                                                try {
                                                    value = Float.parseFloat(arg7);
                                                } catch (NumberFormatException ignored) {
                                                }

                                                setCustomBlockParameter(arg3, List.of(arg4, arg5, arg6), value, sender);
                                            }
                                            case "sound-type" -> {
                                                if (arg7 == null) {
                                                    return false;
                                                }

                                                setCustomBlockParameter(arg3, List.of(arg4, arg5, arg6), arg7, sender);
                                            }
                                        }
                                    }
                                }
                            }
                            default -> {
                                return false;
                            }
                        }
                    }*/
                    default -> {
                        return false;
                    }
                }
            }
            case "killcbentities" -> {

                if (!sender.hasPermission(DefaultPermissions.BDC.Command.KILL_CB_ENTITIES)) {
                    ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                    return true;
                }

                if (player == null) {
                    ChatUtil.sendMessage(sender, "&cThe command can only be applied by the player!");
                    return true;
                }

                if (args.length != 7) {
                    ChatUtil.sendMessage(sender, "&cYou must specify coordinates in the format <x1> <y1> <z1> <x2> <y2> <z2>");
                    return true;
                }

                try {
                    killBlockEntities(new BoundingBox
                                    (
                                            Double.valueOf(arg2),
                                            Double.valueOf(arg3),
                                            Double.valueOf(arg4),
                                            Double.valueOf(arg5),
                                            Double.valueOf(arg6),
                                            Double.valueOf(arg7)
                                    ),
                            player
                    );
                } catch (NumberFormatException exception) {
                    ChatUtil.sendMessage(sender, "&cIt looks like you entered the wrong number");
                    return true;
                }
            }
            case "reload" -> {

                if (!sender.hasPermission("bdc.command.reload")) {
                    ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                    return true;
                }

                if (arg2 == null) {
                    instance.reloadConfig();
                    ChatUtil.sendMessage(sender, "&aConfig has been reloaded!");
                } else {
                    this.instance.getCustomBlockService().getStorage().reload(arg2);
                    ChatUtil.sendMessage(sender, "&a%s block has been reloaded!", arg2);
                }
            }
            default -> {
                return false;
            }
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        /*Material[] materials = Material.values();
        List<Material> blockMaterials = Arrays.stream(materials).filter(Material::isBlock).toList();
        List<Enchantment> enchantments = List.of(Enchantment.values());
        List<ItemFlag> itemFlags = List.of(ItemFlag.values());
        Sound[] sounds = Sound.values();*/

        List<String> blocksNames = service.getStorage().getNames();

        String arg1 = null;
        String arg2 = null;
        String arg3 = null;
        String arg4 = null;
        String arg5 = null;
        String arg6 = null;
        String arg7 = null;
        Player player = null;

        try {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
            arg4 = args[3];
            arg5 = args[4];
            arg6 = args[5];
            arg7 = args[6];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        String killcbentities = "killcbentities";
        if (args.length == 1) {
            StringUtil.copyPartialMatches(arg1, List.of(
                    "custom-block",
                    killcbentities,
                    "reload"
            ), result);
        } else if (args.length == 2) {
            if (arg1.equals(killcbentities)) {
                if (player != null) {
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg2, Collections.singletonList(x + " " + y + " " + z), result);
                }
            } else if (arg1.equals("custom-block")) {
                StringUtil.copyPartialMatches(arg2, List.of(
                        "give"/*,
                        "set"*/
                ), result);
            } else if (arg1.equals("reload")) {
                StringUtil.copyPartialMatches(arg2, blocksNames, result);
            }
        } else if (args.length == 3) {
            if (arg1.equals(killcbentities)) {

                if (player != null) {
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg3, Collections.singletonList(y + " " + z), result);
                }
            } else if (arg2.equals("give")/* || arg2.equals("set")*/) {
                StringUtil.copyPartialMatches(arg3, blocksNames, result);
            }
        } else if (args.length == 4) {
            if (arg1.equals(killcbentities)) {
                if (player != null) {
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg4, Collections.singletonList("" + z), result);
                }
            } /*else if (arg2.equals("set")) {

                StringUtil.copyPartialMatches(arg4, List.of(
                        "central-material",
                        "item",
                        *//*"interactions",*//*
                        "sound",
                        "display",
                        "sides-count"
                ), result);
            }*/ else if (arg2.equals("give")) {

                List<String> tabComplete = new ArrayList<>();

                tabComplete.addAll(List.of(
                        "@a",
                        "@p",
                        "@r",
                        "@s"
                ));

                tabComplete.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());

                StringUtil.copyPartialMatches(arg4, tabComplete, result);
            }
        } else if (args.length == 5) {
            if (arg1.equals(killcbentities)) {
                if (player != null) {
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg5, Collections.singletonList(x + " " + y + " " + z), result);
                }
            } /*else if (arg4.equals("central-material")) {
                StringUtil.copyPartialMatches(arg5, blockMaterials.stream().map(Object::toString).toList(), result);
            } else if (arg4.equals("item")) {
                StringUtil.copyPartialMatches(arg5, List.of(
                        "material",
                        "name",
                        "enchantments",
                        "item-flags",
                        "lore",
                        "skullmeta"
                ), result);
            } *//*else if (arg4.equals("interactions")) {
                StringUtil.copyPartialMatches(arg5, List.of(
                        "width",
                        "height",
                        "command",
                        "command-source"
                ), result);
            }*//* else if (arg4.equals("display")) {
                StringUtil.copyPartialMatches(arg5, List.of(
                        "glowing",
                        "glow-color-override"
                ), result);
            } else if (arg4.equals("sound")) {
                StringUtil.copyPartialMatches(arg5, List.of(
                        "break",
                        "place"
                ), result);
            }*/
        } else if (args.length == 6) {
            if (arg1.equals(killcbentities)) {
                if (player != null) {
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg6, Collections.singletonList(y + " " + z), result);
                }

            } /*else if (arg5.equals("material")) {
                StringUtil.copyPartialMatches(arg6, Arrays.stream(materials).map(Object::toString).toList(), result);
            } else if (arg5.equals("enchantments")) {
                StringUtil.copyPartialMatches(arg6, enchantments.stream().map(enchantment -> enchantment.getKey().getKey().toUpperCase()).toList(), result);
            } else if (arg5.equals("item-flags")) {
                StringUtil.copyPartialMatches(arg6, itemFlags.stream().map(Object::toString).toList(), result);
            } else if (arg5.equals("skullmeta")) {
                StringUtil.copyPartialMatches(arg6, List.of("url"), result);
            } else if (arg5.equals("command-source")) {
                StringUtil.copyPartialMatches(arg6, List.of("player", "console"), result);
            } else if (arg5.equals("glowing")) {
                StringUtil.copyPartialMatches(arg6, List.of("true", "false"), result);
            } else if (arg5.equals("break") || arg5.equals("place")) {
                StringUtil.copyPartialMatches(arg6, List.of("sound-type", "volume", "pitch"), result);
            }*/
        } else if (args.length == 7) {
            if (arg1.equals(killcbentities)) {
                if (player != null) {
                    int z = player.getLocation().getBlockZ();

                    StringUtil.copyPartialMatches(arg7, Collections.singletonList("" + z), result);
                }
            } /*else if (arg6.equals("sound-type")) {
                StringUtil.copyPartialMatches(arg7, Arrays.stream(sounds).map(Enum::name).toList(), result);
            }*/
        }


        return result;
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
}

