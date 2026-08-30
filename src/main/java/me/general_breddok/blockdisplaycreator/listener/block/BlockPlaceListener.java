package me.general_breddok.blockdisplaycreator.listener.block;

import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null)
            return;

        if (!CustomBlockKey.holder(itemMeta).hasName())
            return;

        event.setCancelled(true);
    }
}
