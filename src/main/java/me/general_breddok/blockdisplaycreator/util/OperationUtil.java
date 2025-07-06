package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

@UtilityClass
public class OperationUtil {
    public <T> void doIfNotNull(T object, Consumer<T> action) {
        doIf(object, Objects::nonNull, action);
    }

    public <T> void doIfNotNull(T object, Runnable action) {
        doIf(object, Objects::nonNull, action);
    }

    public <T> void doIf(T object, Predicate<T> condition, Consumer<T> action) {
        if (condition.test(object)) {
            action.accept(object);
        }
    }

    public <T> void doIf(T object, Predicate<T> condition, Runnable action) {
        if (condition.test(object)) {
            action.run();
        }
    }

    public boolean orEquals(Object comparedObject, Object... objects) {
        for (Object object : objects) {
            if (comparedObject.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public boolean andEquals(Object comparedObject, Object... objects) {
        for (Object object : objects) {
            if (!comparedObject.equals(object)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public <T> T firstNonNull(T... objects) {
        for (T object : objects) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return Collector.of(
                ArrayList::new,
                ArrayList::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public <T> List<T> toArray(T[] array) {
        return Arrays.stream(array).toList();
    }

    public boolean nullSafeEquals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    @SafeVarargs
    public boolean isAllNull(Object... objects) {
        return Arrays.stream(objects).allMatch(Objects::isNull);
    }
}
