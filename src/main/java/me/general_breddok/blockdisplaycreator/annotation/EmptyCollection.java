package me.general_breddok.blockdisplaycreator.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to indicate that a method may return an empty collection.
 * <p>
 * Methods annotated with {@code @EmptyCollection} are designed to potentially
 * return an empty collection (e.g., {@link java.util.List}, {@link java.util.Set}, or {@link java.util.Map}).
 * <p>
 * This annotation serves as a reminder to developers
 * and users of the API to expect and handle empty collections.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EmptyCollection {
}
