package me.general_breddok.blockdisplaycreator.animation.bdengine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunctionFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataPackBDEAnimationSet implements BDEAnimationSet {
    Path namespace;
    Map<String, BDEAnimation> transformationAnimations;
    Map<String, BDEAnimation> soundAnimations;
    Map<String, BDEAnimator> transformationAnimators;
    Map<String, BDEAnimator> soundAnimators;
    MCFunction createFunction;
    MCFunction deleteFunction;
    MCFunction stopTransformationAnimationFunction;
    MCFunction stopSoundAnimationFunction;


    public DataPackBDEAnimationSet(@NotNull Path namespace) {
        this.namespace = namespace;

        if (!Files.isDirectory(namespace)) {
            throw new IllegalArgumentException("Namespace must be an existing directory: " + namespace);
        }

        Path functionPath = namespace.resolve("function");
        Path controlPath = functionPath.resolve("_");

        this.createFunction = new MCFunctionFile(controlPath.resolve("create.mcfunction"));
        this.deleteFunction = new MCFunctionFile(controlPath.resolve("delete.mcfunction"));
        this.stopTransformationAnimationFunction = new MCFunctionFile(controlPath.resolve("stop_anim.mcfunction"));
        this.stopSoundAnimationFunction = new MCFunctionFile(controlPath.resolve("stop_sound.mcfunction"));

        this.transformationAnimators = loadMap(
                functionPath.resolve("a"),
                DataPackBDEAnimator::new
        );

        this.soundAnimators = loadMap(
                functionPath.resolve("a_s"),
                DataPackBDEAnimator::new
        );

        this.transformationAnimations = loadMap(
                functionPath.resolve("k"),
                DataPackBDEAnimation::new
        );

        this.soundAnimations = loadMap(
                functionPath.resolve("k_s"),
                DataPackBDEAnimation::new
        );
    }

    @Override
    public String getTag() {
        Path fileName = namespace.getFileName();
        return fileName != null ? fileName.toString() : namespace.toString();
    }

    private static <T> Map<String, T> loadMap(Path directory, Function<Path, T> mapper) {
        if (!Files.isDirectory(directory)) {
            return Collections.emptyMap();
        }

        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    path -> path.getFileName().toString(),
                                    mapper
                            ),
                            Collections::unmodifiableMap
                    ));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read directory: " + directory, e);
        }
    }
}
