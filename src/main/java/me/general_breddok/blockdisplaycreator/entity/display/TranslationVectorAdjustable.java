package me.general_breddok.blockdisplaycreator.entity.display;

import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;

public interface TranslationVectorAdjustable {
    DirectedVector getTranslation();

    void setTranslation(DirectedVector translation);
}
