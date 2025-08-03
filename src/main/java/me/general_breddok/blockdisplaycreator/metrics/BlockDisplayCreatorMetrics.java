package me.general_breddok.blockdisplaycreator.metrics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockDisplayCreatorMetrics implements PluginMetrics {
    BlockDisplayCreator plugin;
    int pluginId;
    Metrics metrics;

    public BlockDisplayCreatorMetrics(BlockDisplayCreator plugin, int pluginId) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.metrics = new Metrics(plugin, pluginId);

        registerCustomBlocksChart();
    }

    private void registerCustomBlocksChart() {
        this.metrics.addCustomChart(new SingleLineChart("customBlocks", () -> this.plugin.getCustomBlockService().getStorage().getAbstractCustomBlocks().size()));
    }
}
