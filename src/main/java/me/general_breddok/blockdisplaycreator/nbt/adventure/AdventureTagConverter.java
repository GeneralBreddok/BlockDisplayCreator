package me.general_breddok.blockdisplaycreator.nbt.adventure;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.nbt.NbtObject;
import net.kyori.adventure.nbt.*;

import java.io.IOException;

@UtilityClass
public class AdventureTagConverter {
    public BinaryTag toBinaryTag(Object object) {
        if (object instanceof BinaryTag binaryTag) {
            return binaryTag;
        } else if (object instanceof Boolean bool) {
            return bool.equals(Boolean.TRUE) ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO;
        } else if (object instanceof Byte bt) {
            return ByteBinaryTag.byteBinaryTag(bt);
        } else if (object instanceof Short sh) {
            return ShortBinaryTag.shortBinaryTag(sh);
        } else if (object instanceof Integer integer) {
            return IntBinaryTag.intBinaryTag(integer);
        } else if (object instanceof Long longInteger) {
            return LongBinaryTag.longBinaryTag(longInteger);
        } else if (object instanceof String str) {
            return StringBinaryTag.stringBinaryTag(str);
        } else if (object instanceof Float fl) {
            return FloatBinaryTag.floatBinaryTag(fl);
        } else if (object instanceof Double db) {
            return DoubleBinaryTag.doubleBinaryTag(db);
        } else if (object instanceof byte[] byteArray) {
            return ByteArrayBinaryTag.byteArrayBinaryTag(byteArray);
        } else if (object instanceof int[] intArray) {
            return IntArrayBinaryTag.intArrayBinaryTag(intArray);
        } else if (object instanceof long[] longArray) {
            return LongArrayBinaryTag.longArrayBinaryTag(longArray);
        }
        return null;
    }

    public Object fromBinaryTag(BinaryTag binaryTag) {
        if (binaryTag instanceof ByteBinaryTag byteTag) {
            return byteTag.value();
        } else if (binaryTag instanceof ShortBinaryTag shortTag) {
            return shortTag.value();
        } else if (binaryTag instanceof IntBinaryTag intTag) {
            return intTag.value();
        } else if (binaryTag instanceof LongBinaryTag longTag) {
            return longTag.value();
        } else if (binaryTag instanceof FloatBinaryTag floatTag) {
            return floatTag.value();
        } else if (binaryTag instanceof DoubleBinaryTag doubleTag) {
            return doubleTag.value();
        } else if (binaryTag instanceof StringBinaryTag stringTag) {
            return stringTag.value();
        } else if (binaryTag instanceof ByteArrayBinaryTag byteArrayTag) {
            return byteArrayTag.value();
        } else if (binaryTag instanceof IntArrayBinaryTag intArrayTag) {
            return intArrayTag.value();
        } else if (binaryTag instanceof LongArrayBinaryTag longArrayTag) {
            return longArrayTag.value();
        } else if (binaryTag instanceof CompoundBinaryTag compoundTag) {
            return new NbtObject(compoundTag);
        }
        return null;
    }

    public String safeToString(CompoundBinaryTag compoundBinaryTag) {
        try {
            return TagStringIO.get().asString(compoundBinaryTag);
        } catch (IOException ignore) {
        }
        return "";
    }
}
