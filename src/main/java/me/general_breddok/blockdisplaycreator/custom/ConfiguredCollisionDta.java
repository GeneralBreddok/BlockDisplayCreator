package me.general_breddok.blockdisplaycreator.custom;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.Summoner;
import me.general_breddok.blockdisplaycreator.entity.living.ShulkerSummoner;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Shulker;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguredCollisionDta implements ConfiguredCollision {
    Summoner<Shulker> summoner;
    Vector offset;
    String identifier;
    double size;

    @Override
    public Shulker summon(@NotNull Location location) {
        Location summonLocation = location.clone().add(offset);

        Shulker shulker = this.summoner.summon(summonLocation);

        BlockDisplay vehicle = location.getWorld().spawn(summonLocation, BlockDisplay.class);

        shulker.setRemoveWhenFarAway(false);
        shulker.setAI(false);
        shulker.setGravity(false);
        shulker.setInvulnerable(true);

        this.setScale(shulker, this.size);

        vehicle.addPassenger(shulker);

        return shulker;
    }

    private void setScale(Shulker shulker, double size) {
        if (size <= 0) {
            return;
        }

        Attribute scaleAttribute = getScaleAttribute();

        if (scaleAttribute == null)
            return;

        shulker.getAttribute(scaleAttribute).setBaseValue(size);
    }

    private static boolean isEarlier1_20_5(String version) {
        return switch (version) {
            case "1.19.4", "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4" -> true;
            default -> false;
        };
    }

    private static boolean isEarlier1_21_3(String version) {
        return switch (version) {
            case "1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.2" -> true;
            default -> false;
        };
    }

    private Attribute getScaleAttribute() {
        String rawVersion = Bukkit.getBukkitVersion();
        String cleanVersion = rawVersion.split("-")[0];

        if (isEarlier1_20_5(cleanVersion)) {
            return null;
        } else if (isEarlier1_21_3(cleanVersion)) {
            return Attribute.valueOf("GENERIC_SCALE");
        } else {
            return Registry.ATTRIBUTE.get(NamespacedKey.minecraft("scale"));
        }
    }

    @Override
    public ConfiguredCollisionDta clone() {
        Summoner<Shulker> clonedSummoner = DeepCloneable.tryClone(this.summoner);
        return new ConfiguredCollisionDta(clonedSummoner, this.offset.clone(), this.identifier, this.size);
    }
}
