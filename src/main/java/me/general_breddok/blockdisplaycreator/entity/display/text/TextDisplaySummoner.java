package me.general_breddok.blockdisplaycreator.entity.display.text;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.entity.display.DisplaySummoner;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a summoner for {@link TextDisplay} entities with full configuration of
 * text-specific properties.
 * <p>
 * Extends {@link DisplaySummoner} and implements {@link TextDisplayCharacteristics}
 * to allow setting text, alignment, background, shadow, line width, and text opacity
 * when spawning the entity.
 * </p>
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextDisplaySummoner extends DisplaySummoner<TextDisplay> implements TextDisplayCharacteristics {

    /**
     * The text content to display.
     */
    String text;

    /**
     * Alignment of the text within the display.
     */
    TextDisplay.TextAlignment alignment;

    /**
     * Whether the default background should be used.
     */
    Boolean defaultBackground;

    /**
     * Whether the text should be see-through.
     */
    Boolean seeThrough;

    /**
     * Whether the text should be shadowed.
     */
    Boolean shadowed;

    /**
     * Width of a line of text in pixels.
     */
    Integer lineWidth;

    /**
     * Opacity of the text as a byte value.
     */
    Byte textOpacity;

    /**
     * Constructs a new TextDisplaySummoner with default properties.
     * <p>
     * The entity class is set to {@link TextDisplay}.
     * </p>
     */
    public TextDisplaySummoner() {
        super(TextDisplay.class);
    }

    /**
     * Constructs a new TextDisplaySummoner from existing characteristics.
     *
     * @param characteristics the source characteristics to copy values from
     */
    public TextDisplaySummoner(TextDisplayCharacteristics characteristics) {
        super(TextDisplay.class, characteristics);

        text = characteristics.getText();
        alignment = characteristics.getAlignment();
        defaultBackground = characteristics.getDefaultBackground();
        seeThrough = characteristics.getSeeThrough();
        shadowed = characteristics.getShadowed();
        lineWidth = characteristics.getLineWidth();
        textOpacity = characteristics.getTextOpacity();
    }

    /**
     * Spawns a {@link TextDisplay} at the given location with all configured properties applied.
     *
     * @param location the spawn location
     * @return the spawned TextDisplay entity
     */
    @Override
    public TextDisplay summon(@NotNull Location location) {
        TextDisplay textDisplay = super.summon(location);

        OperationUtil.doIfNotNull(text, textDisplay::setText);
        OperationUtil.doIfNotNull(alignment, textDisplay::setAlignment);
        OperationUtil.doIfNotNull(defaultBackground, textDisplay::setDefaultBackground);
        OperationUtil.doIfNotNull(seeThrough, textDisplay::setSeeThrough);
        OperationUtil.doIfNotNull(shadowed, textDisplay::setShadowed);
        OperationUtil.doIfNotNull(lineWidth, textDisplay::setLineWidth);
        OperationUtil.doIfNotNull(textOpacity, textDisplay::setTextOpacity);

        return textDisplay;
    }

    /**
     * Creates a deep clone of this summoner, including all text-specific and display properties.
     *
     * @return a cloned TextDisplaySummoner instance
     */
    @Override
    public TextDisplaySummoner clone() {
        TextDisplaySummoner cloned = (TextDisplaySummoner) super.clone();

        cloned.text = this.text;
        cloned.alignment = this.alignment;
        cloned.defaultBackground = this.defaultBackground;
        cloned.seeThrough = this.seeThrough;
        cloned.shadowed = this.shadowed;
        cloned.lineWidth = this.lineWidth;
        cloned.textOpacity = this.textOpacity;

        return cloned;
    }
}

