package me.general_breddok.blockdisplaycreator.commandparser;

import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandNameException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidNumberOfCommandArgumentsException;
import org.jetbrains.annotations.NotNull;

public class ExecuteCommandLine extends MCCommandLine {
    public ExecuteCommandLine(@NotNull String commandLine) throws IllegalArgumentException {
        super(commandLine);

        if (!getName().equals("execute")) {
            throw new InvalidCommandNameException("The string passed is not a execute command!", commandLine);
        }

        if (getArguments().size() < 2) {
            throw new InvalidNumberOfCommandArgumentsException("The execute command must have at least 2 arguments", commandLine);
        }
    }
}
