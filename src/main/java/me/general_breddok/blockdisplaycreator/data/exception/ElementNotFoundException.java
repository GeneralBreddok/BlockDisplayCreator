package me.general_breddok.blockdisplaycreator.data.exception;

import java.util.NoSuchElementException;

public class ElementNotFoundException extends NoSuchElementException {
    public ElementNotFoundException() {
    }

    public ElementNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public ElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public ElementNotFoundException(String s) {
        super(s);
    }
}
