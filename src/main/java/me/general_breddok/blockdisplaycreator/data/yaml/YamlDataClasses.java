package me.general_breddok.blockdisplaycreator.data.yaml;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class YamlDataClasses {
    public final Class<MemorySection> SECTION = MemorySection.class;
    public final Class<ArrayList<?>> LIST = (Class<ArrayList<?>>) (Class<?>) ArrayList.class;
    public final Class<Boolean> BOOLEAN = Boolean.class;

    public final Class<String> STRING = String.class;

    public final Class<Long> LONG = Long.class;
    public final Class<Integer> INT = Integer.class;
    public final Class<Double> DOUBLE = Double.class;


    public final List<Class<?>> SUPPORTED_CLASSES = List.of(
            SECTION,
            LIST,
            BOOLEAN,
            LONG,
            INT,
            DOUBLE,
            STRING
    );

    public boolean isSupportedClass(Class<?> clazz) {
        return SUPPORTED_CLASSES.contains(clazz);
    }

    public boolean isNumberClass(Class<?> clazz) {
        return clazz == LONG || clazz == INT || clazz == DOUBLE;
    }

    public Object getDefaultValue(Class<?> clazz) {
        if (clazz == BOOLEAN) return false;
        if (clazz == STRING) return "";
        if (clazz == LONG) return 0L;
        if (clazz == INT) return 0;
        if (clazz == DOUBLE) return 0.0;
        if (clazz == LIST) return new ArrayList<>();
        return null;
    }
}
