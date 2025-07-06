package me.general_breddok.blockdisplaycreator.listener.block;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> {
            CustomBlockData customBlockData = new CustomBlockData(block, BlockDisplayCreator.getInstance());

            return customBlockData.has(CustomBlockKey.NAME);
        });
    }
}
