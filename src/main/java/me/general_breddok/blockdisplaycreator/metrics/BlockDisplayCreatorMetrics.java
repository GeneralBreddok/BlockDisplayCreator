package me.general_breddok.blockdisplaycreator.metrics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

/**
 * Implementation of {@link PluginMetrics} for BlockDisplayCreator.
 * <p>
 * Wraps the bStats {@link Metrics} system and registers custom charts
 * to monitor plugin-specific statistics, such as the number of custom blocks.
 * </p>
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockDisplayCreatorMetrics implements PluginMetrics {
    /**
     * The main plugin instance.
     */
    BlockDisplayCreator plugin;
    /**
     * The unique plugin identifier used for metrics reporting.
     */
    int pluginId;
    /**
     * The bStats metrics handler.
     */
    Metrics metrics;

    /**
     * Creates a new metrics handler for BlockDisplayCreator.
     *
     * @param plugin   the plugin instance
     * @param pluginId the unique plugin ID
     */
    public BlockDisplayCreatorMetrics(BlockDisplayCreator plugin, int pluginId) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.metrics = new Metrics(plugin, pluginId);

        registerCustomBlocksChart();
    }

    /**
     * Registers a custom chart tracking the number of registered custom blocks.
     */
    private void registerCustomBlocksChart() {
        this.metrics.addCustomChart(new SingleLineChart(
                "customBlocks",
                () -> this.plugin.getCustomBlockService().getStorage().getAbstractCustomBlocks().size()
        ));
    }
}

