package me.general_breddok.blockdisplaycreator.nbt.exception;

import java.io.IOException;

public class NbtParseException extends IOException {
    public NbtParseException() {
    }

    public NbtParseException(String message) {
        super(message);
    }

    public NbtParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NbtParseException(Throwable cause) {
        super(cause);
    }
}
