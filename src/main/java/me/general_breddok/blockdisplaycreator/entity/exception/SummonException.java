package me.general_breddok.blockdisplaycreator.entity.exception;

public class SummonException extends RuntimeException {
    public SummonException() {
    }

    public SummonException(String message) {
        super(message);
    }

    public SummonException(String message, Throwable cause) {
        super(message, cause);
    }

    public SummonException(Throwable cause) {
        super(cause);
    }
}
