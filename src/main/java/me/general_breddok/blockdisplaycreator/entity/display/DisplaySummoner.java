package me.general_breddok.blockdisplaycreator.entity.display;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.EntitySummoner;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.world.TransformationBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisplaySummoner<E extends Display> extends EntitySummoner<E> implements TranslationVectorAdjustable, DisplayCharacteristics {
    DirectedVector translation;

    Display.Billboard billboard;
    Display.Brightness brightness;
    Transformation transformation;
    Color glowColorOverride;

    Float displayHeight;
    Float displayWidth;
    Float shadowRadius;
    Float shadowStrength;
    Float viewRange;

    Integer interpolationDelay;
    Integer interpolationDuration;


    public DisplaySummoner(@NotNull Class<E> entityClass) {
        super(entityClass);
    }

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

    public static boolean isDisplayEntity(@NotNull EntityType entityType) {
        return OperationUtil.orEquals(entityType, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY);
    }

    @Override
    public E summon(@NotNull Location location) {
        E display = super.summon(location);

        OperationUtil.doIfNotNull(translation, () -> display.setTransformation(new TransformationBuilder(display).addTranslation(translation).build()));
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

    @Override
    public DisplaySummoner<E> clone() {
        DisplaySummoner<E> cloned = (DisplaySummoner<E>) super.clone();

        cloned.setTranslation(this.translation.clone());

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
