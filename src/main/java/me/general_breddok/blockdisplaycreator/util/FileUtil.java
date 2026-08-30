package me.general_breddok.blockdisplaycreator.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class FileUtil {

    public boolean containsFile(Path directory, String fileName) {
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.getFileName().toString().equals(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path findFile(Path directory, String fileName) {
        try (Stream<Path> paths = Files.walk(directory)) {
            Optional<Path> result = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst();

            return result.orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
