package me.general_breddok.blockdisplaycreator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark interfaces that represent options.
 * <p>
 * This annotation should be used on interfaces that are specifically designed
 * to group options or flags, similar to {@link java.nio.file.CopyOption}.
 * Interfaces marked with this annotation typically do not define methods, but
 * serve as a common type for enumerations or constants that represent different
 * behavior options in certain operations.
 * <p>
 * Example use cases include:
 * <ul>
 * <li>File system operation options</li>
 * <li>Network request handling flags</li>
 * </ul>
 *
 * This annotation is used purely for documentation and organizational purposes,
 * allowing developers to easily distinguish option-related interfaces from
 * other types of interfaces.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OptionInterface {
}
