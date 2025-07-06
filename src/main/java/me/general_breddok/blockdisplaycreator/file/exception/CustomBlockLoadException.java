package me.general_breddok.blockdisplaycreator.file.exception;

import java.io.File;

public class CustomBlockLoadException extends FileLoadException {
    public CustomBlockLoadException() {
        super();
    }

    public CustomBlockLoadException(String message, Object... args) {
        super(String.format(message, args));
    }

    public CustomBlockLoadException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }

    public CustomBlockLoadException(Throwable cause) {
        super(cause);
    }
}
