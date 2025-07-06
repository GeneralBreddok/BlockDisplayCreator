package me.general_breddok.blockdisplaycreator.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisplayAnimation {

    @NotNull
    final Plugin plugin;
    @Setter
    @NotNull
    Display display;
    @Setter
    @NotNull
    Transformation transformation;

    AnimationSession animationSession = null;

    public DisplayAnimation(@NotNull Display display, @NotNull Transformation transformation, @NotNull Plugin plugin) {
        this.display = display;
        this.transformation = transformation;
        this.plugin = plugin;
    }

    public void animate(int interpolationDuration, int delay, Runnable afterEnd) {
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            this.display.setInterpolationDelay(-1);
            this.display.setInterpolationDuration(interpolationDuration);
            this.display.setTransformation(this.transformation);
        }, delay);

        this.animationSession = new AnimationSession(taskId);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            this.animationSession = null;
            if (afterEnd != null) {
                afterEnd.run();
            }
        }, interpolationDuration);
    }

    public void animate(int interpolationDuration, int delay) {
        animate(interpolationDuration, delay, null);
    }

    public void animate(int interpolationDuration) {
        animate(interpolationDuration, 0);
    }

    public boolean isAnimationRunning() {
        return this.animationSession != null;
    }
}
