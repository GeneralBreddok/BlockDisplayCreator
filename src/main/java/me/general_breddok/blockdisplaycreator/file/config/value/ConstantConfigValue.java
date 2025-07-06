package me.general_breddok.blockdisplaycreator.file.config.value;

import me.general_breddok.blockdisplaycreator.data.yaml.YamlData;

public interface ConstantConfigValue<T> {
    void initialize(YamlData<T> configuration);
}
