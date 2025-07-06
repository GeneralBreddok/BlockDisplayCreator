package me.general_breddok.blockdisplaycreator.web.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionNotFoundException extends InvalidResponseException {
    String path;

    public SessionNotFoundException(String message, String path) {
        super(message);
        this.path = path;
    }

    public SessionNotFoundException(String message, String path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }
}
