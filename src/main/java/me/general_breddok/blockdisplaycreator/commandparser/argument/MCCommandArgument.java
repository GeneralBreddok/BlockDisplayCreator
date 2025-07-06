package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED)
public class MCCommandArgument implements CommandArgument {
    String stringArg;
    @Getter
    CommandLine commandLine;

    public MCCommandArgument(@NotNull String stringArg, CommandLine commandLine) {
        this.stringArg = stringArg;
        this.commandLine = commandLine;
    }

    public MCCommandArgument(@NotNull CommandArgument argument, CommandLine commandLine) {
        this(argument.toString(), commandLine);
    }

    public static String argsToString(@NotNull Collection<? extends CommandArgument> commandArguments) {
        return commandArguments.stream().map(CommandArgument::toString).collect(Collectors.joining(" "));
    }

    public String toString() {
        return stringArg;
    }
}
