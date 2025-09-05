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
import org.bukkit.Bukkit;
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
    final JavaPlugin plugin;

    public BDCCustomBlockConfigStorage(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.abstractCustomBlocks = new ArrayList<>();
    }

    @Override
    public void reloadAll(@Nullable Runnable onComplete) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ChatUtil.log("&6[BlockDisplayCreator] &eInitializing blocks...");

            CustomBlockRepository tempCustomBlockRepository = new CustomBlockFileRepository(this.plugin);
            List<AbstractCustomBlock> tempAbstractCustomBlocks = new ArrayList<>();

            for (CustomBlockConfigurationFile configurationFile : tempCustomBlockRepository.getFiles()) {
                try {
                    tempAbstractCustomBlocks.add(configurationFile.getAbstractCustomBlock());
                } catch (CustomBlockLoadException e) {
                    ChatUtil.log(e.getMessage());
                }
            }


            ChatUtil.log("&6[BlockDisplayCreator] &eInitialization complete, %d blocks initialized", tempAbstractCustomBlocks.size());

            Bukkit.getScheduler().runTask(this.plugin, () -> {
                this.customBlockRepository = tempCustomBlockRepository;
                this.abstractCustomBlocks = tempAbstractCustomBlocks;

                BlockDisplayCreatorCAPICommand bdcCommand = BlockDisplayCreator.getInstance().getBdcCommand();
                if (bdcCommand != null) {
                    bdcCommand.reloadSuggestions();
                }

                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });
    }

    @Override
    public void reload(@NotNull String name, @Nullable Runnable onComplete) throws NoSuchElementException {
        if (!getNames().contains(name)) {
            throw new NoSuchElementException("Block \"" + name + "\" is not in storage");
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            this.abstractCustomBlocks.removeIf(abstractCustomBlock -> abstractCustomBlock.getName().equals(name));

            try {
                CustomBlockConfigurationFile file = this.customBlockRepository.getFile(name);
                file.reload();

                this.abstractCustomBlocks.add(file.getAbstractCustomBlock());
            } catch (CustomBlockLoadException | IllegalArgumentException e) {
                ChatUtil.log("&c" + e.getMessage());
            }

            Bukkit.getScheduler().runTask(this.plugin, () -> {
                BlockDisplayCreatorCAPICommand bdcCommand = BlockDisplayCreator.getInstance().getBdcCommand();
                if (bdcCommand != null) {
                    bdcCommand.reloadSuggestions();
                }

                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });
    }

    @Override
    public void addAbstractCustomBlock(AbstractCustomBlock abstractCustomBlock) {
        if (this.containsAbstractCustomBlock(abstractCustomBlock.getName())) {
            throw new IllegalStateException("Block named " + abstractCustomBlock.getName() + " already exists in storage");
        }
        this.abstractCustomBlocks.add(abstractCustomBlock);
    }

    @Override
    public void removeAbstractCustomBlock(String name) throws NoSuchElementException {
        if (!this.containsAbstractCustomBlock(name)) {
            throw new NoSuchElementException("Block named " + name + " does not exists in storage");
        }
        this.abstractCustomBlocks.remove(name);
    }

    @Override
    @Nullable
    public AbstractCustomBlock getAbstractCustomBlock(@NotNull String name, Object... data) {
        for (AbstractCustomBlock abstractCustomBlock : abstractCustomBlocks) {
            if (abstractCustomBlock.getName().equals(name)) {
                AbstractCustomBlock cloned = abstractCustomBlock.clone();
                cloned.getItem().setAmount(1);
                return cloned;
            }
        }
        return null;
    }

    @Override
    public boolean containsAbstractCustomBlock(@NotNull String name) {
        return abstractCustomBlocks.stream().anyMatch(element -> element.getName().equals(name));
    }

    @NotNull
    public List<AbstractCustomBlock> getAbstractCustomBlocks() {
        return abstractCustomBlocks.stream()
                .map(AbstractCustomBlock::clone)
                .toList();
    }
}
