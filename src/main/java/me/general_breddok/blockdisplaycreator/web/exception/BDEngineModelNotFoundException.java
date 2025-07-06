package me.general_breddok.blockdisplaycreator.web.exception;

public class BDEngineModelNotFoundException extends InvalidResponseException {
    public BDEngineModelNotFoundException() {
    }

    public BDEngineModelNotFoundException(String message) {
        super(message);
    }

    public BDEngineModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BDEngineModelNotFoundException(Throwable cause) {
        super(cause);
    }
}
