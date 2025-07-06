package me.general_breddok.blockdisplaycreator.data.exception;

public class ConfigurationElementNotFoundException extends ElementNotFoundException {
    public ConfigurationElementNotFoundException() {
        super();
    }

    public ConfigurationElementNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public ConfigurationElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public ConfigurationElementNotFoundException(String s) {
        super(s);
    }
}
