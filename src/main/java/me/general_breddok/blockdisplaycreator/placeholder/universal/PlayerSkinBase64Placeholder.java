package me.general_breddok.blockdisplaycreator.placeholder.universal;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.Base64;

public record PlayerSkinBase64Placeholder(Player context) implements UniversalPlaceholder<Player> {

    public static String encodeSkinUrl(String skinUrl) {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        JsonObject skin = new JsonObject();

        skin.add("url", new JsonPrimitive(skinUrl));
        textures.add("SKIN", skin);
        root.add("textures", textures);

        String json = root.toString();
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    @Override
    public String apply(String template) {
        if (this.context == null) {
            return template;
        }

        if (template.isEmpty()) {
            return template;
        }

        URL skinUrl = this.context.getPlayerProfile()
                .getTextures()
                .getSkin();

        if (skinUrl == null || skinUrl.toString().isEmpty()) {
            return template;
        }

        String encoded = encodeSkinUrl(skinUrl.toString());
        return template.replace("%player_skin_base64%", encoded);
    }
}

