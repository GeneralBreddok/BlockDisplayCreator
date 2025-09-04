package me.general_breddok.blockdisplaycreator.rotation.snapper;

import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.Location;

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
            yaws[i] = (i + 1) * this.stepWidth;
        }
        return yaws;
    }

    @Override
    public float getClosestAngle(float yaw) {
        float closestYaw = this.possibleAngles[0];
        float minDifference = Math.abs(yaw - closestYaw);

        for (float possibleYaw : this.possibleAngles) {
            float difference = Math.abs(yaw - possibleYaw);
            ChatUtil.log("Comparing yaw: " + yaw + " with possibleYaw: " + possibleYaw + " | difference: " + difference + " | minDifference: " + minDifference);
            if (difference < minDifference) {
                minDifference = difference;
                closestYaw = possibleYaw;
            }
        }

        return closestYaw;
    }
}
