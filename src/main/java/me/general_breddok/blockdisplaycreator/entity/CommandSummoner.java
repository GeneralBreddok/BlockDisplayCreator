package me.general_breddok.blockdisplaycreator.entity;

import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface CommandSummoner<E> extends GroupSummoner<E> {

    List<CommandLine> getCommands();
    void setCommands(List<CommandLine> commands);

    List<E> summon(@NotNull Location location, @NotNull Entity commandSender, @Nullable Predicate<E> filter);

    @Override
    default List<E> summon(@NotNull Location location, @Nullable Predicate<E> filter) {
        return summon(location, location.getWorld().getEntities().get(0), filter);
    }
}
