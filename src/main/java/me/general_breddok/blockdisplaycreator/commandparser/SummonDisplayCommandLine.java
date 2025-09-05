package me.general_breddok.blockdisplaycreator.commandparser;

import me.general_breddok.blockdisplaycreator.commandparser.argument.CommandArgument;
import me.general_breddok.blockdisplaycreator.commandparser.argument.CommandCoordinate;
import me.general_breddok.blockdisplaycreator.commandparser.argument.EntityArgument;
import me.general_breddok.blockdisplaycreator.commandparser.argument.NbtCommandArgument;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandArgumentException;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.display.DisplaySummoner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SummonDisplayCommandLine extends SummonCommandLine {
    public SummonDisplayCommandLine(@NotNull String commandLine) throws IllegalArgumentException {
        super(commandLine);

        if (!DisplaySummoner.isDisplayEntity(getSummonEntity().getType())) {
            throw new InvalidCommandArgumentException(getSummonEntity().toString() + " is not a display entity!", commandLine);
        }
    }

    public SummonDisplayCommandLine(List<CommandArgument> arguments, String name, @Nullable String namespace, EntityArgument summonEntity, CommandCoordinate x, CommandCoordinate y, CommandCoordinate z, NbtCommandArgument nbtArg) {
        super(arguments, name, namespace, summonEntity, x, y, z, nbtArg);
        if (!DisplaySummoner.isDisplayEntity(getSummonEntity().getType())) {
            throw new InvalidCommandArgumentException(getSummonEntity().toString() + " is not a display entity!");
        }
    }

    @Override
    public SummonDisplayCommandLine clone() {
        return new SummonDisplayCommandLine(
                this.arguments.stream().map(DeepCloneable::tryClone).collect(Collectors.toCollection(ArrayList::new)),
                this.name,
                this.namespace,
                this.summonEntity,
                this.x,
                this.y,
                this.z,
                this.nbtArg
        );
    }
}
