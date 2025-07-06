package me.general_breddok.blockdisplaycreator.file.config.loader;

import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface CustomBlockRepository {
    List<AbstractCustomBlock> getAbstractCustomBlocks();
    @Nullable
    CustomBlockConfigurationFile getFile(String name);

    void addFile(CustomBlockConfigurationFile configurationFile);

    boolean hasFile(String name);

    List<CustomBlockConfigurationFile> getFiles();

    Path getPath();
}
