package me.general_breddok.blockdisplaycreator.world.guard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import org.jetbrains.annotations.Nullable;

public class BDCRegionFlags {
    @Nullable
    public static StateFlag PLACE_CB;
    @Nullable
    public static StateFlag BREAK_CB;
    @Nullable
    public static StateFlag INTERACT_CB;

    public void registerFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        StateFlag placeCbFlag = new StateFlag("place-cb", false);
        StateFlag breakCbFlag = new StateFlag("break-cb", false);
        StateFlag interactCbFlag = new StateFlag("interact-cb", true);

        registry.register(placeCbFlag);
        registry.register(breakCbFlag);
        registry.register(interactCbFlag);

        PLACE_CB = placeCbFlag;
        BREAK_CB = breakCbFlag;
        INTERACT_CB = interactCbFlag;
    }
}
