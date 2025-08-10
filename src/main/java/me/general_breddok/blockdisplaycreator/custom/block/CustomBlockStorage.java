package me.general_breddok.blockdisplaycreator.custom.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Storage interface for managing abstract custom blocks.
 * Provides methods to retrieve, save, delete, and reload custom block definitions.
 */
public interface CustomBlockStorage {

    /**
     * Retrieves an abstract custom block by its name.
     *
     * @param name the name of the custom block
     * @param data additional data for lookup
     * @return the {@link AbstractCustomBlock} if found, otherwise null
     */
    @Nullable
    AbstractCustomBlock getAbstractCustomBlock(@NotNull String name, Object... data);
    /**
     * Checks if an abstract custom block with the given name exists in storage.
     *
     * @param name the name of the custom block to check
     * @return true if the custom block exists, false otherwise
     */
    boolean containsAbstractCustomBlock(@NotNull String name);
    /**
     * Reloads all abstract custom blocks from storage.
     * @param onComplete an optional callback to execute after reloading is complete
     */
    void reloadAll(@Nullable Runnable onComplete);
    /**
     * Reloads all abstract custom blocks from storage.
     */
    default void reloadAll() {
        reloadAll(null);
    }
    /**
     * Reloads a specific abstract custom block by its name.
     *
     * @param name the name of the custom block to reload
     */
    void reload(@NotNull String name);

    /**
     * Adds a new abstract custom block to storage.
     *
     * @param abstractCustomBlock the custom block to add
     * @throws IllegalArgumentException if a block with the same name already exists
     */
    void addAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock);

    /**
     * Removes an abstract custom block from storage by its name.
     *
     * @param name the name of the custom block to remove
     */
    void removeAbstractCustomBlock(String name);

    /**
     * Returns a list of all abstract custom blocks in storage.
     *
     * @return a list of {@link AbstractCustomBlock} instances
     */
    @NotNull
    List<AbstractCustomBlock> getAbstractCustomBlocks();

    /**
     * Returns a list of all custom block names in storage.
     *
     * @return a list of custom block names
     */
    @NotNull
    default List<String> getNames() {
        return getAbstractCustomBlocks().stream().map(AbstractCustomBlock::getName).toList();
    }
}
