package me.general_breddok.blockdisplaycreator.nbt.entity;

import me.general_breddok.blockdisplaycreator.entity.EntityDetails;
import me.general_breddok.blockdisplaycreator.nbt.NbtContainer;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.util.List;

public interface NbtEntity extends NbtContainer, EntityDetails {
    Boolean getOnGround();

    void setOnGround(Boolean onGround);

    CompoundBinaryTag getData();

    void setData(CompoundBinaryTag data);

    <T extends NbtEntity> boolean addNbtPassenger(T passenger);

    boolean removeNbtPassenger(int index);

    <T extends NbtEntity> boolean removeNbtPassenger(T passenger);

    List<? extends NbtEntity> getNbtPassengers();

    void setNbtPassengers(List<? extends NbtEntity> passengers);
}
