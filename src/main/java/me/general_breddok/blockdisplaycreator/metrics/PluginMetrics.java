package me.general_breddok.blockdisplaycreator.metrics;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public interface PluginMetrics {
    JavaPlugin getPlugin();
    int getPluginId();
    Metrics getMetrics();
}