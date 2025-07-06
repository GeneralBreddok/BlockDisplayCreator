package me.general_breddok.blockdisplaycreator.animation.bdengine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunctionFile;

import java.nio.file.Path;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataPackFunctionAnimator implements FunctionAnimator {
    MCFunction playFunction;
    MCFunction playLoopFunction;

    public DataPackFunctionAnimator(Path folderPath) {
        this.playFunction = new MCFunctionFile(folderPath.resolve("play.mcfunction"), false);
        this.playLoopFunction = new MCFunctionFile(folderPath.resolve("play_loop.mcfunction"), false);
    }
}
