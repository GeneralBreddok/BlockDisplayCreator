package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@UtilityClass
public class MathUtil {

    public Float[] toArray(Vector3f vector3f) {
        return new Float[]{vector3f.x, vector3f.y, vector3f.z};
    }

    public Float[] toArray(Quaternionf quaternionf) {
        return new Float[]{quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w};
    }

    public float[] toMatrix(Transformation transformation) {
        Vector3f translation = transformation.getTranslation();
        Quaternionf leftRotation = transformation.getLeftRotation();
        Vector3f scale = transformation.getScale();

        Matrix4f matrix = new Matrix4f();

        matrix.scale(scale);

        matrix.rotate(leftRotation);

        matrix.translate(translation);

        float[] matrixArray = new float[16];
        matrix.get(matrixArray);

        return matrixArray;
    }

    /**
     * Checks if the Y and Z components of the vector are 0.
     *
     * @param vector the vector to check
     * @return true if Y and Z are 0, false otherwise
     */
    public static boolean isXOnly(Vector vector) {
        return vector.getY() == 0 && vector.getZ() == 0;
    }

    /**
     * Checks if the X and Z components of the vector are 0.
     *
     * @param vector the vector to check
     * @return true if X and Z are 0, false otherwise
     */
    public static boolean isYOnly(Vector vector) {
        return vector.getX() == 0 && vector.getZ() == 0;
    }

    /**
     * Checks if the X and Y components of the vector are 0.
     *
     * @param vector the vector to check
     * @return true if X and Y are 0, false otherwise
     */
    public static boolean isZOnly(Vector vector) {
        return vector.getX() == 0 && vector.getY() == 0;
    }

    public Vector distance(Vector vector, float angle) {
        Vector newVector = vector.rotateAroundY(angle);

        return vector.clone().subtract(newVector);
    }

    public Transformation createTransformation(float[] matrix) {
        if (matrix.length != 16) {
            return null;
        }

        Vector3f translation = new Vector3f(matrix[12], matrix[13], matrix[14]);

        Vector3f scale = new Vector3f(matrix[0], matrix[5], matrix[10]);

        Quaternionf leftRotation = extractRotation(matrix);

        // To change
        Quaternionf rightRotation = extractRotation(matrix);

        return new Transformation(translation, leftRotation, scale, rightRotation);
    }

    private Quaternionf extractRotation(float[] matrix) {
        float m00 = matrix[0];
        float m01 = matrix[1];
        float m02 = matrix[2];
        float m10 = matrix[4];
        float m11 = matrix[5];
        float m12 = matrix[6];
        float m20 = matrix[8];
        float m21 = matrix[9];
        float m22 = matrix[10];

        float trace = m00 + m11 + m22;
        Quaternionf quaternion = new Quaternionf();

        if (trace > 0) {
            float s = (float) Math.sqrt(trace + 1.0) * 2; // 4 * s = 4 * sqrt(1 + trace)
            quaternion.w = 0.25f * s;
            quaternion.x = (m21 - m12) / s;
            quaternion.y = (m02 - m20) / s;
            quaternion.z = (m10 - m01) / s;
        } else {
            if (m00 > m11 && m00 > m22) {
                float s = (float) Math.sqrt(1.0 + m00 - m11 - m22) * 2; // 4 * s = 4 * sqrt(1 + m00 - m11 - m22)
                quaternion.w = (m21 - m12) / s;
                quaternion.x = 0.25f * s;
                quaternion.y = (m01 + m10) / s;
                quaternion.z = (m02 + m20) / s;
            } else if (m11 > m22) {
                float s = (float) Math.sqrt(1.0 + m11 - m00 - m22) * 2; // 4 * s = 4 * sqrt(1 + m11 - m00 - m22)
                quaternion.w = (m02 - m20) / s;
                quaternion.x = (m01 + m10) / s;
                quaternion.y = 0.25f * s;
                quaternion.z = (m12 + m21) / s;
            } else {
                float s = (float) Math.sqrt(1.0 + m22 - m00 - m11) * 2; // 4 * s = 4 * sqrt(1 + m22 - m00 - m11)
                quaternion.w = (m10 - m01) / s;
                quaternion.x = (m02 + m20) / s;
                quaternion.y = (m12 + m21) / s;
                quaternion.z = 0.25f * s;
            }
        }

        return quaternion.normalize();
    }
}
