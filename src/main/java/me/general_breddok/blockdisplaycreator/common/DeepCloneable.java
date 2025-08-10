package me.general_breddok.blockdisplaycreator.common;

import me.general_breddok.blockdisplaycreator.util.ChatUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface DeepCloneable<T extends DeepCloneable<T>> extends Cloneable {
    T clone();

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

    static <T> List<T> tryCloneList(List<T> collection) {
        return tryCloneCollection(collection, Collectors.toList());
    }

    static <T, C extends Collection<T>> C tryCloneCollection(C collection, Collector<T, ?, C> collector) {
        return collection.stream().map(DeepCloneable::tryClone).collect(collector);
    }
}
