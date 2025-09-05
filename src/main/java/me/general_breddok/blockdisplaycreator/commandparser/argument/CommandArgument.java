package me.general_breddok.blockdisplaycreator.commandparser.argument;

import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;

public interface CommandArgument {
    CommandLine getCommandLine();

    @Override
    String toString();
}
