package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;

public interface ConfiguredCollision extends ConfiguredEntity<Shulker>, DeepCloneable<ConfiguredCollision> {
    double getSize();
    void setSize(double size);
}
