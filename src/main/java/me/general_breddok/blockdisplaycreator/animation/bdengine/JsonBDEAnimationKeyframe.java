package me.general_breddok.blockdisplaycreator.animation.bdengine;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.MCCommandLine;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonBDEAnimationKeyframe implements BDEAnimationKeyframe {
    List<CommandLine> commands;

    public JsonBDEAnimationKeyframe(@NotNull JsonObject jsonKeyframe) {
        if (!jsonKeyframe.isJsonArray()) {
            throw new IllegalArgumentException("Keyframe must be a JSON array of commands");
        }

        this.commands = jsonKeyframe
                .getAsJsonArray()
                .asList()
                .stream()
                .map(jsonElement -> new MCCommandLine(jsonElement.getAsString()))
                .collect(OperationUtil.toArrayList());
    }
}
