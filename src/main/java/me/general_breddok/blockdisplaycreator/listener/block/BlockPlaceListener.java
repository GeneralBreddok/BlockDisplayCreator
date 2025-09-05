package me.general_breddok.blockdisplaycreator.listener.block;

import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!CustomBlockKey.holder(event.getItemInHand().getItemMeta()).hasName())
            return;

        event.setCancelled(true);
    }
}
