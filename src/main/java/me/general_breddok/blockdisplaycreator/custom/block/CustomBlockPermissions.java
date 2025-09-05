package me.general_breddok.blockdisplaycreator.custom.block;

import org.bukkit.entity.Player;

import java.util.Set;

public interface CustomBlockPermissions {

    Set<String> getPermissions(Type type);

    void clearPermissions(Type type);

    boolean hasPermissions(Player player, Type type);

    boolean hasAllPermissions(Player player);

    boolean addPermission(String permission, Type type);

    boolean removePermission(String permission, Type type);


    enum Type {
        PLACE,
        BREAK,
        INTERACT
    }
}
