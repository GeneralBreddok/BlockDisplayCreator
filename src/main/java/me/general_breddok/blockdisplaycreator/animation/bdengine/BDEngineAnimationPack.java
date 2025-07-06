package me.general_breddok.blockdisplaycreator.animation.bdengine;

import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;

import java.util.Map;

public interface BDEngineAnimationPack {
    Map<String, FunctionAnimation> getAnimations();
    Map<String, FunctionAnimator> getAnimators();
    MCFunction getCreateFunction();
    MCFunction getDeleteFunction();
    MCFunction getStopFunction();
    String getTag();
}
