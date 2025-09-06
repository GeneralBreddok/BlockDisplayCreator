package me.general_breddok.blockdisplaycreator.file;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents an abstract cached file with functionality to load, save, and reload its contents.
 */
@Getter
public abstract class CachedFile {
    /**
     * The path to the file.
     */
    protected Path path;

    /**
     * Constructs a new CachedFile instance and loads the file.
     *
     * @param path      the path to the file, must not be null
     * @param createNew whether to create a new file if it does not exist
     */
    public CachedFile(@NotNull Path path, boolean createNew) {
        this.path = path;
        load(path, createNew);
    }

    /**
     * Saves the current state of the file.
     */
    public abstract void save();

    /**
     * Loads the file from the specified path.
     *
     * @param path      the path to the file, must not be null
     * @param createNew whether to create a new file if it does not exist
     */
    protected abstract void load(@NotNull Path path, boolean createNew);

    /**
     * Reloads the file, typically re-reading its contents.
     * Must be implemented by subclasses.
     */
    public abstract void reload();

    /**
     * Reloads the file with an option to create and load a new file if the file does not exist.
     *
     * @param loadNew whether to load a new file if the file does not exist
     */
    public abstract void reload(boolean loadNew);
}
