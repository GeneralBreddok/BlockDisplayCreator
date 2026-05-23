package me.general_breddok.blockdisplaycreator.web.bdengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.SummonDisplayCommandLine;
import me.general_breddok.blockdisplaycreator.nbt.entity.NbtEntity;
import me.general_breddok.blockdisplaycreator.nbt.entity.NbtEntityObject;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.web.exception.BDEngineModelNotFoundException;
import me.general_breddok.blockdisplaycreator.web.exception.InvalidResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NetworkBDEngineModel implements BDEngineModel {
    private static final String BDENGINE_API = "https://block-display.com/server-api/?id=";
    String name;
    String author;
    String category;
    String imgUrl;
    int modelCount;
    JsonObject commands;
    JsonArray passengers;

    public NetworkBDEngineModel(String modelId) throws InvalidResponseException {

        if (modelId.isEmpty()) {
            throw new IllegalArgumentException("modelId must not be empty!");
        }

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BDENGINE_API + modelId))
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

    private static void processBadRequest(HttpResponse<String> response) throws BDEngineModelNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String error = jsonObject.get("error").getAsString();

        throw new BDEngineModelNotFoundException(error);
    }

    private void processSuccessResponse(HttpResponse<String> response) {
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonObject content = jsonObject.getAsJsonObject("content");

        this.name = content.get("name").getAsString();
        this.author = content.get("author").getAsString();
        this.category = content.get("category").getAsString();
        this.imgUrl = content.get("img_url").getAsString();
        this.modelCount = content.get("model_count").getAsInt();
        JsonElement jsonCommands = content.get("commands");
        if (jsonCommands.isJsonArray()) {
            this.commands = this.jsonArrayToJsonObject(jsonCommands.getAsJsonArray());
        } else {
            this.commands = jsonCommands.getAsJsonObject();
        }
        this.passengers = content.get("Passengers").getAsJsonArray();
    }

    @Override
    public List<SummonDisplayCommandLine> decodeCommands() {
        return this.commands
                .asMap()
                .values()
                .stream()
                .map(jsonCommand ->
                        new SummonDisplayCommandLine(jsonCommand.getAsString())
                )
                .collect(OperationUtil.toArrayList());
    }

    private JsonObject jsonArrayToJsonObject(JsonArray jsonArray) {
        JsonObject jsonObject = new JsonObject();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject.add(String.valueOf(i + 1), jsonArray.get(i));
        }
        return jsonObject;
    }

    @Override
    public List<NbtEntity> decodePassengers() {
        return this.passengers
                .asList()
                .stream()
                .map(jsonPassenger -> {
                            try {
                                return (NbtEntity) new NbtEntityObject(jsonPassenger.getAsString());
                            } catch (IOException e) {
                                return null;
                            }
                        }
                )
                .collect(OperationUtil.toArrayList());
    }
}
