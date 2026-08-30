package me.general_breddok.blockdisplaycreator.animation.bdengine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataPackBDEAnimationKeyframe implements BDEAnimationKeyframe {
    List<CommandLine> commands;

    public DataPackBDEAnimationKeyframe(MCFunction keyframeFunction) {
        this.commands = keyframeFunction.getCommands();
    }
}
