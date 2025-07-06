package me.general_breddok.blockdisplaycreator.serialize;

import org.bukkit.util.BoundingBox;

import java.io.Serial;
import java.io.Serializable;

public class SerialBoundingBox extends BoundingBox implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerialBoundingBox() {
        super();
    }

    public SerialBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }
}
