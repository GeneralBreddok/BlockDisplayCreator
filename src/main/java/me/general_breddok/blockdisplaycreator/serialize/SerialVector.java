package me.general_breddok.blockdisplaycreator.serialize;

import org.bukkit.util.Vector;

import java.io.Serial;
import java.io.Serializable;

public class SerialVector extends Vector implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerialVector() {
        super();
    }

    public SerialVector(int x, int y, int z) {
        super(x, y, z);
    }

    public SerialVector(double x, double y, double z) {
        super(x, y, z);
    }

    public SerialVector(float x, float y, float z) {
        super(x, y, z);
    }
}
