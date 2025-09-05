package me.general_breddok.blockdisplaycreator.file.config.loader;

import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public interface CustomBlockRepository {
    List<AbstractCustomBlock> getAbstractCustomBlocks();

    @Nullable
    CustomBlockConfigurationFile getFile(String name);

    void addFile(CustomBlockConfigurationFile configurationFile);

    boolean hasFile(String name);

    List<CustomBlockConfigurationFile> getFiles();

    Path getPath();
}
