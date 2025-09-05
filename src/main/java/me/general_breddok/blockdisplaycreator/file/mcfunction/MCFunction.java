package me.general_breddok.blockdisplaycreator.file.mcfunction;

import me.general_breddok.blockdisplaycreator.commandparser.CommandContainer;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;

import java.nio.file.Path;
import java.util.List;

public interface MCFunction extends CommandContainer {
    Path getPath();

    List<CommandLine> getCommands();

    CommandLine getCommand(int index);

    void addCommand(CommandLine commandLine);

    void removeCommand(int index);

    void removeCommand(CommandLine commandLine);

    void setCommand(int index, CommandLine commandLine);
}
