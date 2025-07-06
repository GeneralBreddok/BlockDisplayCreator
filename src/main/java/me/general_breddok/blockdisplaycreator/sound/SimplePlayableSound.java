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

@Setter
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimplePlayableSound implements ConfigurationSerializable, ConfigurableSound {

    @NotNull
    Sound soundType;
    float volume;
    float pitch;

    public SimplePlayableSound(@NotNull Sound soundType) {
        this.soundType = soundType;
        this.volume = 1f;
        this.pitch = 1f;
    }

    public void play(@NotNull Location location) {
        location.getWorld().playSound(location, soundType, volume, pitch);
    }

    public void play(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            player.playSound(entity.getLocation(), soundType, volume, pitch);
        } else {
            entity.getWorld().playSound(entity.getLocation(), soundType, volume, pitch);
        }
    }

    public void play(@NotNull List<Entity> entities) {
        entities.forEach(entity -> play(entity.getLocation()));
    }

    public void play(@NotNull World world, @Nullable Predicate<Entity> filter) {

        world.getEntities().stream().filter(entity -> {
            if (filter != null) {
                return filter.test(entity);
            }
            return false;
        }).forEach(entity -> play(entity.getLocation()));
    }
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("sound-type", soundType.name());
        serialized.put("volume", volume);
        serialized.put("pitch", pitch);
        return serialized;
    }

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
