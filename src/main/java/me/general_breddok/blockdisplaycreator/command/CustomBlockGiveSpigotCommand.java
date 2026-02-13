package me.general_breddok.blockdisplaycreator.command;


import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.argument.TargetSelectorType;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.file.config.value.StringMessagesValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.CommandUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CustomBlockGiveSpigotCommand implements TabExecutor {

    private final CustomBlockService service;

    public CustomBlockGiveSpigotCommand(CustomBlockService service) {
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String arg1 = null;
        String arg2 = null;
        String arg3 = null;
        Player player = null;

        try {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (sender instanceof Player) {
            player = (Player) sender;
        }



        if (!sender.hasPermission(DefaultPermissions.BDC.Command.GIVE_CB)) {
            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command");
            return true;
        }

        if (arg1 == null) {
            ChatUtil.sendMessage(sender, "&cYou didn't specify a block!");
            return true;
        }

        if (!service.getStorage().containsAbstractCustomBlock(arg1)) {
            ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_BLOCK_NOT_EXISTS.replace("%customblock_name%", arg3));
            return true;
        }

        AbstractCustomBlock abstractCustomBlock = service.getStorage().getAbstractCustomBlock(arg1);
        ItemStack customBlockItem = abstractCustomBlock.getItem();

        byte amount = 1;

        if (arg3 != null) {
            try {
                amount = Byte.parseByte(arg3);
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

        if (arg2 == null) {
            if (player == null) {
                ChatUtil.sendMessage(sender, StringMessagesValue.COMMAND_CUSTOM_BLOCK_GIVE_NO_PLAYER);
                return true;
            } else {
                CommandUtil.applyPlaceholdersForItem(customBlockItem, player);
                CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, player);

                ItemUtil.distributeItem(player, customBlockItem);
                ChatUtil.sendMessage(player,
                        StringMessagesValue.COMMAND_CUSTOM_BLOCK_GIVE_PLAYER_RECEIVE
                                .replace("%customblock_name%", arg1)
                                .replace("%amount%", String.valueOf(amount))
                );
            }
        } else {
            switch (arg2) {
                case "@a" -> {
                    for (Player recipient : Bukkit.getOnlinePlayers()) {
                        CommandUtil.applyPlaceholdersForItem(customBlockItem, recipient);
                        CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);
                        ItemUtil.distributeItem(recipient, customBlockItem);
                    }
                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to all players", arg1, amount);
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
                    ChatUtil.sendMessage(sender, "&bYou have received the &l%s&ox%s&r&b block", arg1, amount);
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
                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to random player %s", arg1, amount, randomPlayer.getName());
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
                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to nearest player %s", arg1, amount, nearestPlayer.getName());
                    return true;
                }
                default -> {
                    Player recipient = Bukkit.getPlayer(arg2);

                    if (recipient == null) {
                        ChatUtil.sendMessage(sender, "&cYou specified a non-existent selector or player name");
                        return true;
                    }

                    CommandUtil.applyPlaceholdersForItem(customBlockItem, recipient);
                    CommandUtil.applyPlaceholdersForCommand(abstractCustomBlock, customBlockItem, recipient);
                    ItemUtil.distributeItem(recipient, customBlockItem);
                    ChatUtil.sendMessage(sender, "&bYou have given the &l%s&ox%s&r&b block to player %s", arg1, amount, recipient.getName());
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        List<String> blocksNames = service.getStorage().getNames();

        String arg1 = null;
        String arg2 = null;
        String arg3 = null;


        try {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }


        if (args.length == 1) {
            StringUtil.copyPartialMatches(arg1, blocksNames, result);
        } else if (args.length == 2) {
            List<String> tabComplete = new ArrayList<>();

            tabComplete.addAll(List.of(
                    "@a",
                    "@p",
                    "@r",
                    "@s"
            ));

            tabComplete.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());

            StringUtil.copyPartialMatches(arg2, tabComplete, result);
        } else if (args.length == 3) {
            List<String> amounts = new ArrayList<>();

            for (int i = 1; i <= 127; i++) {
                amounts.add(String.valueOf(i));
            }

            StringUtil.copyPartialMatches(arg3, amounts, result);
        }


        return result;
    }
}
