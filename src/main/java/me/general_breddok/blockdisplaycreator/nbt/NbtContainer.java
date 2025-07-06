package me.general_breddok.blockdisplaycreator.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface NbtContainer {
    void put(@NotNull String key, @NotNull BinaryTag value);

    void put(@NotNull String key, @NotNull String value);

    void put(@NotNull CompoundBinaryTag value);

    void put(@NotNull String key, int value);

    void put(@NotNull String key, byte value);

    void put(@NotNull String key, short value);

    void put(@NotNull String key, long value);

    void put(@NotNull String key, double value);

    void put(@NotNull String key, float value);

    void put(@NotNull String key, int[] value);

    void put(@NotNull String key, long[] value);

    void put(@NotNull String key, byte[] value);

    void put(@NotNull String key, boolean value);

    void put(@NotNull String key, @NotNull ListBinaryTag listValue);

    @Nullable
    BinaryTag get(@NotNull String key);

    @Nullable
    BinaryTag get(@NotNull String key, @NotNull BinaryTag def);

    @Nullable
    CompoundBinaryTag getCompound(@NotNull String key);

    @Nullable
    CompoundBinaryTag getCompound(@NotNull String key, @Nullable CompoundBinaryTag def);

    ListBinaryTag getList(@NotNull String key);

    ListBinaryTag getList(@NotNull String key, ListBinaryTag def);

    ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType);

    ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType, ListBinaryTag def);

    @Nullable
    String getString(@NotNull String key);

    @Nullable
    String getString(@NotNull String key, String def);

    int getInt(@NotNull String key);

    int getInt(@NotNull String key, int def);

    double getDouble(@NotNull String key);

    double getDouble(@NotNull String key, double def);

    float getFloat(@NotNull String key);

    float getFloat(@NotNull String key, float def);

    long getLong(@NotNull String key);

    long getLong(@NotNull String key, long def);

    short getShort(@NotNull String key);

    short getShort(@NotNull String key, short def);

    byte getByte(@NotNull String key);

    byte getByte(@NotNull String key, byte def);

    boolean getBoolean(@NotNull String key);

    boolean getBoolean(@NotNull String key, boolean def);

    byte[] getByteArray(@NotNull String key);

    byte[] getByteArray(@NotNull String key, byte[] def);

    int[] getIntArray(@NotNull String key);

    int[] getIntArray(@NotNull String key, int[] def);

    long[] getLongArray(@NotNull String key);

    long[] getLongArray(@NotNull String key, long[] def);

    short[] getShortArray(@NotNull String key);

    short[] getShortArray(@NotNull String key, short[] def);

    double[] getDoubleArray(@NotNull String key);

    double[] getDoubleArray(@NotNull String key, double[] def);

    float[] getFloatArray(@NotNull String key);

    float[] getFloatArray(@NotNull String key, float[] def);

    void remove(@NotNull String key);

    boolean contains(@NotNull String key);

    Set<String> keySet();

    CompoundBinaryTag asCompound();

    @Override
    String toString();
}
