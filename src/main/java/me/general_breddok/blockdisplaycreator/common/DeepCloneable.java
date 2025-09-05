package me.general_breddok.blockdisplaycreator.common;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Represents a contract for objects that support deep cloning.
 * <p>
 * Implementations of this interface are expected to provide a proper {@link #clone()} method
 * that creates a deep copy of the object, including its nested mutable structures.
 * </p>
 *
 * @param <T> the type of the object implementing this interface
 */
public interface DeepCloneable<T extends DeepCloneable<T>> extends Cloneable {

    /**
     * Attempts to clone the given object if it implements {@link DeepCloneable}.
     * <p>
     * If the object is {@code null} or not cloneable, the original reference is returned.
     * </p>
     *
     * @param object the object to clone
     * @param <T>    the type of the object
     * @return cloned object or the same instance if cloning is not applicable
     */
    static <T> T tryClone(T object) {
        if (object == null) {
            return null;
        }

        if (!(object instanceof DeepCloneable<?> deepCloneable)) {
            return object;
        }

        try {
            return (T) deepCloneable.clone();
        } catch (ClassCastException e) {
            return object;
        }
    }

    // ---------- Static utility methods ----------

    /**
     * Attempts to clone each element of the given list.
     * <p>
     * Elements that do not implement {@link DeepCloneable} will be kept as-is.
     * </p>
     *
     * @param collection the list of objects
     * @param <T>        the element type
     * @return a new list with cloned elements where possible
     */
    static <T> List<T> tryCloneList(List<T> collection) {
        return tryCloneCollection(collection, Collectors.toList());
    }

    /**
     * Attempts to clone each element of the given collection.
     * <p>
     * Elements that do not implement {@link DeepCloneable} will be kept as-is.
     * </p>
     *
     * @param collection the collection of objects
     * @param collector  the collector to define the resulting collection type
     * @param <T>        the element type
     * @param <C>        the collection type
     * @return a new collection with cloned elements where possible
     */
    static <T, C extends Collection<T>> C tryCloneCollection(C collection, Collector<T, ?, C> collector) {
        return collection.stream().map(DeepCloneable::tryClone).collect(collector);
    }

    /**
     * Creates a deep clone of the current object.
     *
     * @return a new instance that is a deep copy of this object
     */
    T clone();
}