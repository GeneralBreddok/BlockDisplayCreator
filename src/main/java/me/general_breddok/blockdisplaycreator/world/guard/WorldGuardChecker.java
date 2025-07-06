package me.general_breddok.blockdisplaycreator.world.guard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import org.bukkit.Location;
import org.bukkit.entity.Player;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorldGuardChecker {
    WorldGuardPlugin worldGuardPlugin;
    WorldGuard worldGuard;
    WorldGuardPlatform platform;
    RegionContainer regionContainer;

    public WorldGuardChecker() {
        worldGuardPlugin = WorldGuardPlugin.inst();
        worldGuard = WorldGuard.getInstance();
        platform = worldGuard.getPlatform();
        regionContainer = platform.getRegionContainer();
    }


    public boolean canAccess(Player player, ProtectedRegion region) {
        LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(player);

        return region.isMember(localPlayer);
    }

    public ProtectedRegion getRegion(Location location) {

        if (location.getWorld() == null) {
            return null;
        }

        BlockVector3 blockVector = BukkitAdapter.asBlockVector(location);

        RegionManager regionManager = this.regionContainer.get(BukkitAdapter.adapt(location.getWorld()));

        if (regionManager == null) {
            return null;
        }

        for (ProtectedRegion region : regionManager.getRegions().values()) {
            if (region.contains(blockVector)) {
                return region;
            }
        }
        return null;
    }

    public boolean isFlagAllowed(StateFlag flag, ProtectedRegion region) {
        StateFlag.State flagValue = region.getFlag(flag);

        if (flagValue == null) {
            return false;
        }

        return flagValue == StateFlag.State.ALLOW;
    }

    public static boolean checkWGAccess(Location location, StateFlag flag, Player player) {
        if (flag != null) {
            WorldGuardChecker worldGuardChecker = new WorldGuardChecker();

            ProtectedRegion region = worldGuardChecker.getRegion(location);

            if (region == null)
                return true;

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            if (region.isMember(localPlayer))
                return true;

            if (player.hasPermission(DefaultPermissions.BDC.WG_BYPASS))
                return true;

            return worldGuardChecker.isFlagAllowed(flag, region);
        }
        return true;
    }
}
