package me.general_breddok.blockdisplaycreator.web.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.annotation.UMF;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.web.exception.InvalidResponseException;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@UtilityClass
@UMF(module = "me.general_breddok.blockdisplaycreator.web.player")
public class PlayerProfiles {
    private final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");

    public static String encodeSkinUrl(URL skinUrl) {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        JsonObject skin = new JsonObject();

        skin.add("url", new JsonPrimitive(skinUrl.toString()));
        textures.add("SKIN", skin);
        root.add("textures", textures);

        String json = root.toString();
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    public PlayerProfile getProfileByUrl(String url) {
        URL skinUrl = buildSkinUrl(url);
        return getPlayerProfile(skinUrl);
    }

    @Nullable
    public PlayerProfile getProfileByBase64(String base64) {
        URL skinUrl = decodeSkinUrlFromBase64(base64);
        return skinUrl != null ? getPlayerProfile(skinUrl) : null;
    }

    @Nullable
    public PlayerProfile getProfileById(String id) {
        MojangSession session = getSession(id);
        if (session == null) {
            return null;
        }
        return session.getProperties().stream()
                .filter(prop -> "textures".equals(prop.getName()))
                .findFirst()
                .map(MojangSessionProperty::getValue)
                .map(PlayerProfiles::getProfileByBase64)
                .orElse(null);
    }

    @Nullable
    public PlayerProfile getProfileByName(String playerName) {
        MojangProfile profile = getMojangProfile(playerName);
        if (profile == null) {
            return null;
        }
        return getProfileById(profile.getId());
    }

    @NotNull
    public static PlayerProfile getPlayerProfile(URL skin) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(skin);
        profile.setTextures(textures);
        return profile;
    }

    @Nullable
    public MojangProfile getMojangProfile(String playerName) {
        try {
            return new NetworkPlayerProfile(playerName);
        } catch (InvalidResponseException e) {
            return null;
        }
    }

    @Nullable
    public MojangSession getSession(String mojangUuid) {
        try {
            return new NetworkPlayerSession(mojangUuid);
        } catch (InvalidResponseException e) {
            return null;
        }
    }

    @Nullable
    public URL getSkinUrl(String playerName) {
        MojangProfile profile = getMojangProfile(playerName);
        if (profile == null) {
            return null;
        }
        MojangSession session = getSession(profile.getId());
        if (session == null) {
            return null;
        }
        return session.getProperties().stream()
                .filter(prop -> prop.getName().equals("textures"))
                .findFirst()
                .map(MojangSessionProperty::decodeValue)
                .map(MojangTextureData::getSkin)
                .map(MojangTextureData.SkinInfo::getUrl)
                .orElse(null);
    }

    private URL buildSkinUrl(String url) {
        if (!url.startsWith("http://textures.minecraft.net/texture/")) {
            url = "http://textures.minecraft.net/texture/" + url;
        }
        try {
            return new URI(url).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return null;
        }
    }

    @Nullable
    private URL decodeSkinUrlFromBase64(String base64) {
        try {
            String decodedJson = new String(Base64.getDecoder().decode(base64));
            JsonObject textureDataJson = JsonParser.parseString(decodedJson).getAsJsonObject();
            MojangTextureData.SkinInfo skinInfo = new JsonMojangTextureData(textureDataJson).getSkin();
            return skinInfo != null ? skinInfo.getUrl() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public UUID fromMojangUuid(String mojangUuid) {
        // Convert Mojang's UUID format to standard UUID format
        return UUID.fromString(mojangUuid.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"));
    }

    public boolean isBase64(@NotNull String s) {
        try {
            Base64.getDecoder().decode(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String toMojangUuid(UUID uuid) {
        return uuid.toString().replace("-", "");
    }
}
