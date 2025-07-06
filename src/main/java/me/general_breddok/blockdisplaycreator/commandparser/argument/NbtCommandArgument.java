package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.exception.CommandParseException;
import me.general_breddok.blockdisplaycreator.nbt.NbtContainer;
import me.general_breddok.blockdisplaycreator.nbt.NbtObject;
import me.general_breddok.blockdisplaycreator.nbt.exception.NbtParseException;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NbtCommandArgument extends MCCommandArgument {
    NbtContainer container;

    public NbtCommandArgument(@NotNull String strNbt, CommandLine commandLine) {
        super(strNbt, commandLine);
        try {
            container = new NbtObject(strNbt);
        } catch (NbtParseException e) {
            throw new CommandParseException(e, commandLine.toString());
        }
    }

    @Override
    public String toString() {
        return container.toString();
    }
}
