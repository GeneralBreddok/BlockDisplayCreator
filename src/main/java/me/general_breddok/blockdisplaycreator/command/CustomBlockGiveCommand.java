package me.general_breddok.blockdisplaycreator.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
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
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.placeholder.universal.PlayerSkinBase64Placeholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomBlockGiveCommand {
    final BlockDisplayCreator plugin;
    List<AbstractCustomBlockTooltip> abstractCustomBlockTooltips = new ArrayList<>();

    public CustomBlockGiveCommand(BlockDisplayCreator plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("custom-block")
                .withPermission("bdc.command.custom-block")
                .withAliases("cb")
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
                }).register();
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
}
