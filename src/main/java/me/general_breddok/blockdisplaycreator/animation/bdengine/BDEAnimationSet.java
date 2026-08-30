package me.general_breddok.blockdisplaycreator.animation.bdengine;

import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface BDEAnimationSet {
    Map<String, BDEAnimation> getTransformationAnimations();

    Map<String, BDEAnimation> getSoundAnimations();

    Map<String, BDEAnimator> getTransformationAnimators();

    Map<String, BDEAnimator> getSoundAnimators();

    MCFunction getCreateFunction();

    MCFunction getDeleteFunction();

    MCFunction getStopTransformationAnimationFunction();

    MCFunction getStopSoundAnimationFunction();

    String getTag();
}
