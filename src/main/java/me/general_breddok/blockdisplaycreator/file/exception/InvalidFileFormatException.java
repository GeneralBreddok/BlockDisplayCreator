package me.general_breddok.blockdisplaycreator.file.exception;

import java.io.IOException;

public class InvalidFileFormatException extends IllegalArgumentException {
    public InvalidFileFormatException() {
        super();
    }

    public InvalidFileFormatException(String message) {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileFormatException(Throwable cause) {
        super(cause);
    }
}
