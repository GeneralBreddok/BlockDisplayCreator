package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a summoner capable of spawning a group of entities.
 * <p>
 * Extends {@link Summoner} to return a list of entities of type {@code E}.
 * Provides an additional method to apply a filter for selective spawning.
 * </p>
 *
 * @param <E> the type of entities being summoned
 */
public interface GroupSummoner<E> extends Summoner<List<E>> {

    /**
     * Summons a group of entities at the given location, optionally applying a filter.
     *
     * @param location the location where the entities should be spawned
     * @param filter   an optional predicate to apply to spawned entities
     * @return the list of summoned entities
     */
    List<E> summon(@NotNull Location location, @Nullable Predicate<E> filter);

    /**
     * Summons a group of entities at the given location with no filter applied.
     *
     * @param location the location where the entities should be spawned
     * @return the list of summoned entities
     */
    @Override
    default List<E> summon(@NotNull Location location) {
        return summon(location, null);
    }
}
