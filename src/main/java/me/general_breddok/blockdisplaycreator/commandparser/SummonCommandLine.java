package me.general_breddok.blockdisplaycreator.commandparser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.argument.*;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandNameException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidNumberOfCommandArgumentsException;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SummonCommandLine extends MCCommandLine {
    EntityArgument summonEntity;
    CommandCoordinate x = new MCCommandCoordinate("~", this);
    CommandCoordinate y = new MCCommandCoordinate("~", this);
    CommandCoordinate z = new MCCommandCoordinate("~", this);
    NbtCommandArgument nbtArg = new NbtCommandArgument("{}", this);

    public SummonCommandLine(@NotNull String commandLine) throws IllegalArgumentException {
        super(commandLine);

        if (!this.name.equals("summon") && !this.name.equals("minecraft:summon")) {
            throw new InvalidCommandNameException("The string passed is not a summon command!", commandLine);
        }

        if (this.namespace == null || !this.namespace.equals("minecraft")) {
            this.namespace = "minecraft";
        }

        if (this.arguments.isEmpty()) {
            throw new InvalidNumberOfCommandArgumentsException("You didn't specify the entity to spawn!", commandLine);
        }

        this.summonEntity = new EntityArgument(getArgument(0), this);
        this.arguments.set(0, this.summonEntity);


        OperationUtil.doIfNotNull(getArgument(1), arg -> {
            this.x = new MCCommandCoordinate(arg, this);
            this.arguments.set(1, this.x);
        });
        OperationUtil.doIfNotNull(getArgument(2), arg -> {
            this.y = new MCCommandCoordinate(arg, this);
            this.arguments.set(2, this.y);
        });
        OperationUtil.doIfNotNull(getArgument(3), arg -> {
            this.z = new MCCommandCoordinate(arg, this);
            this.arguments.set(3, this.z);
        });


        OperationUtil.doIfNotNull(getArgument(4), arg -> {

            String strNbtArg = getArguments().stream().skip(4).map(CommandArgument::toString).collect(Collectors.joining());

            this.nbtArg = new NbtCommandArgument(strNbtArg, this);
            this.arguments.set(4, this.nbtArg);
        });
    }

    public SummonCommandLine(List<CommandArgument> arguments, String name, @Nullable String namespace, EntityArgument summonEntity, CommandCoordinate x, CommandCoordinate y, CommandCoordinate z, NbtCommandArgument nbtArg) {
        super(arguments, name, namespace);
        if (!this.name.equals("summon") && !this.name.equals("minecraft:summon")) {
            throw new InvalidCommandNameException("The string passed is not a summon command!");
        }

        if (this.namespace == null || !this.namespace.equals("minecraft")) {
            this.namespace = "minecraft";
        }

        if (this.arguments.isEmpty()) {
            throw new InvalidNumberOfCommandArgumentsException("You didn't specify the entity to spawn!");
        }
        this.summonEntity = summonEntity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nbtArg = nbtArg;
    }

    public SummonCommandLine(CommandLine commandLine) {
        super(commandLine);
    }

    public void setX(double x) {
        this.setX(new MCCommandCoordinate(x, this));
    }

    public void setY(double y) {
        this.setY(new MCCommandCoordinate(y, this));
    }

    public void setZ(double z) {
        this.setZ(new MCCommandCoordinate(z, this));
    }

    public void setCoordinates(@NotNull Location location) {
        this.setX(location.getX());
        this.setY(location.getY());
        this.setZ(location.getZ());
    }

    public void setSummonEntity(EntityType summonEntity) {
        setSummonEntity(new EntityArgument(summonEntity, this));
    }

    public void setSummonEntity(@NotNull EntityArgument summonEntity) {
        this.summonEntity = summonEntity;
        arguments.set(0, summonEntity);
    }


    public void setX(@NotNull MCCommandCoordinate x) {
        this.x = x;
        arguments.set(1, x);
    }

    public void setY(@NotNull MCCommandCoordinate y) {
        this.y = y;
        arguments.set(2, y);
    }

    public void setZ(@NotNull MCCommandCoordinate z) {
        this.z = z;
        arguments.set(3, z);
    }

    public void setNbtArg(@NotNull NbtCommandArgument nbtArg) {
        this.nbtArg = nbtArg;
        arguments.set(4, nbtArg);
    }

    @Override
    public String toString() {
        return (namespace == null ? "" : namespace + ":") +
                name +
                " " +
                summonEntity.toString().toLowerCase() +
                " " +
                x +
                " " +
                y +
                " " +
                z +
                " " +
                nbtArg;

    }

    @Override
    public SummonCommandLine clone() {
        return new SummonCommandLine(
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
