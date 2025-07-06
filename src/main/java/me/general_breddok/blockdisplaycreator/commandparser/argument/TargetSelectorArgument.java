package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandArgumentException;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TargetSelectorArgument extends MCCommandArgument {
    TargetSelectorType type;
    String options;

    public TargetSelectorArgument(@NotNull String stringArg, CommandLine commandLine) {
        super(stringArg, commandLine);

        if (stringArg.length() < 2) {
            throw new InvalidCommandArgumentException("The specified argument is not a selector!");
        }

        String strSelector = stringArg.substring(0, 2);
        String strOptions = stringArg.substring(2);

        if (!TargetSelectorType.isSelector(strSelector)) {
            throw new InvalidCommandArgumentException("The specified argument is not a selector!");
        }

        this.type = TargetSelectorType.parseString(stringArg);

        if (strOptions.isEmpty()) {
            this.options = "[]";
        } else if (strOptions.length() >= 2 && strOptions.charAt(0) == '[' && strOptions.charAt(strOptions.length() - 1) == ']') {
            this.options = strOptions;
        }
    }

    public TargetSelectorArgument(@NotNull CommandArgument argument, CommandLine commandLine) {
        super(argument, commandLine);
    }
}
