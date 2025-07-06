package me.general_breddok.blockdisplaycreator.custom;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.placeholder.universal.UniversalPlaceholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandBundleDta implements CommandBundle {
    List<CommandLine> commands;
    CommandSource commandSource;
    List<String> grantedCommandPermissions;

    @Override
    public void execute(CommandSender sender, UniversalPlaceholder<?>... placeholders) {
        if (sender == null) {
            return;
        }

        if (this.commands.isEmpty()) {
            return;
        }

        List<String> disablePermissions = new ArrayList<>();

        if (!this.grantedCommandPermissions.isEmpty()) {
            for (String grantedPermission : this.grantedCommandPermissions) {
                if (!sender.hasPermission(grantedPermission)) {
                    disablePermissions.add(grantedPermission);
                    sender.addAttachment(BlockDisplayCreator.getInstance(), grantedPermission, true);
                }
            }
        }


        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }


        for (CommandLine command : this.commands) {
            if (command instanceof MCCommandLine mcl) {
                command = mcl.setPlaceholders(player, BlockDisplayCreator.getPlaceholderApi() == null ? null : PlaceholderAPIPlugin.getInstance());
            }

            if (this.commandSource == CommandBundle.CommandSource.PLAYER) {
                command.execute(sender, placeholders);
            } else {
                command.execute(ChatUtil.CONSOLE, placeholders);
            }
        }



        if (!disablePermissions.isEmpty()) {
            for (String disablePermission : disablePermissions) {
                sender.addAttachment(BlockDisplayCreator.getInstance(), disablePermission, false);
            }
        }
    }
}
