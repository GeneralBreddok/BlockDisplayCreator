package me.general_breddok.blockdisplaycreator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Utility Module Facade. An annotation that marks a class as a facade, indicating that it aggregates functionality
 * of a specific module or subsystem, providing a simplified interface to it based on
 * the use of static methods and concrete implementations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UMF {
    /**
     * The name of the module this facade class provides an interface for.
     */
    String module();
}
