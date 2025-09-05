package me.general_breddok.blockdisplaycreator.web.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.web.exception.InvalidResponseException;
import me.general_breddok.blockdisplaycreator.web.exception.ProfileNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NetworkPlayerSession implements MojangSession {
    private static final String SESSION_SERVER = "https://sessionserver.mojang.com/session/minecraft/profile/";
    String name;
    String id;
    List<MojangSessionProperty> properties;
    List<String> profileActions;

    public NetworkPlayerSession(String mojangUuid) throws InvalidResponseException {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SESSION_SERVER + mojangUuid))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                processSuccessResponse(response);
            } else if (response.statusCode() == 400) {
                processBadRequest(response);
            } else if (response.statusCode() == 404) {
                processNotFound(response);
            } else {
                throw new InvalidResponseException();
            }
        } catch (IOException | InterruptedException e) {
            throw new InvalidResponseException(e);
        }
    }

    private static void processNotFound(HttpResponse<String> response) throws ProfileNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String path = jsonObject.get("path").getAsString();
        String error = jsonObject.get("error").getAsString();
        String errorMessage = jsonObject.get("errorMessage").getAsString();

        throw new ProfileNotFoundException(path, error, errorMessage);
    }

    private static void processBadRequest(HttpResponse<String> response) throws ProfileNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String path = jsonObject.get("path").getAsString();
        String errorMessage = jsonObject.get("errorMessage").getAsString();

        throw new ProfileNotFoundException(path, errorMessage);
    }

    private void processSuccessResponse(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        this.name = jsonObject.get("name").getAsString();
        this.id = jsonObject.get("id").getAsString();

        this.properties = new ArrayList<>();
        JsonArray propertiesJsonArray = jsonObject.getAsJsonArray("properties");
        if (propertiesJsonArray != null && !propertiesJsonArray.isEmpty()) {
            propertiesJsonArray.forEach(property ->
                    this.properties.add(new JsonMojangSessionProperty(property.getAsJsonObject())));
        }

        this.profileActions = new ArrayList<>();
        JsonArray profileActionsJsonArray = jsonObject.getAsJsonArray("profileActions");
        if (profileActionsJsonArray != null && !profileActionsJsonArray.isEmpty()) {
            profileActionsJsonArray.forEach(action -> this.profileActions.add(action.getAsString()));
        }
    }
}
