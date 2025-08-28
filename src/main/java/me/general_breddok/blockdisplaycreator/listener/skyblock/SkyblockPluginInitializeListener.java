package me.general_breddok.blockdisplaycreator.listener.skyblock;

import com.bgsoftware.superiorskyblock.api.events.PluginInitializeEvent;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkyblockPluginInitializeListener implements Listener {
    @EventHandler
    public void onPluginInitialize(PluginInitializeEvent event) {
        BlockDisplayCreator.getInstance().getDependentPluginsManager().registerSkyblockFlags();
    }
}
