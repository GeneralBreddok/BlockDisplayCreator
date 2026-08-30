package me.general_breddok.blockdisplaycreator.command;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.argument.TargetSelectorType;
import me.general_breddok.blockdisplaycreator.custom.block.*;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockPlaceOption;
import me.general_breddok.blockdisplaycreator.file.config.value.StringMessagesValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.LocationPlaceholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.CommandUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BlockDisplayCreatorSpigotCommand implements TabExecutor {

    private final BlockDisplayCreator plugin;
    private final CustomBlockService service;

    public BlockDisplayCreatorSpigotCommand(BlockDisplayCreator plugin, CustomBlockService service) {
        this.plugin = plugin;
        this.service = service;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /*if (!sender.hasPermission(DefaultPermissions.BDC.Command.BASE)) {
            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
            return true;
        }*/

        String arg1 = null;
        String arg2 = null;
        String arg3 = null;
        String arg4 = null;
        String arg5 = null;
        String arg6 = null;
        String arg7 = null;
        String arg8 = null;
        String arg9 = null;
        Player player = null;

        try {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
            arg4 = args[3];
            arg5 = args[4];
            arg6 = args[5];
            arg7 = args[6];
            arg8 = args[7];
            arg9 = args[8];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (arg1 == null) {
            ChatUtil.sendMessage(sender, "&cYou did not enter any arguments");
            return true;
        }

        String finalArg5 = arg5;
        String finalArg3 = arg3;
        switch (arg1) {
            case "custom-block" -> {

                if (arg2 == null) {
                    return false;
                }

                final CustomBlockStorage storage = service.getStorage();

                switch (arg2) {
                    /*case "create" -> {

                        ChatUtil.log("1");

                        if (!sender.hasPermission(DefaultPermissions.BDC.Command.CREATE_CB)) {
                            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                            return true;
                        }

                        if (arg3 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a name for the new block!");
                            return true;
                        }

                        if (arg4 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a BDEngine model id for the display of " + arg3 + " block!");
                            return true;
                        }

                        if (arg5 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify an item material for the " + arg3 + " block!");
                            return true;
                        }

                        if (FileUtil.containsFile(BlockDisplayCreator.getInstance().getDataFolder().toPath().resolve("custom-blocks"), arg3 + ".yml")) {
                            ChatUtil.sendMessage(sender, "&cA custom block with the name " + arg3 + " already exists.");
                            return true;
                        }

                        int modelId;

                        try {
                            modelId = Integer.parseInt(arg4);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(sender, "&cThe BDEngine model id must be a number!");
                            return true;
                        }

                        if (ItemUtil.BLOCK_MATERIAL_NAMES.stream().noneMatch(materialName -> materialName.equalsIgnoreCase(finalArg5))) {
                            ChatUtil.sendMessage(sender, "&cThe item material " + finalArg5 + " is not a valid block material!");
                            return true;
                        }

                        Material itemMaterial = Material.getMaterial(finalArg5.toUpperCase());

                        float interactionWidth = 1.001f;
                        float interactionHeight = 1.001f;

                        if (arg6 != null) {
                            try {
                                interactionWidth = Float.parseFloat(arg6);
                            } catch (NumberFormatException e) {
                                ChatUtil.sendMessage(sender, "&cThe interaction width must be a number!");
                                return true;
                            }
                        }

                        if (arg7 != null) {
                            try {
                                interactionHeight = Float.parseFloat(arg7);
                            } catch (NumberFormatException e) {
                                ChatUtil.sendMessage(sender, "&cThe interaction height must be a number!");
                                return true;
                            }
                        }

                        BDEModel model;

                        ChatUtil.log("2");

                        try {
                            model = new NetworkBDEModel(modelId);
                        } catch (
                                InvalidResponseException e) {
                            throw new RuntimeException(e);
                        }

                        ChatUtil.log("3");

                        List<String> summonCommands = model.getSummonCommands();

                        CustomBlockFileRepository.saveAbstractCustomBlock(BlockDisplayCreator.getInstance().getDataFolder().toPath(), arg3, summonCommands, itemMaterial, interactionWidth, interactionHeight);

                        ChatUtil.log("4");

                        storage.reload(arg3);

                        ChatUtil.log("5");
                        ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_CUSTOM_BLOCK_CREATE_SUCCESS.replace("%customblock_name%", arg3));


                    }*/

                    case "place" -> {

                        if (player == null) {
                            ChatUtil.sendMessage(sender, "&cThe sender of the command must be a player!");
                            return true;
                        }

                        if (!sender.hasPermission(DefaultPermissions.BDC.Command.PLACE_CB)) {
                            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                            return true;
                        }

                        if (arg3 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a block!");
                            return true;
                        }

                        if (!storage.containsAbstractCustomBlock(arg3)) {
                            ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_BLOCK_NOT_EXISTS.replace("%customblock_name%", arg3));
                            return true;
                        }


                        Location location = getLocation(arg4, arg5, arg6, player);

                        if (location == null) {
                            return true;
                        }

                        LocationPlaceholder locationPlaceholder = new LocationPlaceholder(location);


                        String attachedFaceStr = arg7 == null ? "up" : arg7;
                        BlockFace attachedFace;

                        try {
                            attachedFace = BlockFace.valueOf(attachedFaceStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_CUSTOM_BLOCK_PLACE_INVALID_ATTACHED_FACE.replace("%face%", attachedFaceStr));
                            return true;
                        }


                        int direction = 0;

                        if (arg8 != null) {
                            try {
                                direction = Integer.parseInt(arg8);
                            } catch (NumberFormatException e) {
                                ChatUtil.sendMessage(sender, "&cThe direction must be a number from 0 to 360!");
                                return true;
                            }
                        }


                        List<CustomBlockPlaceOption> options = new ArrayList<>();

                        if (arg9 != null) {

                            for (int i = 8; i < args.length; i++) {
                                try {
                                    CustomBlockPlaceOption option = CustomBlockPlaceOption.valueOf(args[i].toUpperCase());
                                    options.add(option);
                                } catch (IllegalArgumentException e) {
                                    ChatUtil.sendMessage(sender, "&cThe option " + args[i] + " is not a valid place option!");
                                    return true;
                                }
                            }
                        }


                        AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(arg3);

                        CustomBlockRotation rotation = new BDCCustomBlockRotation(attachedFace, direction);

                        Bukkit.getScheduler().runTask(this.plugin, () -> {
                            CustomBlock customBlock;

                            try {
                                customBlock = this.plugin.getCustomBlockService().placeBlock(abstractCustomBlock, location, rotation, null, options.toArray(CustomBlockPlaceOption[]::new));
                            } catch (IllegalArgumentException e) {
                                ChatUtil.sendMessage(sender,
                                        locationPlaceholder.apply(StringMessagesValue.COMMAND_CUSTOM_BLOCK_PLACE_FAILED)
                                                .replace("%customblock_name%", finalArg3)
                                                .replace("%reason%", e.getMessage())
                                );
                                return;
                            }

                            if (customBlock == null) {
                                ChatUtil.sendMessage(sender, locationPlaceholder.apply(StringMessagesValue.COMMAND_CUSTOM_BLOCK_PLACE_FAILED_WITHOUT_REASON)
                                        .replace("%customblock_name%", finalArg3)
                                );
                                return;
                            }

                            ChatUtil.sendMessage(sender,
                                    locationPlaceholder.apply(StringMessagesValue.COMMAND_CUSTOM_BLOCK_PLACE_PLACED)
                                            .replace("%customblock_name%", finalArg3)
                            );
                        });
                    }

                    case "give" -> {

                        if (!sender.hasPermission(DefaultPermissions.BDC.Command.GIVE_CB)) {
                            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                            return true;
                        }

                        if (arg3 == null) {
                            ChatUtil.sendMessage(sender, "&cYou didn't specify a block!");
                            return true;
                        }

                        if (!storage.containsAbstractCustomBlock(arg3)) {
                            ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_BLOCK_NOT_EXISTS.replace("%customblock_name%", arg3));
                            return true;
                        }

                        AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(arg3);
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
                                ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_CUSTOM_BLOCK_GIVE_NO_PLAYER);
                                return true;
                            } else {
                                CommandUtil.applyPlaceholdersForItem(customBlockItem, player);
                                CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, player);

                                ItemUtil.distributeItem(player, customBlockItem);

                                ChatUtil.sendMessage(player,
                                        StringMessagesValue.COMMAND_CUSTOM_BLOCK_GIVE_PLAYER_RECEIVE
                                                .replace("%customblock_name%", arg3)
                                                .replace("%amount%", String.valueOf(amount))
                                );
                            }
                        } else {
                            switch (arg4) {
                                case "@a" -> {
                                    for (Player recipient : Bukkit.getOnlinePlayers()) {
                                        CommandUtil.applyPlaceholdersForItem(customBlockItem, recipient);
                                        CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);

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

                                    CommandUtil.applyPlaceholdersForItem(customBlockItem, player);
                                    CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, player);

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

                                    CommandUtil.applyPlaceholdersForItem(customBlockItem, randomPlayer);
                                    CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, randomPlayer);

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

                                    CommandUtil.applyPlaceholdersForItem(customBlockItem, nearestPlayer);
                                    CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, nearestPlayer);

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

                                    CommandUtil.applyPlaceholdersForItem(customBlockItem, recipient);
                                    CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);

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
            case "erasecbdata" -> {

                if (!sender.hasPermission(DefaultPermissions.BDC.Command.ERASE_CB_DATA)) {
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
                    eraseCbData(new BoundingBox
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

                if (!sender.hasPermission(DefaultPermissions.BDC.Command.RELOAD)) {
                    ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
                    return true;
                }

                if (arg2 == null) {
                    plugin.reloadConfig();
                    ChatUtil.sendMessage(sender, "&aConfig has been reloaded!");
                } else {
                    this.plugin.getCustomBlockService().getStorage().reload(arg2);
                    ChatUtil.sendMessage(sender, "&a%s block has been reloaded!", arg2);
                }
            }
            default -> {
                return false;
            }
        }


        return true;
    }

    private @Nullable Location getLocation(String arg4, String arg5, String arg6, Player player) {
        double x = parseCoordinate(arg4);
        double y = parseCoordinate(arg5);
        double z = parseCoordinate(arg6);

        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            ChatUtil.sendMessage(player, "&cYou must specify valid coordinates in the format <x> <y> <z>");
            return null;
        }

        Location location = new Location(player.getWorld(), x, y, z);
        return location;
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
        Location lookingLocation = null;


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

        if (player != null) {
            Block targetBlockExact = player.getTargetBlockExact(5);

            if (targetBlockExact != null) {
                lookingLocation = targetBlockExact.getLocation();
            } else {
                lookingLocation = player.getLocation();
            }
        }

        if (args.length == 1) {
            StringUtil.copyPartialMatches(arg1, List.of(
                    "custom-block",
                    "erasecbdata",
                    "reload"
            ), result);
        } else if (args.length == 2) {
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg2, Collections.singletonList(lookingLocation.getBlockX() + " " + lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            } else if (arg1.equals("custom-block")) {
                StringUtil.copyPartialMatches(arg2, List.of(
                        "give",
                        "place"
                        /*"create",
                        "set"*/
                ), result);
            } else if (arg1.equals("reload")) {
                StringUtil.copyPartialMatches(arg2, blocksNames, result);
            }
        } else if (args.length == 3) {
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg3, Collections.singletonList(lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            } else if (arg2.equals("give") || arg2.equals("place")) {
                StringUtil.copyPartialMatches(arg3, blocksNames, result);
            }
        } else if (args.length == 4) {
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg4, Collections.singletonList("" + lookingLocation.getBlockZ()), result);
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
            } else if (arg2.equals("place")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg4, Collections.singletonList(lookingLocation.getBlockX() + " " + lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            }
        } else if (args.length == 5) {
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg5, Collections.singletonList(lookingLocation.getBlockX() + " " + lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            } else if (arg2.equals("place")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg5, Collections.singletonList(lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            }
            /*else if (arg2.equals("create")) {
                StringUtil.copyPartialMatches(arg5, ItemUtil.BLOCK_MATERIAL_NAMES, result);
            }*/
            /*else if (arg4.equals("central-material")) {
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
            } */
            /*}else if (arg4.equals("interactions")) {
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
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg6, Collections.singletonList(lookingLocation.getBlockX() + " " + lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            } else if (arg2.equals("place")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg6, Collections.singletonList("" + lookingLocation.getBlockZ()), result);
                }
            }

            /*else if (arg5.equals("material")) {
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
            if (arg1.equals("erasecbdata")) {
                if (player != null) {
                    StringUtil.copyPartialMatches(arg7, Collections.singletonList(lookingLocation.getBlockX() + " " + lookingLocation.getBlockY() + " " + lookingLocation.getBlockZ()), result);
                }
            } else if (arg2.equals("place")) {
                StringUtil.copyPartialMatches(arg7, List.of("up", "down", "north", "south", "west", "east"), result);
            }
                /*else if (arg6.equals("sound-type")) {
                StringUtil.copyPartialMatches(arg7, Arrays.stream(sounds).map(Enum::name).toList(), result);
            }*/
        }


        return result;
    }


    private void eraseCbData(BoundingBox boundingBox, Player sender) {
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

    private double parseCoordinate(String coord) {
        try {
            if (coord == null) {
                return Double.NaN;
            }
            return Double.parseDouble(coord);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}

