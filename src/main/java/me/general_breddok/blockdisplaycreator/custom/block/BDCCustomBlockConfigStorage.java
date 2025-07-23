package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.command.capi.BlockDisplayCreatorCAPICommand;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockConfigurationFile;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockFileRepository;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockRepository;
import me.general_breddok.blockdisplaycreator.file.exception.CustomBlockLoadException;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BDCCustomBlockConfigStorage implements CustomBlockStorage {
    List<AbstractCustomBlock> abstractCustomBlocks;
    CustomBlockRepository customBlockRepository;
    JavaPlugin plugin;

    public BDCCustomBlockConfigStorage(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.abstractCustomBlocks = new ArrayList<>();
    }

    @Override
    public void reloadAll() {
        ChatUtil.log("&6[BlockDisplayCreator] &eInitializing blocks...");

        BlockDisplayCreator plugin = BlockDisplayCreator.getInstance();

        this.abstractCustomBlocks.clear();
        this.customBlockRepository = new CustomBlockFileRepository(plugin);

        for (CustomBlockConfigurationFile configurationFile : this.customBlockRepository.getFiles()) {
            try {
                this.abstractCustomBlocks.add(configurationFile.getAbstractCustomBlock());
            } catch (CustomBlockLoadException e) {
                ChatUtil.log(e.getMessage());
            }
        }

        BlockDisplayCreatorCAPICommand bdcCommand = BlockDisplayCreator.getInstance().getBdcCommand();
        if (bdcCommand != null) {
            bdcCommand.reloadSuggestions();
        }
        ChatUtil.log("&6[BlockDisplayCreator] &eInitialization complete, %d blocks initialized", this.abstractCustomBlocks.size());
    }

    @Override
    public void reload(@NotNull String name) {
        if (!getNames().contains(name)) {
            throw new NoSuchElementException("Block \"" + name + "\" is not in storage");
        }

        final AbstractCustomBlock[] block = new AbstractCustomBlock[1];
        this.abstractCustomBlocks.removeIf(abstractCustomBlock -> {
            if (abstractCustomBlock.getName().equals(name)) {
                block[0] = abstractCustomBlock;
                return true;
            }
            return false;
        });

        try {
            CustomBlockConfigurationFile file = this.customBlockRepository.getFile(name);
            file.reload();

            this.abstractCustomBlocks.add(file.getAbstractCustomBlock());
        } catch (CustomBlockLoadException | IllegalArgumentException e) {
            ChatUtil.log("&c" + e.getMessage());
        }

        BlockDisplayCreatorCAPICommand bdcCommand = BlockDisplayCreator.getInstance().getBdcCommand();
        if (bdcCommand != null) {
            bdcCommand.reloadSuggestions();
        }
    }

    @Override
    public void saveAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock) {
        if (this.containsAbstractCustomBlock(abstractCustomBlock)) {
            throw new IllegalStateException("Block named " + abstractCustomBlock.getName() + " already exists in storage");
        }
        this.abstractCustomBlocks.add(abstractCustomBlock);
    }

    @Override
    public void deleteAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock) throws NoSuchElementException {
        if (!this.containsAbstractCustomBlock(abstractCustomBlock)) {
            throw new NoSuchElementException("Block named " + abstractCustomBlock.getName() + " does not exists in storage");
        }
        this.abstractCustomBlocks.remove(abstractCustomBlock);
    }

    @Override
    @Nullable
    public AbstractCustomBlock getAbstractCustomBlock(@NotNull String name, Object... data) {
        for (AbstractCustomBlock abstractCustomBlock : abstractCustomBlocks) {
            if (abstractCustomBlock.getName().equals(name)) {
                return abstractCustomBlock.clone();
            }
        }
        return null;
    }


    @Override
    public boolean containsAbstractCustomBlock(@NotNull AbstractCustomBlock abstractCustomBlock) {
        String name = abstractCustomBlock.getName();
        return abstractCustomBlocks.stream().anyMatch(element -> element.getName().equals(name));
    }
}
