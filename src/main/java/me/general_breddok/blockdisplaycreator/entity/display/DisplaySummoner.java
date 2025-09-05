package me.general_breddok.blockdisplaycreator.entity.display;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.EntitySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;


/**
 * A summoner for display entities that manages their spawn and properties.
 * <p>
 * This class extends {@link EntitySummoner} and implements {@link DisplayCharacteristics},
 * allowing the configuration and summoning of {@link Display} entities with detailed
 * visual and interpolation properties.
 * </p>
 *
 * @param <E> The type of {@link Display} entity this summoner manages.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisplaySummoner<E extends Display> extends EntitySummoner<E> implements DisplayCharacteristics {

    /**
     * The billboard mode for the display entity.
     */
    Display.Billboard billboard;

    /**
     * The brightness of the display entity.
     */
    Display.Brightness brightness;

    /**
     * The transformation applied to the display.
     */
    Transformation transformation;

    /**
     * Optional override for the glow color.
     */
    Color glowColorOverride;

    /**
     * Height of the display.
     */
    Float displayHeight;

    /**
     * Width of the display.
     */
    Float displayWidth;

    /**
     * Radius of the shadow.
     */
    Float shadowRadius;

    /**
     * Strength of the shadow.
     */
    Float shadowStrength;

    /**
     * View range of the display.
     */
    Float viewRange;

    /**
     * Delay before interpolation is applied.
     */
    Integer interpolationDelay;

    /**
     * Duration of interpolation.
     */
    Integer interpolationDuration;

    /**
     * Constructs a DisplaySummoner for the specified entity class.
     *
     * @param entityClass the class of the display entity
     */
    public DisplaySummoner(@NotNull Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * Constructs a DisplaySummoner using existing display characteristics.
     *
     * @param entityClass     the class of the display entity
     * @param characteristics the characteristics to copy
     */
    public DisplaySummoner(@NotNull Class<E> entityClass, DisplayCharacteristics characteristics) {
        super(entityClass, characteristics);

        billboard = characteristics.getBillboard();
        brightness = characteristics.getBrightness();
        transformation = characteristics.getTransformation();
        glowColorOverride = characteristics.getGlowColorOverride();

        displayHeight = characteristics.getDisplayHeight();
        displayWidth = characteristics.getDisplayWidth();
        shadowRadius = characteristics.getShadowRadius();
        shadowStrength = characteristics.getShadowStrength();
        viewRange = characteristics.getViewRange();

        interpolationDelay = characteristics.getInterpolationDelay();
        interpolationDuration = characteristics.getInterpolationDuration();
    }

    /**
     * Checks whether the specified entity type is a type of display entity.
     *
     * @param entityType the entity type to check
     * @return true if the entity is a display entity; false otherwise
     */
    public static boolean isDisplayEntity(@NotNull EntityType entityType) {
        return OperationUtil.orEquals(entityType, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY);
    }

    /**
     * Summons the display entity at the specified location,
     * applying all configured display properties.
     *
     * @param location the location to summon the entity
     * @return the summoned display entity
     */
    @Override
    public E summon(@NotNull Location location) {
        E display = super.summon(location);

        OperationUtil.doIfNotNull(billboard, display::setBillboard);
        OperationUtil.doIfNotNull(brightness, display::setBrightness);
        OperationUtil.doIfNotNull(transformation, display::setTransformation);
        OperationUtil.doIfNotNull(glowColorOverride, display::setGlowColorOverride);

        OperationUtil.doIfNotNull(displayHeight, display::setDisplayHeight);
        OperationUtil.doIfNotNull(displayWidth, display::setDisplayWidth);
        OperationUtil.doIfNotNull(shadowRadius, display::setShadowRadius);
        OperationUtil.doIfNotNull(shadowStrength, display::setShadowStrength);
        OperationUtil.doIfNotNull(viewRange, display::setViewRange);

        OperationUtil.doIfNotNull(interpolationDelay, display::setInterpolationDelay);
        OperationUtil.doIfNotNull(interpolationDuration, display::setInterpolationDuration);

        return display;
    }

    /**
     * Creates a deep clone of this DisplaySummoner, including all display-specific properties.
     *
     * @return a cloned DisplaySummoner instance
     */
    @Override
    public DisplaySummoner<E> clone() {
        DisplaySummoner<E> cloned = (DisplaySummoner<E>) super.clone();

        cloned.billboard = this.billboard;
        cloned.brightness = this.brightness;
        cloned.transformation = this.transformation;
        cloned.glowColorOverride = this.glowColorOverride;
        cloned.displayHeight = this.displayHeight;
        cloned.displayWidth = this.displayWidth;
        cloned.shadowRadius = this.shadowRadius;
        cloned.shadowStrength = this.shadowStrength;
        cloned.viewRange = this.viewRange;
        cloned.interpolationDelay = this.interpolationDelay;
        cloned.interpolationDuration = this.interpolationDuration;

        return cloned;
    }
}
