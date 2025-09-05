package me.general_breddok.blockdisplaycreator.commandparser;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.general_breddok.blockdisplaycreator.commandparser.argument.CommandArgument;
import me.general_breddok.blockdisplaycreator.commandparser.argument.MCCommandArgument;
import me.general_breddok.blockdisplaycreator.commandparser.exception.CommandParseException;
import me.general_breddok.blockdisplaycreator.commandparser.exception.InvalidCommandNameException;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.placeholder.universal.UniversalPlaceholder;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents a Minecraft command with its arguments.
 */

@Getter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED)
public class MCCommandLine implements CommandLine, DeepCloneable<MCCommandLine> {
    List<CommandArgument> arguments = new ArrayList<>();
    String name;
    @Nullable
    String namespace;

    /**
     * Constructs a MinecraftCommandLine object from a command string.
     *
     * @param commandLine The command string.
     */
    public MCCommandLine(@NotNull String commandLine) throws CommandParseException {
        if (commandLine.startsWith("/")) {
            commandLine = commandLine.substring(1);
        }

        if (commandLine.isEmpty()) {
            throw new InvalidCommandNameException("The command name cannot be empty", commandLine);
        }


        String[] split = commandLine.split("\\s+");

        String commandName = split[0];
        String key;

        String namespaceCandidate = null;
        if (commandName.contains(":")) {
            String[] parts = commandName.split(":", 2);
            if (parts.length > 2) {
                throw new InvalidCommandNameException("The command name cannot contain more than one ':'", commandLine);
            }
            namespaceCandidate = parts[0];
            key = parts[1];
        } else {
            key = commandName;
        }

        namespace = namespaceCandidate;

        name = key;

        arguments.addAll(Arrays.stream(split).skip(1).map(argument -> new MCCommandArgument(argument, this)).collect(Collectors.toCollection(ArrayList::new)));

        if (name.length() > 256) {
            throw new InvalidCommandNameException("The maximum length of a command name should not exceed 256 characters", commandLine);
        }
    }

    public MCCommandLine(List<CommandArgument> arguments, String name, @Nullable String namespace) {
        if (name.isEmpty()) {
            throw new InvalidCommandNameException("The command name cannot be empty");
        }
        this.arguments = arguments;
        this.name = name;
        this.namespace = namespace;
    }

    public MCCommandLine(CommandLine commandLine) {
        this(commandLine.getArguments(), commandLine.getName(), commandLine.getNamespace());
    }

    /**
     * Returns the argument at the specified index.
     *
     * @param index The index of the argument.
     * @return The argument at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public CommandArgument getArgument(int index) {
        try {
            return arguments.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public CommandLine setPlaceholders(Player player, PlaceholderAPIPlugin papi) {
        String stringCommand = toString();

        if (papi == null || player == null) {
            return this;
        } else {
            stringCommand = PlaceholderAPI.setPlaceholders(player, stringCommand);
        }

        return new MCCommandLine(stringCommand);
    }

    /**
     * Returns a string representation of the command.
     *
     * @return The string representation of the command.
     */
    @Override
    public String toString() {
        return (namespace == null ? "" : namespace + ":") + name + " " + MCCommandArgument.argsToString(arguments);
    }

    @Override
    public MCCommandLine clone() {
        return new MCCommandLine(this.arguments.stream().map(DeepCloneable::tryClone).collect(OperationUtil.toArrayList()), this.name, this.namespace);
    }

    @Override
    public void execute(CommandSender sender, UniversalPlaceholder<?>... placeholders) {
        if (sender == null) {
            return;
        }

        String stringCommand = this.toString();

        for (UniversalPlaceholder<?> placeholder : placeholders) {
            stringCommand = placeholder.apply(stringCommand);
        }

        Bukkit.dispatchCommand(sender, stringCommand);
    }
}
