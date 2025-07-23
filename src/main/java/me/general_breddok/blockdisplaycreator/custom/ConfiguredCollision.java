package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;

public interface ConfiguredCollision extends ConfiguredEntity<Shulker>, DeepCloneable<ConfiguredCollision> {
    double getSize();
    void setSize(double size);
    boolean isDisableBelow1_20_5();
    void setDisableBelow1_20_5(boolean disableBelow1_20_5);
}
