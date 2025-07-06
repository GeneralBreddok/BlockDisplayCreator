package me.general_breddok.blockdisplaycreator.web.player;

import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public interface MojangTextureData {
    @Nullable
    String getProfileName();

    @Nullable
    String getProfileId();

    @Nullable
    SkinInfo getSkin();

    @Nullable
    TextureInfo getCape();

    interface SkinInfo extends TextureInfo {
        @Nullable
        TextureMetadata getMetadata();
    }

    interface TextureInfo {
        URL getUrl();
    }

    interface TextureMetadata {
        PlayerTextures.SkinModel getModel();
    }
}
