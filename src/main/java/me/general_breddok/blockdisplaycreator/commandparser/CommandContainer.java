package me.general_breddok.blockdisplaycreator.commandparser;

import me.general_breddok.blockdisplaycreator.placeholder.universal.UniversalPlaceholder;
import org.bukkit.command.CommandSender;

public interface CommandContainer {
    void execute(CommandSender sender, UniversalPlaceholder<?>... placeholders);
}
