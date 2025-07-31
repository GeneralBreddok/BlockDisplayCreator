package me.general_breddok.blockdisplaycreator.custom;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.Summoner;
import me.general_breddok.blockdisplaycreator.entity.living.ShulkerSummoner;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.version.MinecraftVersion;
import me.general_breddok.blockdisplaycreator.version.VersionCompat;
import me.general_breddok.blockdisplaycreator.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Shulker;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguredCollisionDta implements ConfiguredCollision {
    @NotNull
    Summoner<Shulker> summoner;
    final String identifier;
    @Nullable
    Vector offset;
    double size;
    boolean disableBelow1_20_5;


    public ConfiguredCollisionDta(Summoner<Shulker> summoner, String identifier, Vector offset, double size) {
        this(summoner, identifier, offset, size, false);
    }

    public ConfiguredCollisionDta(Summoner<Shulker> summoner, String identifier, Vector offset) {
        this(summoner, identifier, offset,1, false);
    }

    public ConfiguredCollisionDta(Summoner<Shulker> summoner, String identifier) {
        this(summoner, identifier, null, 1, false);
    }


    @Override
    public Shulker summon(@NotNull Location location) {
        VersionManager versionManager = BlockDisplayCreator.getInstance().getVersionManager();
        if (this.disableBelow1_20_5 && versionManager.isVersionBefore1_20_5()) {
            return null;
        }

        Location summonLocation = location.clone();

        if (this.offset != null) {
            summonLocation.add(this.offset);
        }

        Shulker shulker = this.summoner.summon(summonLocation);

        BlockDisplay vehicle = location.getWorld().spawn(summonLocation, BlockDisplay.class);

        shulker.setRemoveWhenFarAway(false);
        shulker.setAI(false);
        shulker.setGravity(false);
        shulker.setInvulnerable(true);
        shulker.setPersistent(true);

        this.setScale(shulker, this.size);

        vehicle.addPassenger(shulker);

        return shulker;
    }

    public static void setScale(Shulker shulker, double size) {
        if (size <= 0 || size == 1) {
            return;
        }

        Attribute scaleAttribute = VersionCompat.getScaleAttribute();

        if (scaleAttribute == null)
            return;

        shulker.getAttribute(scaleAttribute).setBaseValue(size);
    }

    @Override
    public ConfiguredCollisionDta clone() {
        Summoner<Shulker> clonedSummoner = DeepCloneable.tryClone(this.summoner);
        Vector clonedOffset = null;

        if (this.offset != null) {
            clonedOffset = this.offset.clone();
        }
        return new ConfiguredCollisionDta(clonedSummoner, this.identifier, clonedOffset, this.size, this.disableBelow1_20_5);
    }
}
