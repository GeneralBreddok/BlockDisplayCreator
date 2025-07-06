package me.general_breddok.blockdisplaycreator.web.player;

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

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NetworkPlayerProfile implements MojangProfile {
    String name;
    String id;

    private static final String MOJANG_API = "https://api.mojang.com/users/profiles/minecraft/";

    public NetworkPlayerProfile(String playerName) throws InvalidResponseException {
        HttpClient client = HttpClient.newHttpClient();

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MOJANG_API + playerName))
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

    private void processSuccessResponse(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        this.name = jsonObject.get("name").getAsString();
        this.id = jsonObject.get("id").getAsString();
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
}
