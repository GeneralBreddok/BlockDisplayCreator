package me.general_breddok.blockdisplaycreator.world.skyblock.superior;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@UtilityClass
public class SSBIslandAccessChecker {
    public boolean checkIslandAccess(Location location, IslandPrivilege privilege, Player player) {
        if (privilege == null)
            return true;

        Island island = SuperiorSkyblockAPI.getIslandAt(location);

        if (island == null)
            return true;

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        if (player.hasPermission(DefaultPermissions.BDC.SKYBLOCK_BYPASS) || player.hasPermission(DefaultPermissions.SuperiorSkyBlock.BYPASS))
            return true;

        return island.hasPermission(superiorPlayer, privilege);
    }
}
