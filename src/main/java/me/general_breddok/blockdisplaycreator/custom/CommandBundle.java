package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.commandparser.CommandContainer;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;

import java.util.List;


public interface CommandBundle extends CommandContainer {
    List<CommandLine> getCommands();

    void setCommands(List<CommandLine> commands);

    CommandSource getCommandSource();

    void setCommandSource(CommandSource commandSource);

    List<String> getGrantedCommandPermissions();

    void setGrantedCommandPermissions(List<String> grantedCommandPermissions);

    enum CommandSource {
        PLAYER,
        CONSOLE
    }
}
