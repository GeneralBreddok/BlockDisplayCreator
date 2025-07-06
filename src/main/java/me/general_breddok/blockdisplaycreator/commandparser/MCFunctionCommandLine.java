package me.general_breddok.blockdisplaycreator.commandparser;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.commandparser.exception.CommandParseException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandNameException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidNumberOfCommandArgumentsException;
import org.apache.logging.log4j.message.MessageCollectionMessage;
import org.jetbrains.annotations.NotNull;

@Getter
public class MCFunctionCommandLine extends MCCommandLine {
    String modulePackName;
    String functionPath;

    public MCFunctionCommandLine(@NotNull String commandLine) throws CommandParseException {
        super(commandLine);

        if (!getName().equals("function")) {
            throw new InvalidCommandNameException("The string passed is not a function command!", commandLine);
        }

        if (getArguments().isEmpty()) {
            throw new InvalidNumberOfCommandArgumentsException("The function command must have arguments", commandLine);
        }

        String[] split = this.arguments.get(0).toString().split(":");

        this.modulePackName = split[0];
        this.functionPath = split[1];
    }
}
