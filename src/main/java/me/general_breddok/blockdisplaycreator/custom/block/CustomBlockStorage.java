package me.general_breddok.blockdisplaycreator.custom.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;

public interface CustomBlockStorage {
    @Nullable
    AbstractCustomBlock getAbstractCustomBlock(@NotNull String name, Object... data);
    boolean containsAbstractCustomBlock(@NotNull AbstractCustomBlock abstractCustomBlock);
    void reloadAll();
    void reload(@NotNull String name);

    void saveAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock);
    void deleteAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock);

    @NotNull
    List<AbstractCustomBlock> getAbstractCustomBlocks();
    @NotNull
    default List<String> getNames() {
        return getAbstractCustomBlocks().stream().map(AbstractCustomBlock::getName).toList();
    }
}
