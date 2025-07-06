package me.general_breddok.blockdisplaycreator.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiStatus.Obsolete
public class WorldSelection extends BoundingBox {

    private static final List<Material> destructibleMaterials = new ArrayList<>();

    static {
        destructibleMaterials.add(Material.LAVA);
        destructibleMaterials.add(Material.WATER);
        destructibleMaterials.add(getShortGrass());
        destructibleMaterials.add(Material.TALL_GRASS);
        destructibleMaterials.add(Material.FERN);
        destructibleMaterials.add(Material.LARGE_FERN);
        destructibleMaterials.add(Material.SEAGRASS);
        destructibleMaterials.add(Material.TALL_SEAGRASS);
        destructibleMaterials.add(Material.AIR);
        destructibleMaterials.add(Material.CAVE_AIR);
        destructibleMaterials.add(Material.VOID_AIR);
        destructibleMaterials.add(Material.LIGHT);
    }

    private World world;

    public WorldSelection() {
        super();
    }

    public WorldSelection(double x1, double y1, double z1, double x2, double y2, double z2, World world) {
        super(x1, y1, z1, x2, y2, z2);
        this.world = world;
    }

    public WorldSelection(Location corner1, Location corner2, World world) {
        this(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ(), world);
    }


    public  WorldSelection(BoundingBox boundingBox, World world) {
        this(boundingBox.getMin().toLocation(world), boundingBox.getMax().toLocation(world), world);
    }

    public static boolean isDestructible(List<Location> locationList) {
        return locationList.stream().allMatch(location -> isDestructible(location.getBlock()));
    }

    public static boolean isDestructible(Block block) {
        return isDestructible(block.getType());
    }

    public static boolean isDestructible(Material material) {
        return destructibleMaterials.contains(material);
    }

    public Location getMinLocation() {
        return this.getMin().toLocation(world);
    }

    public Location getMaxLocation() {
        return this.getMax().toLocation(world);
    }

    public List<Location> getLocations() {

        List<Location> locations = new ArrayList<>();
        for (int x = (int) Math.floor(this.getMinX()); x <= Math.floor(this.getMaxX()); x++) {
            for (int y = (int) Math.floor(this.getMinY()); y <= Math.floor(this.getMaxY()); y++) {
                for (int z = (int) Math.floor(this.getMinZ()); z <= Math.floor(this.getMaxZ()); z++) {
                    Location location = new Location(world, x, y, z);
                    locations.add(location);
                }
            }
        }
        return locations;
    }



    private static Material getShortGrass() {
        String rawVersion = Bukkit.getBukkitVersion();
        String cleanVersion = rawVersion.split("-")[0];
        if (isEarlier1_20_3(cleanVersion)) {
            return Material.valueOf("GRASS");
        } else {
            return Material.valueOf("SHORT_GRASS");
        }
    }

    private static boolean isEarlier1_20_3(String version) {
        return switch (version) {
            case  "1.19.4", "1.20", "1.20.1", "1.20.2" -> true;
            default -> false;
        };
    }

    public static boolean isEmptyBlock(@NotNull Material material) {
        switch (material) {
            case AIR, LAVA, WATER, VOID_AIR, STRUCTURE_VOID, CAVE_AIR -> {
                return true;
            }
        }
        return false;
    }
}
