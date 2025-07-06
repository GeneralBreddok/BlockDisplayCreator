package me.general_breddok.blockdisplaycreator.data.exception;

import lombok.Getter;

@Getter
public class ConfigurationDataTypeMismatchException extends ConfigurationDataLoadException {
    private final Class<?> expectedType;
    private final Object receivedValue;

    public ConfigurationDataTypeMismatchException(String key, Class<?> expectedType, Object receivedValue) {
        super(key);
        this.expectedType = expectedType;
        this.receivedValue = receivedValue;
    }

    public ConfigurationDataTypeMismatchException(String message, String key, Class<?> expectedType, Object receivedValue) {
        super(message, key);
        this.expectedType = expectedType;
        this.receivedValue = receivedValue;
    }

    public ConfigurationDataTypeMismatchException(String message, Throwable cause, String key, Class<?> expectedType, Object receivedValue) {
        super(message, cause, key);
        this.expectedType = expectedType;
        this.receivedValue = receivedValue;
    }

    public ConfigurationDataTypeMismatchException(Throwable cause, String key, Class<?> expectedType, Object receivedValue) {
        super(cause, key);
        this.expectedType = expectedType;
        this.receivedValue = receivedValue;
    }
}
