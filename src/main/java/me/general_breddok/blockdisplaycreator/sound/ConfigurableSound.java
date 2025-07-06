package me.general_breddok.blockdisplaycreator.sound;

import org.bukkit.Sound;

public interface ConfigurableSound extends PlayableSound {
    Sound getSoundType();
    float getVolume();
    float getPitch();
}
