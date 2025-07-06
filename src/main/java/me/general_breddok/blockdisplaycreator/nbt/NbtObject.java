package me.general_breddok.blockdisplaycreator.nbt;

import me.general_breddok.blockdisplaycreator.annotation.EmptyCollection;
import me.general_breddok.blockdisplaycreator.nbt.adventure.AdventureTagConverter;
import me.general_breddok.blockdisplaycreator.nbt.exception.NbtParseException;
import net.kyori.adventure.nbt.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * The {@code SNBTObject} class is designed to handle SNBT (Stringified Named Binary Tag) data with ease.
 * This class provides various methods for manipulating and retrieving SNBT data.
 * Unlike the {@code CompoundBinaryTag} class, {@code SNBTObject} is mutable.
 */

public class NbtObject implements NbtContainer {
    protected CompoundBinaryTag compoundBinaryTag;

    /**
     * Constructs an {@code SNBTObject} with the given {@code CompoundBinaryTag}.
     *
     * @param compoundBinaryTag a non-null {@code CompoundBinaryTag} instance.
     */
    public NbtObject(@NotNull CompoundBinaryTag compoundBinaryTag) {
        this.compoundBinaryTag = compoundBinaryTag;
    }

    /**
     * Constructs an {@code SNBTObject} by parsing the given SNBT string.
     *
     * @param snbt a non-null SNBT string.
     * @throws IOException if an error occurs while parsing the SNBT string.
     */
    public NbtObject(@NotNull String snbt) throws NbtParseException {
        try {
            this.compoundBinaryTag = TagStringIO.get().asCompound(snbt);
        } catch (IOException e) {
            throw new NbtParseException(e);
        }
    }

    /**
     * Inserts a {@code BinaryTag} at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the {@code BinaryTag} value to insert.
     */
    public void put(@NotNull String key, @NotNull BinaryTag value) {
        this.compoundBinaryTag = this.compoundBinaryTag.put(key, value);
    }

    /**
     * Inserts a string at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the string value to insert.
     */
    public void put(@NotNull String key, @NotNull String value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putString(key, value);
    }

    /**
     * Inserts all tags from the given {@code CompoundBinaryTag}.
     *
     * @param value the {@code CompoundBinaryTag} whose tags to insert.
     */
    public void put(@NotNull CompoundBinaryTag value) {
        this.compoundBinaryTag = this.compoundBinaryTag.put(value);
    }

    /**
     * Inserts an integer at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the integer value to insert.
     */
    public void put(@NotNull String key, int value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putInt(key, value);
    }

    /**
     * Inserts a byte at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the byte value to insert.
     */
    public void put(@NotNull String key, byte value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putByte(key, value);
    }

    /**
     * Inserts a short at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the short value to insert.
     */
    public void put(@NotNull String key, short value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putShort(key, value);
    }

    /**
     * Inserts a long at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the long value to insert.
     */
    public void put(@NotNull String key, long value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putLong(key, value);
    }

    /**
     * Inserts a double at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the double value to insert.
     */
    public void put(@NotNull String key, double value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putDouble(key, value);
    }

    /**
     * Inserts a float at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the float value to insert.
     */
    public void put(@NotNull String key, float value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putFloat(key, value);
    }

    /**
     * Inserts an integer array at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the integer array to insert.
     */
    public void put(@NotNull String key, int[] value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putIntArray(key, value);
    }

    /**
     * Inserts a long array at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the long array to insert.
     */
    public void put(@NotNull String key, long[] value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putLongArray(key, value);
    }

    /**
     * Inserts a byte array at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the byte array to insert.
     */
    public void put(@NotNull String key, byte[] value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putByteArray(key, value);
    }

    /**
     * Inserts a boolean at the specified key.
     *
     * @param key   the key at which to insert the value.
     * @param value the boolean value to insert.
     */
    public void put(@NotNull String key, boolean value) {
        this.compoundBinaryTag = this.compoundBinaryTag.putBoolean(key, value);
    }

    /**
     * Inserts a list of {@code BinaryTag} at the specified key.
     *
     * @param key       the key at which to insert the value.
     * @param listValue the list of {@code BinaryTag} to insert.
     */
    public void put(@NotNull String key, @NotNull ListBinaryTag listValue) {
        this.compoundBinaryTag = compoundBinaryTag.put(key, listValue);
    }

    /**
     * Retrieves the {@code BinaryTag} at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the {@code BinaryTag} at the specified key, or {@code null} if no value is found.
     */
    @Nullable
    public BinaryTag get(@NotNull String key) {
        return this.compoundBinaryTag.get(key);
    }

    @Override
    public @Nullable BinaryTag get(@NotNull String key, @NotNull BinaryTag def) {
        BinaryTag result = get(key);
        return result == null ? def : result;
    }

    /**
     * Retrieves the {@code CompoundBinaryTag} at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the {@code CompoundBinaryTag} at the specified key, or {@code null} if no value is found.
     */
    @Nullable
    public CompoundBinaryTag getCompound(@NotNull String key, @Nullable CompoundBinaryTag def) {
        BinaryTag binaryTag = get(key);

        if (binaryTag == null) {
            return def;
        }

        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            return def;
        }

        return compound;
    }

    @Nullable
    public CompoundBinaryTag getCompound(@NotNull String key) {
        return getCompound(key, null);
    }

    /**
     * Retrieves the list of {@code BinaryTag} at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the list of {@code BinaryTag} at the specified key.
     */
    @Override
    @EmptyCollection
    public ListBinaryTag getList(@NotNull String key) {
        return this.compoundBinaryTag.getList(key);
    }

    @Override
    public ListBinaryTag getList(@NotNull String key, ListBinaryTag def) {
        return this.compoundBinaryTag.getList(key, def);
    }

    @Override
    public ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType) {
        return this.compoundBinaryTag.getList(key, expectedType);
    }

    @Override
    public ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType, ListBinaryTag def) {
        return this.compoundBinaryTag.getList(key, expectedType, def);
    }

    /**
     * Retrieves the string at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the string at the specified key, or {@code null} if no value is found.
     */
    @Nullable
    public String getString(@NotNull String key) {
        return getString(key, null);
    }

    @Override
    @Nullable
    public String getString(@NotNull String key, String def) {
        BinaryTag result = get(key);

        if (result == null) {
            return def;
        }

        if (!(result instanceof StringBinaryTag s)) {
            return def;
        }

        return s.value();
    }

    /**
     * Retrieves the integer at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the integer at the specified key.
     */
    public int getInt(@NotNull String key) {
        return this.compoundBinaryTag.getInt(key);
    }

    @Override
    public int getInt(@NotNull String key, int def) {
        return this.compoundBinaryTag.getInt(key, def);
    }

    /**
     * Retrieves the double at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the double at the specified key.
     */
    public double getDouble(@NotNull String key) {
        return this.compoundBinaryTag.getDouble(key);
    }

    @Override
    public double getDouble(@NotNull String key, double def) {
        return this.compoundBinaryTag.getDouble(key, def);
    }

    /**
     * Retrieves the float at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the float at the specified key.
     */
    public float getFloat(@NotNull String key) {
        return this.compoundBinaryTag.getFloat(key);
    }

    @Override
    public float getFloat(@NotNull String key, float def) {
        return this.compoundBinaryTag.getFloat(key, def);
    }

    /**
     * Retrieves the byte at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the byte at the specified key.
     */
    public byte getByte(@NotNull String key) {
        return this.compoundBinaryTag.getByte(key);
    }

    @Override
    public byte getByte(@NotNull String key, byte def) {
        return this.compoundBinaryTag.getByte(key, def);
    }

    /**
     * Retrieves the short at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the short at the specified key.
     */
    public short getShort(@NotNull String key) {
        return this.compoundBinaryTag.getShort(key);
    }

    @Override
    public short getShort(@NotNull String key, short def) {
        return this.compoundBinaryTag.getShort(key, def);
    }

    /**
     * Retrieves the long at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the long at the specified key.
     */
    public long getLong(@NotNull String key) {
        return this.compoundBinaryTag.getLong(key);
    }

    @Override
    public long getLong(@NotNull String key, long def) {
        return this.compoundBinaryTag.getLong(key, def);
    }

    /**
     * Retrieves the integer array at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the integer array at the specified key.
     */
    public int[] getIntArray(@NotNull String key) {
        return this.compoundBinaryTag.getIntArray(key);
    }

    @Override
    public int[] getIntArray(@NotNull String key, int[] def) {
        return this.compoundBinaryTag.getIntArray(key, def);
    }

    /**
     * Retrieves the long array at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the long array at the specified key.
     */
    public long[] getLongArray(@NotNull String key) {
        return this.compoundBinaryTag.getLongArray(key);
    }

    @Override
    public long[] getLongArray(@NotNull String key, long[] def) {
        return this.compoundBinaryTag.getLongArray(key, def);
    }

    @Override
    public short[] getShortArray(@NotNull String key) {
        return getShortArray(key, new short[0]);
    }

    @Override
    public short[] getShortArray(@NotNull String key, short[] def) {
        ListBinaryTag list = getList(key);
        short[] result = new short[list.size()];
        int i = 0;
        for (BinaryTag binaryTag : list) {
            if (binaryTag instanceof ShortBinaryTag shortBinaryTag) {
                result[i] = shortBinaryTag.value();
                i++;
            }
        }
        return result.length != 0 ? result : def;
    }

    @Override
    public double[] getDoubleArray(@NotNull String key) {
        return getDoubleArray(key, new double[0]);
    }

    @Override
    public double[] getDoubleArray(@NotNull String key, double[] def) {
        ListBinaryTag list = getList(key);
        double[] result = new double[list.size()];
        int i = 0;
        for (BinaryTag binaryTag : list) {
            if (binaryTag instanceof DoubleBinaryTag doubleBinaryTag) {
                result[i] = doubleBinaryTag.value();
                i++;
            }
        }
        return result.length != 0 ? result : def;
    }

    @Override
    public float[] getFloatArray(@NotNull String key) {
        return this.getFloatArray(key, new float[0]);
    }

    @Override
    public float[] getFloatArray(@NotNull String key, float[] def) {
        ListBinaryTag list = this.getList(key);
        float[] result = new float[list.size()];
        int i = 0;
        for (BinaryTag binaryTag : list) {
            if (binaryTag instanceof FloatBinaryTag floatBinaryTag) {
                result[i] = floatBinaryTag.value();
                i++;
            }
        }
        return result.length != 0 ? result : def;
    }

    /**
     * Retrieves the byte array at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the byte array at the specified key.
     */
    public byte[] getByteArray(@NotNull String key) {
        return this.compoundBinaryTag.getByteArray(key);
    }

    @Override
    public byte[] getByteArray(@NotNull String key, byte[] def) {
        return this.compoundBinaryTag.getByteArray(key, def);
    }

    /**
     * Retrieves the boolean at the specified key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the boolean at the specified key.
     */
    public boolean getBoolean(@NotNull String key) {
        return this.compoundBinaryTag.getBoolean(key);
    }

    @Override
    public boolean getBoolean(@NotNull String key, boolean def) {
        return false;
    }

    /**
     * Removes the entry at the specified key.
     *
     * @param key the key whose associated entry is to be removed.
     */
    public void remove(@NotNull String key) {
        this.compoundBinaryTag = this.compoundBinaryTag.remove(key);
    }

    /**
     * Checks if the {@code SNBTObject} contains an entry at the specified key.
     *
     * @param key the key whose presence is to be tested.
     * @return {@code true} if the {@code SNBTObject} contains an entry at the specified key, otherwise {@code false}.
     */
    public boolean contains(@NotNull String key) {
        return this.compoundBinaryTag.get(key) != null;
    }

    /**
     * Returns the string representation of the {@code SNBTObject}.
     *
     * @return the string representation of the {@code SNBTObject}.
     */
    @Override
    public String toString() {
        try {
            return TagStringIO.get().asString(compoundBinaryTag);
        } catch (IOException ignore) {
        }
        return "";
    }

    public Set<String> keySet() {
        return compoundBinaryTag.keySet();
    }

    public CompoundBinaryTag asCompound() {
        return compoundBinaryTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NbtContainer nbtContainer) {
            return this.toString().equals(nbtContainer.toString());
        } else if (obj instanceof CompoundBinaryTag compound) {
            return this.toString().equals(AdventureTagConverter.safeToString(compound));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compoundBinaryTag);
    }
}

