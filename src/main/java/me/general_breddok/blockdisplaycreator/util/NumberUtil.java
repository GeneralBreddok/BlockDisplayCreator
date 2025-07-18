package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class NumberUtil {
    public <T extends Number> T convertToType(Number number, Class<T> targetType) {
        if (targetType == Integer.class) {
            return targetType.cast(number.intValue());
        } else if (targetType == Long.class) {
            return targetType.cast(number.longValue());
        } else if (targetType == Byte.class) {
            return targetType.cast(number.byteValue());
        } else if (targetType == Double.class) {
            return targetType.cast(number.doubleValue());
        } else if (targetType == Float.class) {
            return targetType.cast(number.floatValue());
        } else if (targetType == Short.class) {
            return targetType.cast(number.shortValue());
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + targetType.getName());
        }
    }

    public boolean isNumberClass(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }

    public boolean isNumberList(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return clazz.getTypeParameters().length == 1 && isNumberClass(clazz.getTypeParameters()[0].getClass());
        }
        return false;
    }

    public boolean isIntegerType(Class<? extends Number> clazz) {
        return clazz == Integer.class || clazz == Long.class || clazz == Byte.class || clazz == Short.class;
    }

    public boolean isFloatingPointType(Class<? extends Number> clazz) {
        return clazz == Double.class || clazz == Float.class;
    }

    public boolean isDigitString(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    public static Number parseNumber(String input) throws NumberFormatException {
        input = input.trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e1) {
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e2) {
                return Double.parseDouble(input);
            }
        }
    }
}
