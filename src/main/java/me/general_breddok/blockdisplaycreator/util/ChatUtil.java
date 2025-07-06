package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Utility class for handling chat related operations.
 *
 * @author Genral_Breddok
 * @since 1.0.0
 */
@UtilityClass
public class ChatUtil {
    /**
     * Console command sender instance.
     */
    public final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");


    public <T> Consumer<? super T> logConsumer() {
        return (Consumer<T>) t -> ChatUtil.log(t.toString());
    }

    public <T> Consumer<? super T> logConsumer(String before) {
        return (Consumer<T>) t -> ChatUtil.log(before + t.toString());
    }

    public String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', HEX_PATTERN.matcher(message).replaceAll("&x&$1&$2&$3&$4&$5&$6"));
    }

    /**
     * Sends a message to a command sender.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     */
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Sends a formatted message to a command sender.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     * @param args    The arguments for the message format.
     */
    public void sendMessage(CommandSender sender, String message, Object... args) {
        sendMessage(sender, String.format(message, args));
    }

    /**
     * Logs a message to the console.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        sendMessage(CONSOLE, message);
    }

    /**
     * Logs a formatted message to the console.
     *
     * @param message The message to log.
     * @param args    The arguments for the message format.
     */
    public void log(String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), message, args);
    }

    /**
     * Sends a message to a list of players.
     *
     * @param players The list of players.
     * @param message The message to send.
     */
    public void sendMessage(List<Player> players, String message) {
        players.forEach(player -> sendMessage(player, message));
    }

    /**
     * Sends a formatted message to a list of players.
     *
     * @param players The list of players.
     * @param message The message to send.
     * @param args    The arguments for the message format.
     */
    public void sendMessage(List<Player> players, String message, Object... args) {
        sendMessage(players, format(message, args));
    }

    /**
     * Sends a message to all players in a world.
     *
     * @param world   The world.
     * @param message The message to send.
     */
    public void sendMessage(World world, String message) {
        sendMessage(world.getPlayers(), message);
    }

    /**
     * Sends a formatted message to all players in a world.
     *
     * @param world   The world.
     * @param message The message to send.
     * @param args    The arguments for the message format.
     */
    public void sendMessage(World world, String message, Object... args) {
        sendMessage(world, format(message, args));
    }

    /**
     * Sends a message to all online players on a server.
     *
     * @param server  The server.
     * @param message The message to send.
     */
    public void sendMessage(Server server, String message) {
        sendMessage((List<Player>) server.getOnlinePlayers(), message);
    }

    /**
     * Sends a formatted message to all online players on a server.
     *
     * @param server  The server.
     * @param message The message to send.
     * @param args    The arguments for the message format.
     */
    public void sendMessage(Server server, String message, Object... args) {
        sendMessage(server, format(message, args));
    }

    /**
     * Formats a message using String.format.
     *
     * @param message The message to format.
     * @param args    The arguments for the message format.
     * @return The formatted message.
     */
    public String format(String message, Object... args) {
        return String.format(message, args);
    }

    /**
     * Dispatches a command to a command sender.
     *
     * @param sender      The command sender.
     * @param commandLine The command line to dispatch.
     */
    public boolean sendCommand(CommandSender sender, String commandLine) {
        return Bukkit.dispatchCommand(sender, commandLine);
    }

    /**
     * Dispatches a command to the console.
     *
     * @param commandLine The command line to dispatch.
     */
    public boolean sendConsoleCommand(String commandLine) {
        return sendCommand(CONSOLE, commandLine);
    }

    /**
     * Dispatches a command to a command sender with arguments.
     *
     * @param sender      The command sender.
     * @param commandName The command name.
     * @param commandArgs The command arguments.
     */
    public boolean sendCommand(CommandSender sender, String commandName, Object... commandArgs) {
        return sendCommand(sender, commandName + " " + String.join(" ", Arrays.stream(commandArgs).map(Object::toString).toList()));
    }

    /**
     * Dispatches a command to the console with arguments.
     *
     * @param commandName The command name.
     * @param commandArgs The command arguments.
     */
    public boolean sendConsoleCommand(String commandName, Object... commandArgs) {
        return sendCommand(CONSOLE, commandName, commandArgs);
    }

    /**
     * Sets placeholders in a message using PlaceholderAPI.
     *
     * @param player     The player for whom placeholders are being set.
     * @param string     The message with placeholders.
     * @param papiPlugin The PlaceholderAPI plugin instance.
     * @return The message with placeholders set.
     */
    public String setPlaceholders(Player player, String string, Plugin papiPlugin) {
        if (Objects.isNull(papiPlugin))
            return string;

        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
