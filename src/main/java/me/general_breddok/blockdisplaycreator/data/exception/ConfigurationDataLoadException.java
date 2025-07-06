package me.general_breddok.blockdisplaycreator.data.exception;

public class ConfigurationDataLoadException extends DataLoadException {
    public ConfigurationDataLoadException(String key) {
        super(key);
    }

    public ConfigurationDataLoadException(String message, String key) {
        super(message, key);
    }

    public ConfigurationDataLoadException(String message, Throwable cause, String key) {
        super(message, cause, key);
    }

    public ConfigurationDataLoadException(Throwable cause, String key) {
        super(cause, key);
    }

    @Override
    public String getDataKey() {
        return (String) dataKey;
    }
}
