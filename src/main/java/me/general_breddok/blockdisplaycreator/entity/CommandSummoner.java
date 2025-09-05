package me.general_breddok.blockdisplaycreator.entity;

import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a group summoner that can execute commands upon summoning entities.
 * <p>
 * Extends {@link GroupSummoner} to include command handling capabilities
 * and supports summoning with a specific command sender entity context.
 * </p>
 *
 * @param <E> the type of entities being summoned
 */
public interface CommandSummoner<E> extends GroupSummoner<E> {

    /**
     * Gets the list of commands that will be executed when summoning entities.
     *
     * @return list of {@link CommandLine} objects representing commands
     */
    List<CommandLine> getCommands();

    /**
     * Sets the commands to be executed when summoning entities.
     *
     * @param commands list of {@link CommandLine} objects
     */
    void setCommands(List<CommandLine> commands);

    /**
     * Summons a group of entities at the given location with a specific command sender
     * and an optional filter.
     *
     * @param location      the location where entities should be summoned
     * @param commandSender the entity considered as the sender of commands
     * @param filter        optional predicate to filter summoned entities
     * @return the list of summoned entities
     */
    List<E> summon(@NotNull Location location, @NotNull Entity commandSender, @Nullable Predicate<E> filter);

    /**
     * Summons a group of entities at the given location applying an optional filter.
     * <p>
     * By default, uses the first entity in the world as the command sender.
     * </p>
     *
     * @param location the location where entities should be summoned
     * @param filter   optional predicate to filter summoned entities
     * @return the list of summoned entities
     */
    @Override
    default List<E> summon(@NotNull Location location, @Nullable Predicate<E> filter) {
        return summon(location, location.getWorld().getEntities().get(0), filter);
    }
}

