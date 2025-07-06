package me.general_breddok.blockdisplaycreator.rotation.snapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AngleSnapper {

    int steps;
    float[] possibleAngles;
    float stepWidth;

    protected AngleSnapper(int steps) {
        if (steps < 1) {
            throw new IllegalArgumentException("The count of steps must be greater than zero.");
        }
        this.steps = steps;
        this.stepWidth = calculateStepWidth();
        this.possibleAngles = calculatePossibleAngles();
    }

    protected abstract float calculateStepWidth();

    protected abstract float[] calculatePossibleAngles();

    public abstract float getClosestAngle(float angle);
}
