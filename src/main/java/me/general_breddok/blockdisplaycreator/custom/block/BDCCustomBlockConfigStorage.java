package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.data.exception.ElementNotFoundException;
import me.general_breddok.blockdisplaycreator.data.yaml.YamlConfigFile;
import me.general_breddok.blockdisplaycreator.file.config.CustomBlockLoader;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockConfigurationFile;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockFileRepository;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockRepository;
import me.general_breddok.blockdisplaycreator.file.config.loader.CustomBlockYamlFile;
import me.general_breddok.blockdisplaycreator.file.exception.CustomBlockLoadException;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BDCCustomBlockConfigStorage implements CustomBlockStorage {
    private List<AbstractCustomBlock> abstractCustomBlocks = new ArrayList<>();
    @Getter
    private CustomBlockRepository customBlockRepository;

    @Override
    public void reloadAll() {
        abstractCustomBlocks.clear();
        ChatUtil.log("&6[BlockDisplayCreator] &eInitializing blocks...");

        BlockDisplayCreator plugin = BlockDisplayCreator.getInstance();


        customBlockRepository = new CustomBlockFileRepository(plugin);

        CustomBlockLoader configLoader = new CustomBlockLoader(plugin.getYamlConfiguration(), plugin);
        configLoader.moveBlocksToFiles(customBlockRepository);


        for (CustomBlockConfigurationFile configurationFile : customBlockRepository.getFiles()) {
            try {
                abstractCustomBlocks.add(configurationFile.getAbstractCustomBlock());
            } catch (CustomBlockLoadException e) {
                ChatUtil.log(e.getMessage());
            }
        }

        BlockDisplayCreator.getInstance().getBdcCommand().reloadSuggestions();
        ChatUtil.log("&6[BlockDisplayCreator] &eInitialization complete, %d blocks initialized", abstractCustomBlocks.size());
    }

    @Override
    public void reload(@NotNull String name) {
        if (!getNames().contains(name)) {
            throw new NoSuchElementException("Block \"" + name + "\" is not in storage");
        }

        final AbstractCustomBlock[] block = new AbstractCustomBlock[1];
        abstractCustomBlocks.removeIf(abstractCustomBlock -> {
            if (abstractCustomBlock.getName().equals(name)) {
                block[0] = abstractCustomBlock;
                return true;
            }
            return false;
        });

        try {
            CustomBlockConfigurationFile file = this.customBlockRepository.getFile(name);
            file.reload();

            abstractCustomBlocks.add(file.getAbstractCustomBlock());
        } catch (CustomBlockLoadException | IllegalArgumentException e) {
            if (block[0] != null) {
                abstractCustomBlocks.add(block[0]);
            }
            ChatUtil.log("&c" + e.getMessage());
        }

        BlockDisplayCreator.getInstance().getBdcCommand().reloadSuggestions();
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
    public @NotNull List<AbstractCustomBlock> getAbstractCustomBlocks() {
        return abstractCustomBlocks;
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
