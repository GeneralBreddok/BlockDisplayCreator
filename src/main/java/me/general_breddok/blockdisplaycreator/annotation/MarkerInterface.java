package me.general_breddok.blockdisplaycreator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark interfaces
 * that serve as markers in the system. Marker interfaces do not contain
 * any methods and are used to signify a certain behavior or property
 * of the classes that implement them.
 *
 * <p>Applying this annotation allows developers and tools to more easily
 * identify interfaces intended for use as markers, which can improve
 * code readability and maintainability.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @MarkerInterface
 * public interface Serializable {
 *     // This interface serves as a marker for serializing objects.
 * }
 * }
 * </pre>
 *
 * @see java.io.Serializable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MarkerInterface {
}