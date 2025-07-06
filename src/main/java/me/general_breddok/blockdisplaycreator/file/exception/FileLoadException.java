package me.general_breddok.blockdisplaycreator.file.exception;

public class FileLoadException extends RuntimeException {
    public FileLoadException() {
    }

    public FileLoadException(String message) {
        super(message);
    }

    public FileLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileLoadException(Throwable cause) {
        super(cause);
    }
}
