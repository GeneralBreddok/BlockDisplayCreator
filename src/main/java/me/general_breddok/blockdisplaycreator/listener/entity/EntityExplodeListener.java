package me.general_breddok.blockdisplaycreator.listener.entity;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            CustomBlockData customBlockData = new CustomBlockData(block, BlockDisplayCreator.getInstance());

            return customBlockData.has(CustomBlockKey.NAME);
        });
    }
}
