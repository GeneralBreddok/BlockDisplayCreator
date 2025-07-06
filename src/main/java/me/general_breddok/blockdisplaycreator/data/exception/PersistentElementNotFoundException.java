package me.general_breddok.blockdisplaycreator.data.exception;

public class PersistentElementNotFoundException extends ElementNotFoundException {
    public PersistentElementNotFoundException() {
    }

    public PersistentElementNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public PersistentElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public PersistentElementNotFoundException(String s) {
        super(s);
    }
}
