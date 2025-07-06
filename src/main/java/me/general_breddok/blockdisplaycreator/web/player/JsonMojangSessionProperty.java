package me.general_breddok.blockdisplaycreator.web.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonMojangSessionProperty implements MojangSessionProperty {
    String name;
    String value;
    String signature;

    public JsonMojangSessionProperty(JsonObject propertyObject) {
        this.name = propertyObject.get("name").getAsString();
        this.value = propertyObject.get("value").getAsString();

        this.signature = propertyObject.has("signature") ? propertyObject.get("signature").getAsString() : null;
    }

    @Override
    public MojangTextureData decodeValue() {
        if (!this.name.equals("textures")) {
            return null;
        }

        String decodedJson = new String(Base64.getDecoder().decode(value));
        JsonObject textureDataJson = JsonParser.parseString(decodedJson).getAsJsonObject();
        try {
            return new JsonMojangTextureData(textureDataJson);
        } catch (URISyntaxException | MalformedURLException e) {
            return null;
        }
    }
}
