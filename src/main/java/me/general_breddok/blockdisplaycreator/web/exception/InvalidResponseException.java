package me.general_breddok.blockdisplaycreator.web.exception;

public class InvalidResponseException extends Exception {
    public InvalidResponseException() {
    }

    public InvalidResponseException(String message) {
        super(message);
    }

    public InvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponseException(Throwable cause) {
        super(cause);
    }
}
