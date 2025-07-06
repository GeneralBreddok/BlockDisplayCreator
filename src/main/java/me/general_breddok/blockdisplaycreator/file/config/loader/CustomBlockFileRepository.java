package me.general_breddok.blockdisplaycreator.file.config.loader;

import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldguard.WorldGuard;
import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.file.exception.InvalidFileFormatException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CustomBlockFileRepository implements CustomBlockRepository {
    private final JavaPlugin plugin;
    @Getter
    private List<CustomBlockConfigurationFile> files;

    private static final String REPOSITORY_NAME = "custom-blocks";

    public CustomBlockFileRepository(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.files = new ArrayList<>();
        Path dataFolder = plugin.getDataFolder().toPath();

        Path repository = dataFolder.resolve(REPOSITORY_NAME);

        if (Files.notExists(repository)) {
            try {
                Files.createDirectory(repository);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        try (Stream<Path> stream = Files.list(repository)) {
            files.addAll(stream
                    .map(path -> {
                        CustomBlockConfigurationFile file = null;
                        try {
                           file = new CustomBlockYamlFile(new YamlConfigFile(path));
                        } catch (InvalidFileFormatException ignore) {
                        }
                        return file;
                    })
                    .filter(Objects::nonNull)
                    .toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<AbstractCustomBlock> getAbstractCustomBlocks() {
        return files.stream().map(CustomBlockConfigurationFile::getAbstractCustomBlock).toList();
    }


    @Nullable
    public CustomBlockConfigurationFile getFile(String name) {
        return files.stream()
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addFile(CustomBlockConfigurationFile configurationFile) {
        files.add(configurationFile);
    }

    @Override
    public boolean hasFile(String name) {
        return files.stream()
                .anyMatch(file -> name.equals(file.getName()));
    }

    public Path getPath() {
        return Path.of(plugin.getDataFolder().toPath().toString(), REPOSITORY_NAME);
    }
}
