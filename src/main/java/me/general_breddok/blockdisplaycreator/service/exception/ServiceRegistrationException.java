package me.general_breddok.blockdisplaycreator.service.exception;

public class ServiceRegistrationException extends IllegalArgumentException {
    public ServiceRegistrationException() {
    }

    public ServiceRegistrationException(String message) {
        super(message);
    }

    public ServiceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRegistrationException(Throwable cause) {
        super(cause);
    }
}
