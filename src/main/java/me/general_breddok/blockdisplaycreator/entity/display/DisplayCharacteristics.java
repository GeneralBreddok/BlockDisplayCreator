package me.general_breddok.blockdisplaycreator.entity.display;

import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

/**
 * Represents the characteristics of a display entity.
 * <p>
 * This interface extends {@link EntityCharacteristics} and provides
 * methods to manage properties specific to display entities,
 * such as visual appearance, transformation, size, glow, shadows,
 * and interpolation settings.
 * </p>
 */
public interface DisplayCharacteristics extends EntityCharacteristics {

    /**
     * Gets the billboard type of the display.
     *
     * @return the {@link Display.Billboard} type
     */
    Display.Billboard getBillboard();

    /**
     * Sets the billboard type of the display.
     *
     * @param billboard the {@link Display.Billboard} type
     */
    void setBillboard(Display.Billboard billboard);

    /**
     * Gets the brightness setting of the display.
     *
     * @return the {@link Display.Brightness} value
     */
    Display.Brightness getBrightness();

    /**
     * Sets the brightness of the display.
     *
     * @param brightness the {@link Display.Brightness} value
     */
    void setBrightness(Display.Brightness brightness);

    /**
     * Gets the transformation applied to the display.
     *
     * @return the {@link Transformation} object
     */
    Transformation getTransformation();

    /**
     * Sets the transformation for the display.
     *
     * @param transformation the {@link Transformation} object
     */
    void setTransformation(Transformation transformation);

    /**
     * Gets the override glow color of the display, if any.
     *
     * @return the {@link Color} used for glow, or null if none
     */
    Color getGlowColorOverride();

    /**
     * Sets the override glow color of the display.
     *
     * @param glowColorOverride the {@link Color} to use for glow
     */
    void setGlowColorOverride(Color glowColorOverride);

    /**
     * Gets the height of the display.
     *
     * @return the display height in blocks
     */
    Float getDisplayHeight();

    /**
     * Sets the height of the display.
     *
     * @param displayHeight the display height in blocks
     */
    void setDisplayHeight(Float displayHeight);

    /**
     * Gets the width of the display.
     *
     * @return the display width in blocks
     */
    Float getDisplayWidth();

    /**
     * Sets the width of the display.
     *
     * @param displayWidth the display width in blocks
     */
    void setDisplayWidth(Float displayWidth);

    /**
     * Gets the radius of the display's shadow.
     *
     * @return the shadow radius
     */
    Float getShadowRadius();

    /**
     * Sets the radius of the display's shadow.
     *
     * @param shadowRadius the shadow radius
     */
    void setShadowRadius(Float shadowRadius);

    /**
     * Gets the strength of the display's shadow.
     *
     * @return the shadow strength
     */
    Float getShadowStrength();

    /**
     * Sets the strength of the display's shadow.
     *
     * @param shadowStrength the shadow strength
     */
    void setShadowStrength(Float shadowStrength);

    /**
     * Gets the view range of the display entity.
     *
     * @return the view range in blocks
     */
    Float getViewRange();

    /**
     * Sets the view range of the display entity.
     *
     * @param viewRange the view range in blocks
     */
    void setViewRange(Float viewRange);

    /**
     * Gets the interpolation delay applied to the display.
     *
     * @return the interpolation delay in ticks
     */
    Integer getInterpolationDelay();

    /**
     * Sets the interpolation delay for the display.
     *
     * @param interpolationDelay the delay in ticks
     */
    void setInterpolationDelay(Integer interpolationDelay);

    /**
     * Gets the duration of interpolation applied to the display.
     *
     * @return the interpolation duration in ticks
     */
    Integer getInterpolationDuration();

    /**
     * Sets the duration of interpolation for the display.
     *
     * @param interpolationDuration the duration in ticks
     */
    void setInterpolationDuration(Integer interpolationDuration);
}
