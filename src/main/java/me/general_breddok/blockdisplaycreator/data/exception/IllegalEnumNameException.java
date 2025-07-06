package me.general_breddok.blockdisplaycreator.data.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class IllegalEnumNameException extends IllegalArgumentException {

    @Nullable
    private final Class<Enum<? extends Enum<?>>> enumClass;
    private final String invalidName;

    public IllegalEnumNameException(Class<Enum<? extends Enum<?>>> enumClass, String invalidName) {
        this.enumClass = enumClass;
        this.invalidName = invalidName;
    }

    public IllegalEnumNameException(String s, Class<Enum<? extends Enum<?>>> enumClass, String invalidName) {
        super(s);
        this.enumClass = enumClass;
        this.invalidName = invalidName;
    }

    public IllegalEnumNameException(String s, IllegalEnumNameException exception) {
        super(s);
        this.enumClass = exception.getEnumClass();
        this.invalidName = exception.getInvalidName();
    }

    public IllegalEnumNameException(String message, Throwable cause, Class<Enum<? extends Enum<?>>> enumClass, String invalidName) {
        super(message, cause);
        this.enumClass = enumClass;
        this.invalidName = invalidName;
    }

    public IllegalEnumNameException(Throwable cause, Class<Enum<? extends Enum<?>>> enumClass, String invalidName) {
        super(cause);
        this.enumClass = enumClass;
        this.invalidName = invalidName;
    }
}
