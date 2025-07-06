package me.general_breddok.blockdisplaycreator.data.locator;

import com.google.common.reflect.TypeToken;

public class ConfigLocator<T> extends DataLocator<String, T> {
    public ConfigLocator(String valuePath, TypeToken<T> valueTypeToken) {
        super(valuePath, valueTypeToken);
    }
}
