package me.general_breddok.blockdisplaycreator.entity.living;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;

public interface LivingEntityCharacteristics extends EntityCharacteristics {
    Integer getRemainingAir();

    void setRemainingAir(Integer ticks);

    Integer getMaximumAir();

    void setMaximumAir(Integer ticks);

    Integer getArrowCooldown();

    void setArrowCooldown(Integer ticks);

    Integer getArrowsInBody();

    void setArrowsInBody(Integer count);

    Integer getMaximumNoDamageTicks();

    void setMaximumNoDamageTicks(Integer ticks);

    Integer getNoDamageTicks();

    void setNoDamageTicks(Integer ticks);

    Boolean getRemoveWhenFarAway();

    void setRemoveWhenFarAway(Boolean remove);

    Boolean getCanPickupItems();

    void setCanPickupItems(Boolean pickup);

    Boolean hasAI();

    void setAI(Boolean ai);

    boolean getInvisible();

    void setInvisible(boolean invisible);
}
