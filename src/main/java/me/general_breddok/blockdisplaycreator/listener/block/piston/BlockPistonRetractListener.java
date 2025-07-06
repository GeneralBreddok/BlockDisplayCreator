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
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlockPistonRetractListener implements Listener {


    /*private ServiceManager<CustomBlockService> serviceManager;

    public BlockPistonRetractListener(ServiceManager<CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }*/

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {

        List<Block> blocks = event.getBlocks();
        BlockFace direction = event.getDirection();

        for (Block block : blocks) {

            CustomBlockData customBlockData = new CustomBlockData(block, BlockDisplayCreator.getInstance());

            if (!customBlockData.has(CustomBlockKey.NAME)) {
                continue;
            }

            /*CustomBlock customBlock = CustomBlockManager.getByLocation(location);


            if (customBlock == null) {
                event.setCancelled(true);
                return;
            }

            Location to = location.clone().add(direction.getDirection()).add(0.5, 0, 0.5);
            customBlock.setLocation(to, true);*/

            event.setCancelled(true);
            return;
        }
    }
}
