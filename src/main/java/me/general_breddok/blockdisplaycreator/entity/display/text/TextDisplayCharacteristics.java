package me.general_breddok.blockdisplaycreator.entity.display.text;

import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import org.bukkit.entity.TextDisplay;

public interface TextDisplayCharacteristics extends DisplayCharacteristics {
    String getText();

    void setText(String text);

    TextDisplay.TextAlignment getAlignment();

    void setAlignment(TextDisplay.TextAlignment alignment);

    Boolean getDefaultBackground();

    void setDefaultBackground(Boolean defaultBackground);

    Boolean getSeeThrough();

    void setSeeThrough(Boolean seeThrough);

    Boolean getShadowed();

    void setShadowed(Boolean shadowed);

    Integer getLineWidth();

    void setLineWidth(Integer lineWidth);

    Byte getTextOpacity();

    void setTextOpacity(Byte textOpacity);
}
