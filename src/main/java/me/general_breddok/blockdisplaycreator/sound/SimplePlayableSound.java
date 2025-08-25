package me.general_breddok.blockdisplaycreator.sound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Represents a simple implementation of a playable sound in Minecraft.
 * This class provides methods to play sounds at specific locations, for entities,
 * for a list of entities, or for entities in a world filtered by a predicate.
 * It also supports serialization and deserialization for configuration purposes.
 */
@Setter
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimplePlayableSound implements ConfigurationSerializable, PlayableSound {

    /**
     * The type of sound to be played.
     */
    @NotNull
    Sound soundType;

    /**
     * The volume of the sound.
     */
    float volume;

    /**
     * The pitch of the sound.
     */
    float pitch;

    /**
     * Constructs a SimplePlayableSound with default volume and pitch values.
     *
     * @param soundType the type of sound to be played
     */
    public SimplePlayableSound(@NotNull Sound soundType) {
        this.soundType = soundType;
        this.volume = 1f;
        this.pitch = 1f;
    }

    /**
     * Plays the sound at the specified location.
     *
     * @param location the location where the sound will be played
     */
    public void play(@NotNull Location location) {
        location.getWorld().playSound(location, soundType, volume, pitch);
    }

    /**
     * Plays the sound for the specified entity.
     * If the entity is a player, the sound is played directly to the player.
     * Otherwise, the sound is played at the entity's location.
     *
     * @param entity the entity for which the sound will be played
     */
    public void play(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            player.playSound(entity.getLocation(), soundType, volume, pitch);
        } else {
            entity.getWorld().playSound(entity.getLocation(), soundType, volume, pitch);
        }
    }

    /**
     * Plays the sound for a list of entities.
     *
     * @param entities the list of entities for which the sound will be played
     */
    public void play(@NotNull List<Entity> entities) {
        entities.forEach(entity -> play(entity.getLocation()));
    }

    /**
     * Plays the sound for entities in the specified world that match the given filter.
     *
     * @param world  the world in which the sound will be played
     * @param filter a predicate to filter entities; if null, the sound will not be played
     */
    public void play(@NotNull World world, @Nullable Predicate<Entity> filter) {
        world.getEntities().stream().filter(entity -> {
            if (filter != null) {
                return filter.test(entity);
            }
            return false;
        }).forEach(entity -> play(entity.getLocation()));
    }

    /**
     * Serializes the sound configuration into a map for storage or transmission.
     *
     * @return a map containing the serialized sound configuration
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("sound-type", soundType.name());
        serialized.put("volume", volume);
        serialized.put("pitch", pitch);
        return serialized;
    }

    /**
     * Deserializes a sound configuration from a map.
     *
     * @param serialized the map containing the serialized sound configuration
     * @return a SimplePlayableSound instance or null if the sound type is missing
     */
    public static SimplePlayableSound deserialize(Map<String, Object> serialized) {
        String soundTypeName = (String) serialized.get("sound-type");

        if (soundTypeName == null) {
            return null;
        }

        Sound soundType = Sound.valueOf(soundTypeName.toUpperCase());
        float volume = ((Number) serialized.getOrDefault("volume", 1.0f)).floatValue();
        float pitch = ((Number) serialized.getOrDefault("pitch", 1.0f)).floatValue();

        return new SimplePlayableSound(soundType, volume, pitch);
    }
}