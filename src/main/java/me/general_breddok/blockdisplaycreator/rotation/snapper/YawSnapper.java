package me.general_breddok.blockdisplaycreator.rotation.snapper;

public class YawSnapper extends AngleSnapper {

    public YawSnapper(int steps) {
        super(steps);
    }

    @Override
    protected float calculateStepWidth() {
        return 360f / getSteps();
    }

    @Override
    protected float[] calculatePossibleAngles() {
        float[] yaws = new float[this.steps];
        for (int i = 0; i < this.steps; i++) {
            yaws[i] = i * this.stepWidth;
        }
        return yaws;
    }

    @Override
    public float getClosestAngle(float yaw) {
        float closestYaw = this.possibleAngles[0];
        float minDifference = this.circularDifference(yaw, closestYaw);

        for (float possibleYaw : this.possibleAngles) {
            float difference = this.circularDifference(yaw, possibleYaw);
            if (difference < minDifference) {
                minDifference = difference;
                closestYaw = possibleYaw;
            }
        }

        return closestYaw;
    }

    private float circularDifference(float a, float b) {
        float diff = Math.abs(a - b) % 360f;
        return Math.min(diff, 360f - diff);
    }
}
