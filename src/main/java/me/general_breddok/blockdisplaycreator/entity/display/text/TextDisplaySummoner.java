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


@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextDisplaySummoner extends DisplaySummoner<TextDisplay> implements TextDisplayCharacteristics {
    String text;
    TextDisplay.TextAlignment alignment;
    Boolean defaultBackground;
    Boolean seeThrough;
    Boolean shadowed;
    Integer lineWidth;
    Byte textOpacity;

    public TextDisplaySummoner() {
        super(TextDisplay.class);
    }

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
