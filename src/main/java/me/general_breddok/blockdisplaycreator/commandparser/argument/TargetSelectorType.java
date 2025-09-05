package me.general_breddok.blockdisplaycreator.commandparser.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TargetSelectorType {
    NEAREST_PLAYER("@p"),
    ALL_PLAYERS("@a"),
    RANDOM_PLAYER("@r"),
    SELF("@s"),
    ALL_ENTITIES("@e");

    private static final Map<String, TargetSelectorType> VARIABLES = Map.of(
            "@p", NEAREST_PLAYER,
            "@a", ALL_PLAYERS,
            "@r", RANDOM_PLAYER,
            "@s", SELF,
            "@e", ALL_ENTITIES
    );
    String variable;

    public static Player getNearestPlayer(Location location, @Nullable Player defaultPlayer) {
        Player nearestPlayer = defaultPlayer;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : location.getWorld().getPlayers()) {
            double distance = player.getLocation().distance(location);
            if (distance < closestDistance) {
                closestDistance = distance;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }

    public static List<Player> getAllPlayers(World world) {
        return world.getPlayers();
    }

    public static Player getRandomPlayer() {
        List<? extends Player> players = Bukkit.getOnlinePlayers().stream().toList();

        if (players.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(players.size());

        return players.get(randomIndex);
    }

    public static List<Entity> getAllEntities(World world) {
        return world.getEntities();
    }

    public static boolean isSelector(String str) {
        return VARIABLES.containsKey(str);
    }

    public static boolean isSelectorOrPlayer(String str) {
        return VARIABLES.containsKey(str) ||
                Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .anyMatch(playerName -> playerName.equals(str));
    }

    public static TargetSelectorType parseString(String str) {
        return VARIABLES.get(str);
    }

    public static Set<String> getVariables() {
        return VARIABLES.keySet();
    }

    public static Set<String> getPlayerVariables() {
        Set<String> set = VARIABLES.keySet();
        set.remove(TargetSelectorType.ALL_ENTITIES.getVariable());
        return set;
    }
}
