package me.general_breddok.blockdisplaycreator.custom.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;

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
     * Checks if the specified abstract custom block exists in storage.
     *
     * @param abstractCustomBlock the custom block to check
     * @return true if the block exists, false otherwise
     */
    boolean containsAbstractCustomBlock(@NotNull AbstractCustomBlock abstractCustomBlock);

    /**
     * Reloads all abstract custom blocks from the storage source.
     */
    void reloadAll();

    /**
     * Reloads a specific abstract custom block by its name.
     *
     * @param name the name of the custom block to reload
     */
    void reload(@NotNull String name);

    /**
     * Saves the specified abstract custom block to storage.
     *
     * @param abstractCustomBlock the custom block to save
     */
    void saveAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock);

    /**
     * Deletes the specified abstract custom block from storage.
     *
     * @param abstractCustomBlock the custom block to delete
     */
    void deleteAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock);

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
