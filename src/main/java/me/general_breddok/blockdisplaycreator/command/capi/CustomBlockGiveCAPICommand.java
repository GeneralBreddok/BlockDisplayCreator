package me.general_breddok.blockdisplaycreator.command.capi;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockStorage;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomBlockGiveCAPICommand {
    final BlockDisplayCreator plugin;

    public CustomBlockGiveCAPICommand(BlockDisplayCreator plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("custom-block")
                .withPermission("bdc.command.custom-block")
                .withAliases("cb")
                .withShortDescription("Gives a custom block to a player")
                .withFullDescription("Gives a custom block to a player. You can specify the receiver and the amount of blocks to give. ")
                .withUsage(
                        "/cb <block> [receiver] [amount]"
                )
                .withArguments(
                        new TextArgument("block")
                                .replaceSuggestions(this.plugin.getBdcCommand().getCustomBlockSuggestions())
                ).withOptionalArguments(
                        new EntitySelectorArgument.ManyPlayers("receiver"),
                        new IntegerArgument("amount", 1, 127)
                ).executes((sender, args) -> {
                    String block = (String) args.get("block");
                    int amount = (int) args.getOrDefault("amount", 1);

                    Collection<Player> receivers = (Collection<Player>) args.get("receiver");

                    CustomBlockStorage storage = this.plugin.getCustomBlockService().getStorage();

                    if (!storage.containsAbstractCustomBlock(block)) {
                        ChatUtil.sendMessage(sender, "&cBlock %s does not exist!", block);
                        return;
                    }

                    AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(block);

                    ItemStack item = abstractCustomBlock.getItem();
                    item.setAmount(amount);

                    if (receivers == null) {
                        if (sender instanceof Player player) {

                            this.plugin.getBdcCommand().applyPlaceholdersForItem(item, player);
                            this.plugin.getBdcCommand().applyPlaceholdersForCommand(abstractCustomBlock, item, player);

                            ItemUtil.distributeItem(player, item);
                            ChatUtil.sendMessage(player, "&bYou have received the &l%s&ox%s&r&b block", block, amount);
                        } else {
                            throw CommandAPI.failWithString("You have not specified the block receiver!");
                        }
                    } else {
                        for (Player receiver : receivers) {
                            this.plugin.getBdcCommand().applyPlaceholdersForItem(item, receiver);
                            this.plugin.getBdcCommand().applyPlaceholdersForCommand(abstractCustomBlock, item, receiver);

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

                }).register();
    }
}
