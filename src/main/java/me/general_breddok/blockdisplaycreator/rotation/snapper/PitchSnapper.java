package me.general_breddok.blockdisplaycreator.rotation.snapper;

public class PitchSnapper extends AngleSnapper {

    public PitchSnapper(int steps) {
        super(steps);
    }

    @Override
    protected float calculateStepWidth() {
        return 180f / this.steps;
    }

    @Override
    protected float[] calculatePossibleAngles() {
        float[] pitches = new float[this.steps];
        float currentPitch = -90;
        for (int i = 0; i < this.steps; i++) {
            pitches[i] = currentPitch;
            currentPitch += this.stepWidth;
        }
        return pitches;
    }

    @Override
    public float getClosestAngle(float pitch) {

        float closestAngle = this.possibleAngles[0];
        float minDifference = Math.abs(pitch - closestAngle);

        for (float possibleAngle : this.possibleAngles) {
            float difference = Math.abs(pitch - possibleAngle);
            if (difference < minDifference) {
                minDifference = difference;
                closestAngle = possibleAngle;
            }
        }

        return closestAngle;
    }
}
