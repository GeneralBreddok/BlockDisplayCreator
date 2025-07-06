package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.custom.CommandBundle;
import org.jetbrains.annotations.Nullable;

public interface CustomBlockStageSettings {
    @Nullable
    CommandBundle getPlaceCommandBundle();
    @Nullable
    CommandBundle getBreakCommandBundle();
}
