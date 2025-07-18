package me.general_breddok.blockdisplaycreator.nbt.adventure;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import net.kyori.adventure.nbt.*;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class AdventureTagBuilder {

    public <T extends BinaryTag> ListBinaryTag.Builder<T> list(BinaryTagType<T> type, List<T> tags) {
        return ListBinaryTag
                .builder(type)
                .add(tags);
    }

    public ListBinaryTag.Builder<LongBinaryTag> longList(Long... tags) {
        return list(
                BinaryTagTypes.LONG,
                Arrays.stream(tags).map(LongBinaryTag::longBinaryTag).collect(OperationUtil.toArrayList())
        );
    }

    public ListBinaryTag.Builder<IntBinaryTag> intList(Integer... tags) {
        return list(
                BinaryTagTypes.INT,
                Arrays.stream(tags).map(IntBinaryTag::intBinaryTag).toList()
        );
    }

    public ListBinaryTag.Builder<ShortBinaryTag> shortList(Short... tags) {
        return list(
                BinaryTagTypes.SHORT,
                Arrays.stream(tags).map(ShortBinaryTag::shortBinaryTag).toList()
        );
    }

    public ListBinaryTag.Builder<ByteBinaryTag> byteList(Byte... tags) {
        return list(
                BinaryTagTypes.BYTE,
                Arrays.stream(tags).map(ByteBinaryTag::byteBinaryTag).toList()
        );
    }

    public ListBinaryTag.Builder<DoubleBinaryTag> doubleList(Double... tags) {
        return list(
                BinaryTagTypes.DOUBLE,
                Arrays.stream(tags).map(DoubleBinaryTag::doubleBinaryTag).toList()
        );
    }

    public ListBinaryTag.Builder<FloatBinaryTag> floatList(Float... tags) {
        return list(
                BinaryTagTypes.FLOAT,
                Arrays.stream(tags).map(FloatBinaryTag::floatBinaryTag).toList()
        );
    }

    public LongBinaryTag longTag(long l) {
        return LongBinaryTag.longBinaryTag(l);
    }

    public IntBinaryTag intTag(int i) {
        return IntBinaryTag.intBinaryTag(i);
    }

    public ShortBinaryTag shortTag(short s) {
        return ShortBinaryTag.shortBinaryTag(s);
    }

    public ByteBinaryTag byteTag(byte b) {
        return ByteBinaryTag.byteBinaryTag(b);
    }

    public DoubleBinaryTag doubleTag(double d) {
        return DoubleBinaryTag.doubleBinaryTag(d);
    }

    public FloatBinaryTag floatTag(float f) {
        return FloatBinaryTag.floatBinaryTag(f);
    }
}
