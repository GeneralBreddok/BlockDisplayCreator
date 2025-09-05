package me.general_breddok.blockdisplaycreator.entity.exception;

/**
 * Runtime exception thrown when an entity cannot be summoned.
 */
public class SummonException extends RuntimeException {

    /**
     * Constructs a new SummonException with no detail message.
     */
    public SummonException() {
    }

    /**
     * Constructs a new SummonException with the specified detail message.
     *
     * @param message the detail message
     */
    public SummonException(String message) {
        super(message);
    }

    /**
     * Constructs a new SummonException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public SummonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new SummonException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public SummonException(Throwable cause) {
        super(cause);
    }
}

