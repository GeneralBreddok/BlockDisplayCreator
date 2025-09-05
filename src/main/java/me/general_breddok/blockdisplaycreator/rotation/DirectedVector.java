package me.general_breddok.blockdisplaycreator.rotation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectedVector extends Vector implements DeepCloneable<DirectedVector> {
    float yaw;
    float pitch;

    public DirectedVector() {
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public DirectedVector(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public DirectedVector(int x, int y, int z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public DirectedVector(double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public DirectedVector(float x, float y, float z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static DirectedVector fromLocation(Location loc) {
        return new DirectedVector(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void setYaw(double yaw) {
        this.yaw = (float) yaw;
    }

    public void setPitch(double pitch) {
        this.pitch = (float) pitch;
    }

    public float getRoundedYaw() {
        return Math.round(yaw);
    }

    public float getRoundedPitch() {
        return Math.round(pitch);
    }

    public Location toLocation(@Nullable World world) {
        return new Location(world, getX(), getY(), getZ(), yaw, pitch);
    }

    public DirectedVector clone() {
        return (DirectedVector) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DirectedVector other)) return false;

        return super.equals(other) && this.yaw == other.yaw && this.pitch == other.pitch;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Float.floatToIntBits(yaw);
        result = 31 * result + Float.floatToIntBits(pitch);
        return result;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + yaw + "," + pitch;
    }
}
