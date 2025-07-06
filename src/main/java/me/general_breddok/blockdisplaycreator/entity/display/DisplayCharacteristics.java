package me.general_breddok.blockdisplaycreator.entity.display;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

public interface DisplayCharacteristics extends EntityCharacteristics {
    Display.Billboard getBillboard();

    void setBillboard(Display.Billboard billboard);

    Display.Brightness getBrightness();

    void setBrightness(Display.Brightness brightness);

    Transformation getTransformation();

    void setTransformation(Transformation transformation);

    Color getGlowColorOverride();

    void setGlowColorOverride(Color glowColorOverride);

    Float getDisplayHeight();

    void setDisplayHeight(Float displayHeight);

    Float getDisplayWidth();

    void setDisplayWidth(Float displayWidth);

    Float getShadowRadius();

    void setShadowRadius(Float shadowRadius);

    Float getShadowStrength();

    void setShadowStrength(Float shadowStrength);

    Float getViewRange();

    void setViewRange(Float viewRange);

    Integer getInterpolationDelay();

    void setInterpolationDelay(Integer interpolationDelay);

    Integer getInterpolationDuration();

    void setInterpolationDuration(Integer interpolationDuration);
}
