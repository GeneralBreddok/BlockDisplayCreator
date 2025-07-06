package me.general_breddok.blockdisplaycreator.web.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileNotFoundException extends InvalidResponseException {
    final String path;
    @Nullable
    String error = null;

    public ProfileNotFoundException(String path, String message) {
        super(message);
        this.path = path;
    }

    public ProfileNotFoundException(String path, String message, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public ProfileNotFoundException(String path, @Nullable String error, String message) {
        super(message);
        this.path = path;
        this.error = error;
    }

    public ProfileNotFoundException(String path, @Nullable String error, String message, Throwable cause) {
        super(message, cause);
        this.path = path;
        this.error = error;
    }
}
