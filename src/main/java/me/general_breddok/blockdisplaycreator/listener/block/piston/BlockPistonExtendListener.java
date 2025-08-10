package me.general_breddok.blockdisplaycreator.listener.block.piston;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.List;

public class BlockPistonExtendListener implements Listener {

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {

        List<Block> blocks = event.getBlocks();

        for (Block block : blocks) {

            CustomBlockData customBlockData = new CustomBlockData(block, BlockDisplayCreator.getInstance());

            if (!customBlockData.has(CustomBlockKey.NAME)) {
                continue;
            }

            event.setCancelled(true);
            return;
        }
    }
}
