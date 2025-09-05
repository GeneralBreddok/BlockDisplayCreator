package me.general_breddok.blockdisplaycreator.common;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.listener.packet.PacketReceiveListener;
import me.general_breddok.blockdisplaycreator.world.guard.WGRegionFlags;
import me.general_breddok.blockdisplaycreator.world.skyblock.superior.SSBIslandPrivileges;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Manages optional plugin dependencies used by BlockDisplayCreator.
 * <p>
 * Supported integrations:
 * <ul>
 *   <li>WorldGuard</li>
 *   <li>PlaceholderAPI</li>
 *   <li>PacketEvents</li>
 *   <li>SuperiorSkyblock2</li>
 * </ul>
 * Provides availability checks, initialization, and flag registration
 * for each supported plugin.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BDCDependentPluginsManager {
    /**
     * Reference to the main plugin instance.
     */
    Plugin mainPlugin;

    /**
     * WorldGuard plugin instance, or {@code null} if not present.
     */
    Plugin worldGuard;
    /**
     * PlaceholderAPI plugin instance, or {@code null} if not present.
     */
    Plugin placeholderApi;
    /**
     * PacketEvents plugin instance, or {@code null} if not present.
     */
    Plugin packetEvents;
    /**
     * SuperiorSkyblock2 plugin instance, or {@code null} if not present.
     */
    Plugin superiorSkyblock;

    /**
     * Creates a new dependency manager.
     *
     * @param mainPlugin the owning plugin, used to access the server
     *                   and resolve available dependencies
     */
    public BDCDependentPluginsManager(Plugin mainPlugin) {
        this.mainPlugin = mainPlugin;
        PluginManager pluginManager = mainPlugin.getServer().getPluginManager();
        this.worldGuard = pluginManager.getPlugin("WorldGuard");
        this.placeholderApi = pluginManager.getPlugin("PlaceholderAPI");
        this.packetEvents = pluginManager.getPlugin("PacketEvents");
        this.superiorSkyblock = pluginManager.getPlugin("SuperiorSkyblock2");
    }

    /**
     * Checks if WorldGuard is available on the server.
     *
     * @return {@code true} if WorldGuard is loaded
     */
    public boolean isWorldGuardAvailable() {
        return worldGuard != null;
    }

    /**
     * Checks if PlaceholderAPI is available on the server.
     *
     * @return {@code true} if PlaceholderAPI is loaded
     */
    public boolean isPlaceholderApiAvailable() {
        return placeholderApi != null;
    }

    /**
     * Checks if PacketEvents is available on the server.
     *
     * @return {@code true} if PacketEvents is loaded
     */
    public boolean isPacketEventsAvailable() {
        return packetEvents != null;
    }

    /**
     * Checks if SuperiorSkyblock2 is available on the server.
     *
     * @return {@code true} if SuperiorSkyblock2 is loaded
     */

    public boolean isSuperiorSkyblockAvailable() {
        return superiorSkyblock != null;
    }

    /**
     * Loads PacketEvents (if available) and registers packet listeners.
     */
    public void loadPacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this.mainPlugin));
            PacketEvents.getAPI().load();

            PacketEvents.getAPI().getEventManager().registerListener(PacketReceiveListener.get(), PacketListenerPriority.NORMAL);
        }
    }

    /**
     * Initializes PacketEvents (if available).
     */
    public void initPacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.getAPI().init();
        }
    }

    /**
     * Terminates PacketEvents (if available).
     */
    public void terminatePacketEvents() {
        if (this.isPacketEventsAvailable()) {
            PacketEvents.getAPI().terminate();
        }
    }

    /**
     * Registers custom flags for WorldGuard regions (if available).
     */
    public void registerWorldGuardFlags() {
        if (this.isWorldGuardAvailable()) {
            WGRegionFlags regionFlags = new WGRegionFlags();
            regionFlags.registerFlags();
        }
    }

    /**
     * Registers custom flags for SuperiorSkyblock2 islands (if available).
     */
    public void registerSkyblockFlags() {
        if (this.isSuperiorSkyblockAvailable()) {
            SSBIslandPrivileges islandFlags = new SSBIslandPrivileges();
            islandFlags.registerFlags();
        }
    }
}
