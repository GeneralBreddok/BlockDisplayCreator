package me.general_breddok.blockdisplaycreator.world.skyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SSIslandAccessChecker {
    public static boolean checkIslandAccess(Location location, IslandPrivilege privilege, Player player) {
        if (privilege == null)
            return true;

        Island island = SuperiorSkyblockAPI.getIslandAt(location);

        if (island == null)
            return true;

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        if (player.hasPermission(DefaultPermissions.BDC.SKYBLOCK_BYPASS) || player.hasPermission(DefaultPermissions.Superior.SKYBLOCK_BYPASS))
            return true;

        return island.hasPermission(superiorPlayer, privilege);
    }
}
