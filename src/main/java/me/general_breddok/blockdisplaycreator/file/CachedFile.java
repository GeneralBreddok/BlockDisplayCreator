package me.general_breddok.blockdisplaycreator.file;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

@Getter
public abstract class CachedFile {
    protected Path path;

    public CachedFile(@NotNull Path path, boolean createNew) {
        this.path = path;
        load(path, createNew);
    }

    public abstract void save();
    protected abstract void load(@NotNull Path path, boolean createNew);
    public abstract void reload();
    public abstract void reload(boolean loadNew);
}
