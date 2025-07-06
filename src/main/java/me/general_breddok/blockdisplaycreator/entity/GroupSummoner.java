package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface GroupSummoner<E> extends Summoner<List<E>> {
    List<E> summon(@NotNull Location location, @Nullable Predicate<E> filter);

    @Override
    default List<E> summon(@NotNull Location location) {
        return summon(location, null);
    }
}
