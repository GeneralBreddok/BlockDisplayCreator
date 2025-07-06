package me.general_breddok.blockdisplaycreator.listener.inventory;

import com.google.common.reflect.TypeToken;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemCraftListener implements Listener {

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();

        if (result == null)
            return;

        for (ItemStack itemStack : event.getInventory().getMatrix()) {

            if (itemStack == null)
                continue;

            if (itemStack.getItemMeta() == null)
                continue;

            PersistentData<String> persistentData = new PersistentData<>(itemStack.getItemMeta(), TypeTokens.STRING);

            if (persistentData.has(CustomBlockKey.NAME))
                continue;

            event.getInventory().setResult(new ItemStack(Material.AIR));

            return;
        }
    }
}
