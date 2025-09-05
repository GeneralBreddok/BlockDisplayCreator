package me.general_breddok.blockdisplaycreator.file.config.loader;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.AbstractCustomBlock;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.file.exception.InvalidFileFormatException;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomBlockFileRepository implements CustomBlockRepository {
    private static final String REPOSITORY_NAME = "custom-blocks";
    private final JavaPlugin plugin;
    @Getter
    private List<CustomBlockConfigurationFile> files;

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


        this.files = loadYamlFiles(repository);
    }

    public static List<CustomBlockConfigurationFile> loadYamlFiles(Path repository) {
        try (Stream<Path> stream = Files.walk(repository)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return new CustomBlockYamlFile(new YamlConfigFile(path));
                        } catch (InvalidFileFormatException ignore) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<AbstractCustomBlock> getAbstractCustomBlocks() {
        return files.stream().map(CustomBlockConfigurationFile::getAbstractCustomBlock).collect(OperationUtil.toArrayList());
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
