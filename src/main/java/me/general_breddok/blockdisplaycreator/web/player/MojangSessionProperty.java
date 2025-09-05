package me.general_breddok.blockdisplaycreator.web.player;

import org.jetbrains.annotations.Nullable;

public interface MojangSessionProperty {
    String getName();

    String getValue();

    @Nullable
    MojangTextureData decodeValue();

    @Nullable
    String getSignature();
}
