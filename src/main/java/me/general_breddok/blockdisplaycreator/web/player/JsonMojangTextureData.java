package me.general_breddok.blockdisplaycreator.web.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonMojangTextureData implements MojangTextureData {
    String profileName;
    String profileId;
    SkinInfo skin;
    TextureInfo cape;

    public JsonMojangTextureData(JsonObject jsonObject) throws URISyntaxException, MalformedURLException {
        this.profileName = Optional.ofNullable(jsonObject.get("profileName"))
                .map(JsonElement::getAsString)
                .orElse(null);

        this.profileId = Optional.ofNullable(jsonObject.get("profileId"))
                .map(JsonElement::getAsString)
                .orElse(null);

        JsonObject texturesObject = jsonObject.getAsJsonObject("textures");

        if (texturesObject.has("SKIN")) {
            JsonObject skinObject = texturesObject.getAsJsonObject("SKIN");
            this.skin = new JsonSkinInfo(skinObject);
        } else {
            this.skin = null;
        }

        if (texturesObject.has("CAPE")) {
            JsonObject capeObject = texturesObject.getAsJsonObject("CAPE");
            this.cape = new JsonTextureInfo(capeObject);
        } else {
            this.cape = null;
        }
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class JsonSkinInfo extends JsonTextureInfo implements SkinInfo {
        TextureMetadata metadata;

        public JsonSkinInfo(JsonObject skinObject) throws MalformedURLException, URISyntaxException {
            super(skinObject);

            if (skinObject.has("metadata")) {
                JsonObject metadataObject = skinObject.getAsJsonObject("metadata");
                this.metadata = new JsonTextureMetadata(metadataObject);
            } else {
                this.metadata = null;
            }
        }
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class JsonTextureInfo implements TextureInfo {
        URL url;

        public JsonTextureInfo(JsonObject textureObject) throws MalformedURLException, URISyntaxException {
            this.url = new URI(textureObject.get("url").getAsString()).toURL();
        }
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class JsonTextureMetadata implements TextureMetadata {
        PlayerTextures.SkinModel model;

        public JsonTextureMetadata(JsonObject metadataObject) {
            String modelString = metadataObject.has("model") ? metadataObject.get("model").getAsString() : "classic";
            this.model = modelString.equals("slim") ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC;
        }
    }
}
