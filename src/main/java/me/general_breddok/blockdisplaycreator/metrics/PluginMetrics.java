package me.general_breddok.blockdisplaycreator.metrics;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

/**
 * Abstraction for plugin metrics integration.
 * <p>
 * Provides access to the plugin instance, its metrics identifier,
 * and the {@link Metrics} object used for statistics collection.
 * </p>
 */
public interface PluginMetrics {

    /**
     * Gets the plugin associated with the metrics.
     *
     * @return the {@link Plugin} instance
     */
    Plugin getPlugin();

    /**
     * Gets the plugin identifier for metrics reporting.
     *
     * @return the unique plugin ID
     */
    int getPluginId();

    /**
     * Gets the metrics handler instance.
     *
     * @return the {@link Metrics} object
     */
    Metrics getMetrics();
}
