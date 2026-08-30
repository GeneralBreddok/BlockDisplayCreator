package me.general_breddok.blockdisplaycreator.web.exception;

public class BDEModelNotFoundException extends InvalidResponseException {
    public BDEModelNotFoundException() {
    }

    public BDEModelNotFoundException(String message) {
        super(message);
    }

    public BDEModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BDEModelNotFoundException(Throwable cause) {
        super(cause);
    }
}
