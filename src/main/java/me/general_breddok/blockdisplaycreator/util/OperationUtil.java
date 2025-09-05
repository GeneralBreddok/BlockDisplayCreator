package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utility class providing common operations for conditional execution, null checks,
 * and collection handling frequently used throughout the code.
 */
@UtilityClass
public class OperationUtil {

    /**
     * Returns a Collector that accumulates elements into an ArrayList.
     *
     * @param <T> the type of input elements
     * @return a Collector that produces an ArrayList containing the input elements
     */
    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return Collectors.toCollection(ArrayList::new);
    }

    /**
     * Executes the given action if the object is not null.
     *
     * @param object the object to check
     * @param action the action to execute if object is not null
     * @param <T>    the type of the object
     */
    public <T> void doIfNotNull(T object, Consumer<T> action) {
        doIf(object, Objects::nonNull, action);
    }

    /**
     * Executes the given Runnable if the object is not null.
     *
     * @param object the object to check
     * @param action the action to execute if object is not null
     * @param <T>    the type of the object
     */
    public <T> void doIfNotNull(T object, Runnable action) {
        doIf(object, Objects::nonNull, action);
    }

    /**
     * Executes the given action if the object satisfies the specified condition.
     *
     * @param object    the object to check
     * @param condition the predicate to test the object
     * @param action    the action to execute if condition is true
     * @param <T>       the type of the object
     */
    public <T> void doIf(T object, Predicate<T> condition, Consumer<T> action) {
        if (condition.test(object)) {
            action.accept(object);
        }
    }

    /**
     * Executes the given Runnable if the object satisfies the specified condition.
     *
     * @param object    the object to check
     * @param condition the predicate to test the object
     * @param action    the action to execute if condition is true
     * @param <T>       the type of the object
     */
    public <T> void doIf(T object, Predicate<T> condition, Runnable action) {
        if (condition.test(object)) {
            action.run();
        }
    }

    /**
     * Checks if the compared object is equal to at least one of the provided objects.
     *
     * @param comparedObject the object to compare
     * @param objects        array of objects to compare against
     * @return true if comparedObject equals any object in the array
     */
    public boolean orEquals(Object comparedObject, Object... objects) {
        for (Object object : objects) {
            if (comparedObject.equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the compared object is equal to all of the provided objects.
     *
     * @param comparedObject the object to compare
     * @param objects        array of objects to compare against
     * @return true if comparedObject equals all objects in the array
     */
    public boolean andEquals(Object comparedObject, Object... objects) {
        for (Object object : objects) {
            if (!comparedObject.equals(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the first non-null element from the provided objects.
     *
     * @param objects the objects to check
     * @param <T>     the type of the objects
     * @return the first non-null object or null if all are null
     */
    @SafeVarargs
    public <T> T firstNonNull(T... objects) {
        for (T object : objects) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    /**
     * Checks if all provided objects are null.
     *
     * @param objects the objects to check
     * @return true if all objects are null
     */
    @SafeVarargs
    public boolean isAllNull(Object... objects) {
        return Arrays.stream(objects).allMatch(Objects::isNull);
    }
}
