package me.general_breddok.blockdisplaycreator.data.locator;

import com.google.common.reflect.TypeToken;

public class MetaDataLocator<T> extends DataLocator<String, T> {
    public MetaDataLocator(String key, TypeToken<T> valueTypeToken) {
        super(key, valueTypeToken);
    }
}
