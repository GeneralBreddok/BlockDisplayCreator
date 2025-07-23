package me.general_breddok.blockdisplaycreator.sound;

import org.bukkit.Sound;

/**
 * Represents a configurable sound that can be played in various contexts within a Minecraft world.
 * Extends the {@link PlayableSound} interface to provide additional configuration options
 * such as sound type, volume, and pitch.
 */
public interface ConfigurableSound extends PlayableSound {

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
}
