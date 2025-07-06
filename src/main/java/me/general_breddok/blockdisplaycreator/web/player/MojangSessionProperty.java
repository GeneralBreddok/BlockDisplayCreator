package me.general_breddok.blockdisplaycreator.web.player;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public interface MojangSessionProperty {
    String getName();

    String getValue();

    @Nullable
    MojangTextureData decodeValue();

    @Nullable
    String getSignature();
}
