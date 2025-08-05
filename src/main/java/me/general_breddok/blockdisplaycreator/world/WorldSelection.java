package me.general_breddok.blockdisplaycreator.world;

import lombok.Getter;
import lombok.Setter;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.version.MinecraftVersion;
import me.general_breddok.blockdisplaycreator.version.VersionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class WorldSelection extends BoundingBox {

    private static final Set<Material> ephemeralMaterials = new HashSet<>();

    static {
        VersionManager versionManager = BlockDisplayCreator.getInstance().getVersionManager();

        ephemeralMaterials.add(Material.LAVA);
        ephemeralMaterials.add(Material.WATER);

        ephemeralMaterials.add(Material.AIR);
        ephemeralMaterials.add(Material.CAVE_AIR);
        ephemeralMaterials.add(Material.VOID_AIR);
        ephemeralMaterials.add(Material.STRUCTURE_VOID);
        ephemeralMaterials.add(Material.LIGHT);

        ephemeralMaterials.add(getShortGrass());
        ephemeralMaterials.add(Material.TALL_GRASS);
        ephemeralMaterials.add(Material.FERN);
        ephemeralMaterials.add(Material.LARGE_FERN);
        ephemeralMaterials.add(Material.SEAGRASS);
        ephemeralMaterials.add(Material.TALL_SEAGRASS);
        ephemeralMaterials.add(Material.NETHER_SPROUTS);
        ephemeralMaterials.add(Material.WARPED_ROOTS);
        ephemeralMaterials.add(Material.CRIMSON_ROOTS);
        ephemeralMaterials.add(Material.DEAD_BUSH);
        ephemeralMaterials.add(Material.HANGING_ROOTS);
        ephemeralMaterials.add(Material.VINE);
        ephemeralMaterials.add(Material.GLOW_LICHEN);
        ephemeralMaterials.add(Material.SCULK_VEIN);

        ephemeralMaterials.add(Material.FIRE);
        ephemeralMaterials.add(Material.SOUL_FIRE);

        addLeafLitter(versionManager);
        addShortDryGrass(versionManager);
        addTallDryGrass(versionManager);
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

    public static boolean isEphemeral(List<Location> locationList) {
        return locationList.stream().allMatch(location -> isEphemeral(location.getBlock()));
    }

    public static boolean isEphemeral(Block block) {
        return isEphemeral(block.getType());
    }

    public static boolean isEphemeral(Material material) {
        return ephemeralMaterials.contains(material);
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

    public static boolean isEmptyBlock(@NotNull Material material) {
        switch (material) {
            case AIR, LAVA, WATER, VOID_AIR, STRUCTURE_VOID, CAVE_AIR -> {
                return true;
            }
        }
        return false;
    }

    private static Material getShortGrass() {
        MinecraftVersion version = BlockDisplayCreator.getInstance().getVersionManager().getCurrentVersion();
        if (version.isBelow(MinecraftVersion.V1_20_3)) {
            return Material.valueOf("GRASS");
        } else {
            return Material.valueOf("SHORT_GRASS");
        }
    }

    private static void addLeafLitter(VersionManager versionManager) {
        if (!versionManager.isVersionBefore1_21_5()) {
            ephemeralMaterials.add(Material.valueOf("LEAF_LITTER"));
        }
    }

    private static void addShortDryGrass(VersionManager versionManager) {
        if (!versionManager.isVersionBefore1_21_5()) {
            ephemeralMaterials.add(Material.valueOf("SHORT_DRY_GRASS"));
        }
    }

    private static void addTallDryGrass(VersionManager versionManager) {
        if (!versionManager.isVersionBefore1_21_5()) {
            ephemeralMaterials.add(Material.valueOf("TALL_DRY_GRASS"));
        }
    }

    public static boolean isSingleLayerSnow(Block block) {
        if (block.getType() != Material.SNOW) {
            return false;
        }
        Snow snow = (Snow) block.getBlockData();
        return snow.getLayers() == 1;
    }
}
