package me.general_breddok.blockdisplaycreator.animation.bdengine;

import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;

public interface FunctionAnimator {
    MCFunction getPlayFunction();

    MCFunction getPlayLoopFunction();
}
