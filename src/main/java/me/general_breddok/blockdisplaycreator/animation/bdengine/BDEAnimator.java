package me.general_breddok.blockdisplaycreator.animation.bdengine;

import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;

public interface BDEAnimator {
    MCFunction getPlayFunction();

    MCFunction getPlayLoopFunction();
}
