package me.general_breddok.blockdisplaycreator.data.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;


public class UnregisteredDataTypeException extends RuntimeException {
    private final Class<?> unregisteredType;

    public UnregisteredDataTypeException(Class<?> unregisteredType) {
        this.unregisteredType = unregisteredType;
    }

    public UnregisteredDataTypeException(String message, Class<?> unregisteredType) {
        super(message);
        this.unregisteredType = unregisteredType;
    }

    public UnregisteredDataTypeException(String message, Throwable cause, Class<?> unregisteredType) {
        super(message, cause);
        this.unregisteredType = unregisteredType;
    }

    public UnregisteredDataTypeException(Throwable cause, Class<?> unregisteredType) {
        super(cause);
        this.unregisteredType = unregisteredType;
    }
}
