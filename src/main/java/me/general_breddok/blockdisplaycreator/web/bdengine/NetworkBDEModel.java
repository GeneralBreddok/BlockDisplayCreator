package me.general_breddok.blockdisplaycreator.web.bdengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.animation.bdengine.BDEAnimation;
import me.general_breddok.blockdisplaycreator.animation.bdengine.JsonBDEAnimation;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.SummonCommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.SummonDisplayCommandLine;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.version.MinecraftVersion;
import me.general_breddok.blockdisplaycreator.web.exception.BDEModelNotFoundException;
import me.general_breddok.blockdisplaycreator.web.exception.InvalidResponseException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NetworkBDEModel implements BDEModel {
    private static final String BDENGINE_API = "https://block-display.com/server-api/?id=";

    MinecraftVersion version;
    BDEModel.Type type;
    @Nullable
    String projectId;
    JsonArray passengers;
    @Nullable
    JsonObject transformationAnimations;
    @Nullable
    JsonObject soundAnimations;
    @Nullable
    JsonElement hitbox;

    public NetworkBDEModel(int modelId) throws InvalidResponseException {
        this(String.valueOf(modelId));
    }

    public NetworkBDEModel(String modelId) throws InvalidResponseException {
        this(modelId, null);
    }

    public NetworkBDEModel(String modelId, @Nullable String tag) throws InvalidResponseException {

        if (modelId.isEmpty()) {
            throw new IllegalArgumentException("modelId must not be empty!");
        }

        HttpClient client = HttpClient.newHttpClient();

        try {
            String urlString = BDENGINE_API + modelId;

            if (tag != null && !tag.isEmpty()) {
                urlString += "&tag=" + tag;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                processSuccessResponse(response);
            } else if (response.statusCode() == 400) {
                processBadRequest(response);
            }

        } catch (IOException | InterruptedException e) {
            throw new InvalidResponseException(e);
        }
    }

    private void processBadRequest(HttpResponse<String> response) throws BDEModelNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String error = jsonObject.get("error").getAsString();

        throw new BDEModelNotFoundException(error);
    }

    private void processSuccessResponse(HttpResponse<String> response) throws BDEModelNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonObject content = jsonObject.getAsJsonObject("content");

        if (content == null || content.isJsonNull()) {
            processBadRequest(response);
            return;
        }

        this.version = MinecraftVersion.fromString(content.get("version").getAsString());
        this.type = BDEModel.Type.fromString(content.get("type").getAsString());
        JsonElement projectIdElement = content.get("project_id");

        if (projectIdElement != null && !projectIdElement.isJsonNull()) {
            this.projectId = projectIdElement.getAsString();
        }

        this.passengers = content.get("passengers").getAsJsonArray();
        this.hitbox = content.get("hitbox");

        JsonObject datapack = content.getAsJsonObject("datapack");

        if (datapack != null && !datapack.isJsonNull()) {
            this.transformationAnimations = datapack.getAsJsonObject("transformation_animations");
            this.soundAnimations = datapack.getAsJsonObject("sound_animations");
        }
    }

    @Override
    public List<String> decodePassengers() {
        return this.passengers
                .asList()
                .stream()
                .map(JsonElement::getAsString)
                .collect(OperationUtil.toArrayList());
    }

    @Override
    public List<String> getSummonCommands() {
        return this.passengers
                .asList()
                .stream()
                .map(passengers ->
                        "/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[" + passengers.getAsString() + "]}"
                )
                .collect(OperationUtil.toArrayList());

    }

    @Override
    public List<CommandLine> decodeSummonCommands() {
        return this.passengers
                .asList()
                .stream()
                .map(passengers ->
                        new SummonDisplayCommandLine("/summon block_display ~-0.5 ~-0.5 ~-0.5 {Passengers:[" + passengers.getAsString() + "]}")
                )
                .collect(OperationUtil.toArrayList());
    }

    @Override
    public List<BDEAnimation> decodeTransformationAnimations() {
        if (this.transformationAnimations == null || this.transformationAnimations.isJsonNull()) {
            return List.of();
        }

        return this.transformationAnimations
                    .asMap()
                    .values()
                    .stream()
                    .map(jsonAnimation -> new JsonBDEAnimation(jsonAnimation.getAsJsonObject()))
                    .collect(OperationUtil.toArrayList());
    }

    @Override
    public List<BDEAnimation> decodeSoundAnimations() {
        if (this.soundAnimations == null || this.soundAnimations.isJsonNull()) {
            return List.of();
        }

        return this.soundAnimations
                .asMap()
                .values()
                .stream()
                .map(jsonAnimation -> new JsonBDEAnimation(jsonAnimation.getAsJsonObject()))
                .collect(OperationUtil.toArrayList());
    }

    @Override
    public List<SummonCommandLine> decodeHitbox() {
        if (this.hitbox == null || this.hitbox.isJsonNull()) {
            return List.of();
        }

        return this.hitbox
                .getAsJsonArray()
                .asList()
                .stream()
                .map(jsonCommand ->
                        new SummonCommandLine(jsonCommand.getAsString())
                )
                .collect(OperationUtil.toArrayList());
    }
}

