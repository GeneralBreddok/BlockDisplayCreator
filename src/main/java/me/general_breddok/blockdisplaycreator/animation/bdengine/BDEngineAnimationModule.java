package me.general_breddok.blockdisplaycreator.animation.bdengine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunctionFile;
import org.bukkit.packs.DataPack;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BDEngineAnimationModule implements BDEngineAnimationPack {
    final Path animComplexPath;
    MCFunction createFunction;
    MCFunction deleteFunction;
    MCFunction stopFunction;
    Map<String, FunctionAnimation> animations;
    Map<String, FunctionAnimator> animators;


    public BDEngineAnimationModule(Path animModulePath) {
        this.animComplexPath = animModulePath;

        if (!Files.isDirectory(animModulePath)) {
            return;
        }

        Path funcPath = animModulePath.resolve("function");

        Path dominantFuncPath = funcPath.resolve("_");
        Path animControlPath = funcPath.resolve("a");
        Path animPath = funcPath.resolve("k");

        this.createFunction = new MCFunctionFile(dominantFuncPath.resolve("create.mcfunction"));
        this.deleteFunction = new MCFunctionFile(dominantFuncPath.resolve("delete.mcfunction"));
        this.stopFunction = new MCFunctionFile(dominantFuncPath.resolve("stop.mcfunction"));

        try (Stream<Path> stream = Files.list(animControlPath)) {
            this.animators = stream.map(path ->
                            new AbstractMap.SimpleEntry<>(
                                    path.getFileName().toString(),
                                    new DataPackFunctionAnimator(path)
                            )
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (Stream<Path> stream = Files.list(animPath)) {
            this.animations = stream.map(path ->
                            new AbstractMap.SimpleEntry<>(
                                    path.getFileName().toString(),
                                    new DataPackFunctionAnimation(path)
                            )
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Override
    public String getTag() {
        return animComplexPath.getFileName().toString();
    }
}
