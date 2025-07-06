package me.general_breddok.blockdisplaycreator.animation;


import org.bukkit.Bukkit;

public class AnimationSession {
    private int taskId = -1;

    public AnimationSession(int taskId) {
        this.taskId = taskId;
    }

    public boolean isCurrentlyRunning() {
        return Bukkit.getScheduler().isCurrentlyRunning(this.taskId);
    }

    public boolean isQueued() {
        return Bukkit.getScheduler().isQueued(this.taskId);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
