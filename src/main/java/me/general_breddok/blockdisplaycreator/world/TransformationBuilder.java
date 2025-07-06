package me.general_breddok.blockdisplaycreator.world;

import me.general_breddok.blockdisplaycreator.common.Builder;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class TransformationBuilder implements Builder<Transformation> {
    @NotNull
    private Vector3f translation;
    @NotNull
    private Quaternionf leftRotation;
    @NotNull
    private Vector3f scale;
    @NotNull
    private Quaternionf rightRotation;

    public static final Transformation EMPTY = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(), new Quaternionf());

    public TransformationBuilder(@NotNull Transformation transformation) {
        this.translation = transformation.getTranslation();
        this.leftRotation = transformation.getLeftRotation();
        this.scale = transformation.getScale();
        this.rightRotation = transformation.getRightRotation();
    }

    public TransformationBuilder(@NotNull Display display) {
        this(display.getTransformation());
    }

    public TransformationBuilder() {
        this(EMPTY);
    }

    public TransformationBuilder setTranslation(Vector3f translation) {
        this.translation = translation;
        return this;
    }

    public TransformationBuilder setLeftRotation(Quaternionf leftRotation) {
        this.leftRotation = leftRotation;
        return this;
    }

    public TransformationBuilder setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public TransformationBuilder setRightRotation(Quaternionf rightRotation) {
        this.rightRotation = rightRotation;
        return this;
    }

    public TransformationBuilder setTranslation(float x, float y, float z) {
        return setTranslation(new Vector3f(x, y, z));
    }

    public TransformationBuilder setLeftRotation(float x, float y, float z, float w) {
        return setLeftRotation(new Quaternionf(x, y, z, w));
    }

    public TransformationBuilder setScale(float x, float y, float z) {
        return setScale(new Vector3f(x, y, z));
    }

    public TransformationBuilder setRightRotation(float x, float y, float z, float w) {
        return setRightRotation(new Quaternionf(x, y, z, w));
    }

    public TransformationBuilder addTranslation(float x, float y, float z) {
        this.translation.add(x, y, z);
        return this;
    }

    public TransformationBuilder addLeftRotation(float x, float y, float z, float w) {
        this.leftRotation.add(x, y, z, w);
        return this;
    }

    public TransformationBuilder addScale(float x, float y, float z) {
        this.scale.add(x, y, z);
        return this;
    }

    public TransformationBuilder addRightRotation(float x, float y, float z, float w) {
        this.rightRotation.add(x, y, z, w);
        return this;
    }

    public TransformationBuilder addTranslation(Vector3fc vector3fc) {
        this.translation.add(vector3fc);
        return this;
    }

    public TransformationBuilder addTranslation(Vector vector) {
        this.translation.add(vector.toVector3f());
        return this;
    }

    public TransformationBuilder addLeftRotation(Quaternionfc quaternionfc) {
        this.leftRotation.add(quaternionfc);
        return this;
    }

    public TransformationBuilder addScale(Vector3f vector3f) {
        this.scale.add(vector3f);
        return this;
    }

    public TransformationBuilder addScale(Vector vector) {
        this.scale.add(vector.toVector3f());
        return this;
    }

    public TransformationBuilder addRightRotation(Quaternionfc quaternionfc) {
        this.rightRotation.add(quaternionfc);
        return this;
    }

    public Transformation build() {
        return new Transformation(translation, leftRotation, scale, rightRotation);
    }
}
