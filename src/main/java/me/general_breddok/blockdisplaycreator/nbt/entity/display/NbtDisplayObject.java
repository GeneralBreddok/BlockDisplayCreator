package me.general_breddok.blockdisplaycreator.nbt.entity.display;

import me.general_breddok.blockdisplaycreator.common.ColorConverter;
import me.general_breddok.blockdisplaycreator.entity.EntityCharacteristics;
import me.general_breddok.blockdisplaycreator.entity.display.DisplayCharacteristics;
import me.general_breddok.blockdisplaycreator.nbt.adventure.AdventureTagBuilder;
import me.general_breddok.blockdisplaycreator.nbt.entity.NbtEntityObject;
import me.general_breddok.blockdisplaycreator.util.MathUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;

public class NbtDisplayObject extends NbtEntityObject implements NbtDisplay {
    public NbtDisplayObject(@NotNull CompoundBinaryTag compoundBinaryTag) {
        super(compoundBinaryTag);
    }

    public NbtDisplayObject(@NotNull String snbt) throws IOException {
        super(snbt);
    }

    public NbtDisplayObject(@NotNull EntityCharacteristics chtc) {
        super(chtc);
        if (chtc instanceof DisplayCharacteristics displayChtc) {
            this.setBillboard(displayChtc.getBillboard());
            this.setBrightness(displayChtc.getBrightness());
            this.setTransformation(displayChtc.getTransformation());
            this.setDisplayWidth(displayChtc.getDisplayWidth());
            this.setDisplayHeight(displayChtc.getDisplayHeight());
            this.setGlowColorOverride(displayChtc.getGlowColorOverride());
            this.setShadowRadius(displayChtc.getShadowRadius());
            this.setShadowStrength(displayChtc.getShadowStrength());
            this.setViewRange(displayChtc.getViewRange());
            this.setInterpolationDelay(displayChtc.getInterpolationDelay());
            this.setInterpolationDuration(displayChtc.getInterpolationDuration());
        }
    }

    @Override
    public Display.Billboard getBillboard() {
        Display.Billboard billboard;
        String strBillboard = this.getString(NbtDisplayPropertyNames.BILLBOARD);

        if (strBillboard == null) {
            return null;
        }

        try {
            billboard = Display.Billboard.valueOf(strBillboard);
        } catch (IllegalArgumentException e) {
            return null;
        }

        return billboard;
    }

    @Override
    public void setBillboard(Display.Billboard billboard) {
        if (billboard == null) {
            this.remove(NbtDisplayPropertyNames.BILLBOARD);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.BILLBOARD,
                billboard.toString().toLowerCase()
        );
    }

    @Override
    public Display.Brightness getBrightness() {
        Display.Brightness billboard;
        CompoundBinaryTag cpdBillboard = this.getCompound(NbtDisplayPropertyNames.BILLBOARD);

        if (cpdBillboard == null) {
            return null;
        }

        try {
            billboard = new Display.Brightness(
                    cpdBillboard.getInt(NbtDisplayPropertyNames.BLOCK_BRIGHTNESS, 10),
                    cpdBillboard.getInt(NbtDisplayPropertyNames.SKY_BRIGHTNESS, 10)
            );
        } catch (IllegalArgumentException e) {
            return null;
        }

        return billboard;
    }

    @Override
    public void setBrightness(Display.Brightness brightness) {
        if (brightness == null) {
            this.remove(NbtDisplayPropertyNames.BRIGHTNESS);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.BRIGHTNESS,

                CompoundBinaryTag.builder()
                        .put(
                                NbtDisplayPropertyNames.SKY_BRIGHTNESS,
                                AdventureTagBuilder.intTag(brightness.getSkyLight()))

                        .put(
                                NbtDisplayPropertyNames.BLOCK_BRIGHTNESS,
                                AdventureTagBuilder.intTag(brightness.getBlockLight()))
                        .build()
        );
    }

    @Override
    public Transformation getTransformation() {
        Transformation transformation = null;
        BinaryTag binaryTransformation = this.get(NbtDisplayPropertyNames.TRANSFORMATION);

        if (binaryTransformation instanceof CompoundBinaryTag transformationCbt) {
            try {
                transformation = new Transformation(
                        this.getTransformationVector3f(NbtDisplayPropertyNames.TRANSLATION, transformationCbt),
                        this.getTransformationQuaternionf(NbtDisplayPropertyNames.LEFT_ROTATION, transformationCbt),
                        this.getTransformationVector3f(NbtDisplayPropertyNames.SCALE, transformationCbt),
                        this.getTransformationQuaternionf(NbtDisplayPropertyNames.RIGHT_ROTATION, transformationCbt)
                );
            } catch (NullPointerException e) {
                return null;
            }
        } else if (binaryTransformation instanceof ListBinaryTag transformationList) {
            if (transformationList.size() != 16) {
                return null;
            }

            float[] transformationArray = new float[transformationList.size()];
            int i = 0;
            for (BinaryTag binaryTag : transformationList) {
                if (binaryTag instanceof FloatBinaryTag floatBinaryTag) {
                    transformationArray[i] = floatBinaryTag.value();
                    i++;
                }
            }

            transformation = MathUtil.createTransformation(transformationArray);
        }


        return transformation;
    }

    @Override
    public void setTransformation(Transformation transformation) {
        this.setTransformation(transformation, true);
    }

    private Vector3f getTransformationVector3f(String name, CompoundBinaryTag cpdTransformation) {
        ListBinaryTag vector3fList = cpdTransformation.getList(name);

        if (vector3fList.size() != 3) {
            return null;
        }

        return new Vector3f(
                vector3fList.getFloat(0),
                vector3fList.getFloat(1),
                vector3fList.getFloat(2)
        );
    }

    private Quaternionf getTransformationQuaternionf(String name, CompoundBinaryTag cpdTransformation) {
        Quaternionf quaternionf = null;
        BinaryTag binaryVector3f = cpdTransformation.get(name);

        if (binaryVector3f instanceof CompoundBinaryTag quaternionfCbt) {
            float angle = quaternionfCbt.getFloat(NbtDisplayPropertyNames.ANGLE);
            ListBinaryTag axisList = quaternionfCbt.getList(NbtDisplayPropertyNames.AXIS);

            if (axisList.size() != 3) {
                return null;
            }

            float angleRadians = (float) Math.toRadians(angle);

            Vector3f rotationAxis = new Vector3f(
                    axisList.getFloat(0),
                    axisList.getFloat(1),
                    axisList.getFloat(2)
            );

            quaternionf = new Quaternionf().fromAxisAngleRad(rotationAxis, angleRadians);

        } else if (binaryVector3f instanceof ListBinaryTag quaternionfList) {

            if (quaternionfList.size() != 4) {
                return null;
            }

            quaternionf = new Quaternionf(
                    quaternionfList.getFloat(0),
                    quaternionfList.getFloat(1),
                    quaternionfList.getFloat(2),
                    quaternionfList.getFloat(3)
            );
        }

        return quaternionf;
    }

    public void setTransformation(Transformation transformation, boolean compound) {
        if (transformation == null) {
            this.remove(NbtDisplayPropertyNames.TRANSFORMATION);
            return;
        }

        if (compound) {
            this.setTransformationAsCompound(transformation);
        } else {
            this.setTransformationAsMatrix(transformation);
        }
    }

    private void setTransformationAsMatrix(Transformation transformation) {
        float[] transformationMatrix = MathUtil.toMatrix(transformation);
        Float[] complexTransformationMatrix = new Float[transformationMatrix.length];

        for (int i = 0; i < transformationMatrix.length; i++) {
            complexTransformationMatrix[i] = transformationMatrix[i];
        }

        this.put(
                NbtDisplayPropertyNames.TRANSFORMATION,

                AdventureTagBuilder.floatList(complexTransformationMatrix).build()
        );
    }

    private void setTransformationAsCompound(Transformation transformation) {
        this.put(
                NbtDisplayPropertyNames.TRANSFORMATION,

                CompoundBinaryTag.builder()
                        .put(
                                NbtDisplayPropertyNames.LEFT_ROTATION,

                                AdventureTagBuilder.floatList(
                                        MathUtil.toArray(transformation.getLeftRotation())
                                ).build()
                        )

                        .put(
                                NbtDisplayPropertyNames.RIGHT_ROTATION,

                                AdventureTagBuilder.floatList(
                                        MathUtil.toArray(transformation.getRightRotation())
                                ).build()
                        )

                        .put(
                                NbtDisplayPropertyNames.TRANSLATION,

                                AdventureTagBuilder.floatList(
                                        MathUtil.toArray(transformation.getTranslation())
                                ).build()
                        )

                        .put(
                                NbtDisplayPropertyNames.SCALE,

                                AdventureTagBuilder.floatList(
                                        MathUtil.toArray(transformation.getScale())
                                ).build()
                        )
                        .build()
        );
    }

    @Override
    public Color getGlowColorOverride() {
        int decimalColor = this.getInt(NbtDisplayPropertyNames.GLOW_COLOR_OVERRIDE, 16777215);

        return ColorConverter.fromDecimalABGR(decimalColor);
    }

    @Override
    public void setGlowColorOverride(Color glowColorOverride) {
        if (glowColorOverride == null) {
            this.remove(NbtDisplayPropertyNames.GLOW_COLOR_OVERRIDE);
            return;
        }


        int decimalColor;

        decimalColor = ColorConverter.toOppositeDecimalABGR(glowColorOverride);

        this.put(
                NbtDisplayPropertyNames.GLOW_COLOR_OVERRIDE,

                decimalColor
        );
    }

    @Override
    public Float getDisplayHeight() {
        return this.getFloat(NbtDisplayPropertyNames.HEIGHT);
    }

    @Override
    public void setDisplayHeight(Float displayHeight) {
        if (displayHeight == null) {
            this.remove(NbtDisplayPropertyNames.HEIGHT);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.HEIGHT,

                displayHeight
        );
    }

    @Override
    public Float getDisplayWidth() {
        return this.getFloat(NbtDisplayPropertyNames.WIDTH);
    }

    @Override
    public void setDisplayWidth(Float displayWidth) {
        if (displayWidth == null) {
            this.remove(NbtDisplayPropertyNames.WIDTH);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.WIDTH,

                displayWidth
        );
    }

    @Override
    public Float getShadowRadius() {
        return this.getFloat(NbtDisplayPropertyNames.SHADOW_RADIUS);
    }

    @Override
    public void setShadowRadius(Float shadowRadius) {
        if (shadowRadius == null) {
            this.remove(NbtDisplayPropertyNames.SHADOW_RADIUS);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.SHADOW_RADIUS,

                shadowRadius
        );
    }

    @Override
    public Float getShadowStrength() {
        return this.getFloat(NbtDisplayPropertyNames.SHADOW_STRENGTH);
    }

    @Override
    public void setShadowStrength(Float shadowStrength) {
        if (shadowStrength == null) {
            this.remove(NbtDisplayPropertyNames.SHADOW_STRENGTH);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.SHADOW_STRENGTH,

                shadowStrength
        );
    }

    @Override
    public Float getViewRange() {
        return this.getFloat(NbtDisplayPropertyNames.VIEW_RANGE, 1f);
    }

    @Override
    public void setViewRange(Float viewRange) {
        if (viewRange == null) {
            this.remove(NbtDisplayPropertyNames.VIEW_RANGE);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.VIEW_RANGE,

                viewRange
        );
    }

    @Override
    public Integer getInterpolationDelay() {
        return this.getInt(NbtDisplayPropertyNames.START_INTERPOLATION, -1);
    }

    @Override
    public void setInterpolationDelay(Integer interpolationDelay) {
        if (interpolationDelay == null) {
            this.remove(NbtDisplayPropertyNames.START_INTERPOLATION);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.START_INTERPOLATION,

                interpolationDelay
        );
    }

    @Override
    public Integer getInterpolationDuration() {
        return this.getInt(NbtDisplayPropertyNames.INTERPOLATION_DURATION);
    }

    @Override
    public void setInterpolationDuration(Integer interpolationDuration) {
        if (interpolationDuration == null) {
            this.remove(NbtDisplayPropertyNames.INTERPOLATION_DURATION);
            return;
        }

        this.put(
                NbtDisplayPropertyNames.INTERPOLATION_DURATION,

                interpolationDuration
        );
    }
}
