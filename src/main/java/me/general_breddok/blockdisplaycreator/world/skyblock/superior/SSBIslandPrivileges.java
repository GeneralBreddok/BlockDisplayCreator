package me.general_breddok.blockdisplaycreator.world.skyblock.superior;

import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import org.jetbrains.annotations.Nullable;

public class SSBIslandPrivileges {
    @Nullable
    public static IslandPrivilege PLACE_CB;
    @Nullable
    public static IslandPrivilege BREAK_CB;
    @Nullable
    public static IslandPrivilege INTERACT_CB;

    public void registerFlags() {
        IslandPrivilege.register("PLACE_CB");
        IslandPrivilege.register("BREAK_CB");
        IslandPrivilege.register("INTERACT_CB");

        PLACE_CB = IslandPrivilege.getByName("PLACE_CB");
        BREAK_CB = IslandPrivilege.getByName("BREAK_CB");
        INTERACT_CB = IslandPrivilege.getByName("INTERACT_CB");
    }
}
