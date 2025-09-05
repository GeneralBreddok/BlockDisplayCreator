package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a generic entity summoner.
 * <p>
 * Provides a contract for creating or spawning an entity of type {@code E}
 * at a specified {@link Location}.
 * </p>
 *
 * @param <E> the type of entity being summoned
 */
public interface Summoner<E> {

    /**
     * Summons an entity of type {@code E} at the given location.
     *
     * @param location the location where the entity should be spawned
     * @return the summoned entity instance
     */
    E summon(@NotNull Location location);
}
