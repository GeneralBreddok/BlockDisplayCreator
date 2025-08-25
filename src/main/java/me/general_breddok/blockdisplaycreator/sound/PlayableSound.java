package me.general_breddok.blockdisplaycreator.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;


/**
 * Represents a sound that can be played in various contexts within a Minecraft world.
 * Provides methods to play the sound at a specific location, for a specific entity,
 * for a list of entities, or for entities in a world filtered by a predicate.
 */
public interface PlayableSound {
    /**
     * Retrieves the type of sound to be played.
     *
     * @return the {@link Sound} type
     */
    Sound getSoundType();

    /**
     * Retrieves the volume of the sound.
     *
     * @return the volume as a float
     */
    float getVolume();

    /**
     * Retrieves the pitch of the sound.
     *
     * @return the pitch as a float
     */
    float getPitch();
    /**
     * Plays the sound at the specified location.
     *
     * @param location the location where the sound will be played
     */
    void play(@NotNull Location location);

    /**
     * Plays the sound for the specified entity.
     *
     * @param entity the entity for which the sound will be played
     */
    void play(@NotNull Entity entity);

    /**
     * Plays the sound for a list of entities.
     *
     * @param entities the list of entities for which the sound will be played
     */
    void play(@NotNull List<Entity> entities);

    /**
     * Plays the sound for entities in the specified world that match the given filter.
     *
     * @param world  the world in which the sound will be played
     * @param filter a predicate to filter entities; if null, the sound will be played for all entities
     */
    void play(@NotNull World world, @Nullable Predicate<Entity> filter);
}
