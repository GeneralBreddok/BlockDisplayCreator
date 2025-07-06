package me.general_breddok.blockdisplaycreator.file.exception;

public class InvalidFileNameException extends IllegalArgumentException {
    public InvalidFileNameException() {
    }
    public InvalidFileNameException(String s) {
        super(s);
    }

    public InvalidFileNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileNameException(Throwable cause) {
        super(cause);
    }
}
