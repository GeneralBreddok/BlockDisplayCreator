package me.general_breddok.blockdisplaycreator.data.exception;

public class MetaElementNotFoundException extends ElementNotFoundException {
    public MetaElementNotFoundException() {
    }

    public MetaElementNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public MetaElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public MetaElementNotFoundException(String s) {
        super(s);
    }
}
