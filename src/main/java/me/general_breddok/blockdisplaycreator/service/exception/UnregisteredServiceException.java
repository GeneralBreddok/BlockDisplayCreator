package me.general_breddok.blockdisplaycreator.service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.NoSuchElementException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnregisteredServiceException extends NoSuchElementException {

    @Nullable
    String serviceClass = null;

    public UnregisteredServiceException() {
    }

    public UnregisteredServiceException(String s) {
        super(s);
    }

    public UnregisteredServiceException(String s, Throwable cause, @Nullable String serviceClass) {
        super(s, cause);
        this.serviceClass = serviceClass;
    }

    public UnregisteredServiceException(Throwable cause, @Nullable String serviceClass) {
        super(cause);
        this.serviceClass = serviceClass;
    }

    public UnregisteredServiceException(String s, @Nullable String serviceClass) {
        super(s);
        this.serviceClass = serviceClass;
    }

    public UnregisteredServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnregisteredServiceException(Throwable cause) {
        super(cause);
    }
}
