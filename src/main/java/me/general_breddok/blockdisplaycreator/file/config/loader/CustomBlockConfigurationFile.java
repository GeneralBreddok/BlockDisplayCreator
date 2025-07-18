package me.general_breddok.blockdisplaycreator.file.config.loader;

import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface CustomBlockConfigurationFile {
    AbstractCustomBlock getAbstractCustomBlock();
    void set(String key, Object value);
    Object get(String key);
    String getName();
    String getPath();
    void reload();
}
