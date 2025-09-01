package me.general_breddok.blockdisplaycreator.listener.skyblock.superior;

import com.bgsoftware.superiorskyblock.api.events.PluginInitializeEvent;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockPluginInitializeListener implements Listener {
    @EventHandler
    public void onPluginInitialize(PluginInitializeEvent event) {
        BlockDisplayCreator.getInstance().getDependentPluginsManager().registerSkyblockFlags();
    }
}
