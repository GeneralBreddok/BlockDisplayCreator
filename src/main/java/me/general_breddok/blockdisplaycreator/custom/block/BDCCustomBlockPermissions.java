package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Stream;


@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BDCCustomBlockPermissions implements CustomBlockPermissions {
    Set<String> placePermissions;
    Set<String> breakPermissions;
    Set<String> interactPermissions;


    @Override
    public Set<String> getPermissions(Type type) {
        return switch (type) {
            case PLACE -> this.placePermissions;
            case BREAK -> this.breakPermissions;
            case INTERACT -> this.interactPermissions;
        };
    }

    @Override
    public void clearPermissions(Type type) {
        switch (type) {
            case PLACE -> this.placePermissions.clear();
            case BREAK -> this.breakPermissions.clear();
            case INTERACT -> this.interactPermissions.clear();
        }
    }

    @Override
    public boolean hasPermissions(Player player, Type type) {
        return this.getPermissions(type).stream()
                .allMatch(player::hasPermission);
    }

    @Override
    public boolean hasAllPermissions(Player player) {
        return Stream.of(this.placePermissions, this.breakPermissions, this.interactPermissions)
                .flatMap(Set::stream)
                .allMatch(player::hasPermission);
    }

    @Override
    public boolean addPermission(String permission, Type type) {
        return switch (type) {
            case PLACE -> this.placePermissions.add(permission);
            case BREAK -> this.breakPermissions.add(permission);
            case INTERACT -> this.interactPermissions.add(permission);
        };
    }

    @Override
    public boolean removePermission(String permission, Type type) {
        return switch (type) {
            case PLACE -> this.placePermissions.remove(permission);
            case BREAK -> this.breakPermissions.remove(permission);
            case INTERACT -> this.interactPermissions.remove(permission);
        };
    }
}
