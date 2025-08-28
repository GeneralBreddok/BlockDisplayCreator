package me.general_breddok.blockdisplaycreator.common;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.listener.packet.PacketReceiveListener;
import me.general_breddok.blockdisplaycreator.world.guard.CBRegionFlags;
import me.general_breddok.blockdisplaycreator.world.skyblock.CBIslandPrivileges;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BDCDependentPluginsManager {
    Plugin mainPlugin;
    Plugin worldGuard;
    Plugin placeholderApi;
    Plugin packetEvents;
    Plugin superiorSkyblock;

    public BDCDependentPluginsManager(Plugin mainPlugin) {
        this.mainPlugin = mainPlugin;
        PluginManager pluginManager = mainPlugin.getServer().getPluginManager();
        this.worldGuard = pluginManager.getPlugin("WorldGuard");
        this.placeholderApi = pluginManager.getPlugin("PlaceholderAPI");
        this.packetEvents = pluginManager.getPlugin("PacketEvents");
        this.superiorSkyblock = pluginManager.getPlugin("SuperiorSkyblock2");
    }

    public boolean isWorldGuardAvailable() {
        return worldGuard != null;
    }

    public boolean isPlaceholderApiAvailable() {
        return placeholderApi != null;
    }

    public boolean isPacketEventsAvailable() {
        return packetEvents != null;
    }

    public boolean isSuperiorSkyblockAvailable() {
        return superiorSkyblock != null;
    }

    public void loadPacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this.mainPlugin));
            PacketEvents.getAPI().load();

            PacketEvents.getAPI().getEventManager().registerListener(PacketReceiveListener.get(), PacketListenerPriority.NORMAL);
        }
    }

    public void initPacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.getAPI().init();
        }
    }

    public void terminatePacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.getAPI().terminate();
        }
    }

    public void registerWorldGuardFlags() {
        if (this.isWorldGuardAvailable()) {
            CBRegionFlags regionFlags = new CBRegionFlags();
            regionFlags.registerFlags();
        }
    }

    public void registerSkyblockFlags() {
        if (this.isSuperiorSkyblockAvailable()) {
            CBIslandPrivileges islandFlags = new CBIslandPrivileges();
            islandFlags.registerFlags();
        }
    }
}
