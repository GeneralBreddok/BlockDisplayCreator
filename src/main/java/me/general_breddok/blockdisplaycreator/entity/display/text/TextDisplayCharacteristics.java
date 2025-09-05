package me.general_breddok.blockdisplaycreator.entity.display.text;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.entity.TextDisplay;

/**
 * Represents the characteristics of a text display entity.
 * <p>
 * Extends {@link DisplayCharacteristics} to include properties specific to text,
 * such as content, alignment, background, shadow, and text opacity.
 * </p>
 */
public interface TextDisplayCharacteristics extends DisplayCharacteristics {

    /**
     * Gets the text displayed by this entity.
     *
     * @return the text string
     */
    String getText();

    /**
     * Sets the text to be displayed by this entity.
     *
     * @param text the text string
     */
    void setText(String text);

    /**
     * Gets the alignment of the text.
     *
     * @return the text alignment
     */
    TextDisplay.TextAlignment getAlignment();

    /**
     * Sets the alignment of the text.
     *
     * @param alignment the desired text alignment
     */
    void setAlignment(TextDisplay.TextAlignment alignment);

    /**
     * Determines whether the default background is enabled.
     *
     * @return true if default background is used, false otherwise
     */
    Boolean getDefaultBackground();

    /**
     * Sets whether the default background is enabled.
     *
     * @param defaultBackground true to use default background, false otherwise
     */
    void setDefaultBackground(Boolean defaultBackground);

    /**
     * Determines whether the text is see-through.
     *
     * @return true if see-through is enabled, false otherwise
     */
    Boolean getSeeThrough();

    /**
     * Sets whether the text should be see-through.
     *
     * @param seeThrough true to enable see-through, false otherwise
     */
    void setSeeThrough(Boolean seeThrough);

    /**
     * Determines whether the text is shadowed.
     *
     * @return true if shadow is enabled, false otherwise
     */
    Boolean getShadowed();

    /**
     * Sets whether the text should have a shadow.
     *
     * @param shadowed true to enable shadow, false otherwise
     */
    void setShadowed(Boolean shadowed);

    /**
     * Gets the line width of the text.
     *
     * @return the line width in pixels
     */
    Integer getLineWidth();

    /**
     * Sets the line width of the text.
     *
     * @param lineWidth the line width in pixels
     */
    void setLineWidth(Integer lineWidth);

    /**
     * Gets the opacity of the text.
     *
     * @return the text opacity as a byte value
     */
    Byte getTextOpacity();

    /**
     * Sets the opacity of the text.
     *
     * @param textOpacity the opacity value as a byte
     */
    void setTextOpacity(Byte textOpacity);
}
