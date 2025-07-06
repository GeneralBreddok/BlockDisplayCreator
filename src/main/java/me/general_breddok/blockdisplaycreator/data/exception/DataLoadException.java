package me.general_breddok.blockdisplaycreator.data.exception;

import lombok.Getter;

@Getter
public class DataLoadException extends RuntimeException {
    final Object dataKey;
    public DataLoadException(Object dataKey) {
        this.dataKey = dataKey;
    }

    public DataLoadException(String message, Object dataKey) {
        super(message);
        this.dataKey = dataKey;
    }

    public DataLoadException(String message, Throwable cause, Object dataKey) {
        super(message, cause);
        this.dataKey = dataKey;
    }

    public DataLoadException(Throwable cause, Object dataKey) {
        super(cause);
        this.dataKey = dataKey;
    }
}
