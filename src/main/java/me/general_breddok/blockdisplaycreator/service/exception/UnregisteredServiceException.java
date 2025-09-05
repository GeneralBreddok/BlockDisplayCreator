package me.general_breddok.blockdisplaycreator.service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * Exception thrown when attempting to access a service
 * that has not been registered.
 * <p>
 * Extends {@link NoSuchElementException} to signal that the
 * requested element (service) could not be found.
 * </p>
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnregisteredServiceException extends NoSuchElementException {

    /**
     * The class name of the service that could not be found,
     * or {@code null} if not specified.
     */
    @Nullable
    String serviceClass = null;

    /**
     * Creates an empty exception without a detail message.
     */
    public UnregisteredServiceException() {
    }

    /**
     * Creates an exception with the given detail message.
     *
     * @param s the detail message
     */
    public UnregisteredServiceException(String s) {
        super(s);
    }

    /**
     * Creates an exception with a message, cause, and service class name.
     *
     * @param s            the detail message
     * @param cause        the cause of the exception
     * @param serviceClass the class name of the service (nullable)
     */
    public UnregisteredServiceException(String s, Throwable cause, @Nullable String serviceClass) {
        super(s, cause);
        this.serviceClass = serviceClass;
    }

    /**
     * Creates an exception with a cause and service class name.
     *
     * @param cause        the cause of the exception
     * @param serviceClass the class name of the service (nullable)
     */
    public UnregisteredServiceException(Throwable cause, @Nullable String serviceClass) {
        super(cause);
        this.serviceClass = serviceClass;
    }

    /**
     * Creates an exception with a message and service class name.
     *
     * @param s            the detail message
     * @param serviceClass the class name of the service (nullable)
     */
    public UnregisteredServiceException(String s, @Nullable String serviceClass) {
        super(s);
        this.serviceClass = serviceClass;
    }

    /**
     * Creates an exception with a message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public UnregisteredServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception with a cause.
     *
     * @param cause the cause of the exception
     */
    public UnregisteredServiceException(Throwable cause) {
        super(cause);
    }
}
