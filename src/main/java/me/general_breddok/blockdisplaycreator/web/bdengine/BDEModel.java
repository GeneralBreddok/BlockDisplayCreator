package me.general_breddok.blockdisplaycreator.web.bdengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.animation.bdengine.BDEAnimation;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.SummonCommandLine;
import me.general_breddok.blockdisplaycreator.version.MinecraftVersion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BDEModel {

    MinecraftVersion getVersion();

    Type getType();

    @Nullable
    String getProjectId();

    JsonArray getPassengers();

    List<String> decodePassengers();

    List<String> getSummonCommands();

    List<CommandLine> decodeSummonCommands();

    @Nullable
    JsonObject getTransformationAnimations();

    List<BDEAnimation> decodeTransformationAnimations();

    @Nullable
    JsonObject getSoundAnimations();

    List<BDEAnimation> decodeSoundAnimations();

    @Nullable
    JsonElement getHitbox();

    List<SummonCommandLine> decodeHitbox();

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    enum Type {
        FULL("full"),
        MODEL_ONLY("modelOnly");

        String line;

        @Nullable
        public static Type fromString(String typeString) {
            if (typeString == null)
                return null;

            for (Type modelType : values()) {
                if (typeString.equalsIgnoreCase(modelType.getLine())) {
                    return modelType;
                }
            }
            return null;
        }
    }
}
