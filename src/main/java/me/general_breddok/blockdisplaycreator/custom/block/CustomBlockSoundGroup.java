package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.sound.PlayableSound;
import org.jetbrains.annotations.Nullable;


public interface CustomBlockSoundGroup {
    @Nullable
    PlayableSound getPlaceSound();

    void setPlaceSound(PlayableSound sound);

    @Nullable
    PlayableSound getBreakSound();

    void setBreakSound(PlayableSound sound);
}
