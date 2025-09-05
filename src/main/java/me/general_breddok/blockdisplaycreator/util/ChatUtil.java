package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
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
import java.util.regex.Pattern;


/**
 * Utility class for handling chat messages, logging, commands, and placeholders.
 * <p>
 * Provides convenience methods to send formatted and colored messages
 * to players, worlds, servers, and the console, as well as to execute commands
 * and apply PlaceholderAPI placeholders.
 * </p>
 */
@UtilityClass
public class ChatUtil {

    /**
     * The console command sender instance.
     */
    public final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    /**
     * Pattern for matching hex color codes in chat messages.
     */
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");

    /**
     * Applies color formatting (including hex codes) to a message.
     *
     * @param message the raw message
     * @return the colored message
     */
    public String color(String message) {
        return ChatColor.translateAlternateColorCodes(
                '&',
                HEX_PATTERN.matcher(message).replaceAll("&x&$1&$2&$3&$4&$5&$6")
        );
    }

    /**
     * Sends a formatted message to a command sender.
     *
     * @param sender  the recipient
     * @param message the message
     */
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Sends a formatted message with arguments to a command sender.
     *
     * @param sender  the recipient
     * @param message the format string
     * @param args    the arguments
     */
    public void sendMessage(CommandSender sender, String message, Object... args) {
        sendMessage(sender, String.format(message, args));
    }

    /**
     * Sends a message to the console.
     *
     * @param message the message
     */
    public void log(String message) {
        sendMessage(CONSOLE, message);
    }

    /**
     * Sends a formatted message with arguments to the console.
     *
     * @param message the format string
     * @param args    the arguments
     */
    public void log(String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), message, args);
    }

    /**
     * Sends a message to a list of players.
     *
     * @param players the recipients
     * @param message the message
     */
    public void sendMessage(List<Player> players, String message) {
        players.forEach(player -> sendMessage(player, message));
    }

    /**
     * Sends a formatted message with arguments to a list of players.
     *
     * @param players the recipients
     * @param message the format string
     * @param args    the arguments
     */
    public void sendMessage(List<Player> players, String message, Object... args) {
        sendMessage(players, format(message, args));
    }

    /**
     * Sends a message to all players in a world.
     *
     * @param world   the world
     * @param message the message
     */
    public void sendMessage(World world, String message) {
        sendMessage(world.getPlayers(), message);
    }

    /**
     * Sends a formatted message with arguments to all players in a world.
     *
     * @param world   the world
     * @param message the format string
     * @param args    the arguments
     */
    public void sendMessage(World world, String message, Object... args) {
        sendMessage(world, format(message, args));
    }

    /**
     * Sends a message to all players on the server.
     *
     * @param server  the server
     * @param message the message
     */
    public void sendMessage(Server server, String message) {
        sendMessage((List<Player>) server.getOnlinePlayers(), message);
    }

    /**
     * Sends a formatted message with arguments to all players on the server.
     *
     * @param server  the server
     * @param message the format string
     * @param args    the arguments
     */
    public void sendMessage(Server server, String message, Object... args) {
        sendMessage(server, format(message, args));
    }

    /**
     * Formats a message with arguments.
     *
     * @param message the format string
     * @param args    the arguments
     * @return the formatted string
     */
    public String format(String message, Object... args) {
        return String.format(message, args);
    }

    /**
     * Executes a command as the given sender.
     *
     * @param sender      the sender executing the command
     * @param commandLine the command line
     * @return true if the command was successful, false otherwise
     */
    public boolean sendCommand(CommandSender sender, String commandLine) {
        return Bukkit.dispatchCommand(sender, commandLine);
    }

    /**
     * Executes a command as the console.
     *
     * @param commandLine the command line
     * @return true if the command was successful, false otherwise
     */
    public boolean sendConsoleCommand(String commandLine) {
        return sendCommand(CONSOLE, commandLine);
    }

    /**
     * Executes a command with arguments as the given sender.
     *
     * @param sender      the sender executing the command
     * @param commandName the command name
     * @param commandArgs the command arguments
     * @return true if the command was successful, false otherwise
     */
    public boolean sendCommand(CommandSender sender, String commandName, Object... commandArgs) {
        return sendCommand(sender, commandName + " " + String.join(" ", Arrays.stream(commandArgs).map(Object::toString).toList()));
    }

    /**
     * Executes a command with arguments as the console.
     *
     * @param commandName the command name
     * @param commandArgs the command arguments
     * @return true if the command was successful, false otherwise
     */
    public boolean sendConsoleCommand(String commandName, Object... commandArgs) {
        return sendCommand(CONSOLE, commandName, commandArgs);
    }

    /**
     * Applies PlaceholderAPI placeholders to a string if the plugin is available.
     *
     * @param player     the player context
     * @param string     the message string
     * @param papiPlugin the PlaceholderAPI plugin instance (nullable)
     * @return the string with placeholders applied, or unchanged if not available
     */
    public String setPlaceholders(Player player, String string, Plugin papiPlugin) {
        if (Objects.isNull(papiPlugin)) {
            return string;
        }
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
