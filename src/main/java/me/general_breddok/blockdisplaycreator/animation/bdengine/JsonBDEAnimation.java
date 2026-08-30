package me.general_breddok.blockdisplaycreator.animation.bdengine;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonBDEAnimation implements BDEAnimation {
    List<BDEAnimationKeyframe> keyframes;

    public JsonBDEAnimation(@NotNull JsonObject jsonAnimation) {
        this.keyframes = jsonAnimation
                .entrySet()
                .stream()
                .map(entry -> (BDEAnimationKeyframe) new JsonBDEAnimationKeyframe(entry.getValue().getAsJsonObject()))
                .toList();
    }
}
