package me.general_breddok.blockdisplaycreator.version;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class VersionCompat {
    private boolean soundEnum = BlockDisplayCreator.getInstance().getVersionManager().isVersionBefore1_21_3();
    @Getter
    private List<String> soundNames;

    static {
        if (soundEnum) {
            soundNames = Arrays.stream(Sound.values()).map(Sound::name).toList();
        } else {
            soundNames = Arrays.stream(Sound.class.getFields()).map(Field::getName).toList();
        }
    }

    public Attribute getScaleAttribute() {
        VersionManager versionManager = BlockDisplayCreator.getInstance().getVersionManager();

        if (versionManager.isVersionBefore1_20_5()) {
            return null;
        } else if (versionManager.isVersionBefore1_21_3()) {
            return Attribute.valueOf("GENERIC_SCALE");
        } else {
            return Registry.ATTRIBUTE.get(NamespacedKey.minecraft("scale"));
        }
    }

    public boolean isSoundEnum() {
        return soundEnum;
    }
}
