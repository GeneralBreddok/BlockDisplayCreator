package me.general_breddok.blockdisplaycreator.entity;

public interface SpigotEntityCharacteristics extends EntityCharacteristics {
    Boolean getPersistent();

    void setPersistent(Boolean persistent);

    Boolean getOp();

    void setOp(Boolean op);

    Integer getTicksLived();

    void setTicksLived(Integer ticksLived);

    Boolean getVisibleByDefault();

    void setVisibleByDefault(Boolean visibleByDefault);
}
