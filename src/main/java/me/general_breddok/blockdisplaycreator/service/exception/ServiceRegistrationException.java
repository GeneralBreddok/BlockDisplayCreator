package me.general_breddok.blockdisplaycreator.service.exception;

/**
 * Exception thrown to indicate an issue during service registration.
 * Extends {@link IllegalArgumentException}.
 */
public class ServiceRegistrationException extends IllegalArgumentException {

    /**
     * Constructs a new {@code ServiceRegistrationException} with no detail message.
     */
    public ServiceRegistrationException() {
    }

    /**
     * Constructs a new {@code ServiceRegistrationException} with the specified detail message.
     *
     * @param message the detail message
     */
    public ServiceRegistrationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ServiceRegistrationException} with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ServiceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}