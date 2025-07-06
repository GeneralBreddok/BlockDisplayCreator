package me.general_breddok.blockdisplaycreator.animation.bdengine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunction;
import me.general_breddok.blockdisplaycreator.file.mcfunction.MCFunctionFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataPackFunctionAnimation implements FunctionAnimation {
    List<MCFunction> functions;

    public DataPackFunctionAnimation(Path folderPath) {
        try (Stream<Path> stream = Files.list(folderPath)) {
            this.functions = stream.map(path -> (MCFunction) new MCFunctionFile(path)).toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
