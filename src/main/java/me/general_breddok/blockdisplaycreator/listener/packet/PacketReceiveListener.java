package me.general_breddok.blockdisplaycreator.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromBlock;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromEntity;
import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.util.EntityUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Supplier;

public final class PacketReceiveListener {

    public static PacketListener get() {
        return new PacketListener() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                PacketTypeCommon packetType = event.getPacketType();

                CustomBlockService customBlockService = BlockDisplayCreator.getInstance().getCustomBlockService();

                if (packetType == PacketType.Play.Client.PICK_ITEM_FROM_ENTITY) {

                    WrapperPlayClientPickItemFromEntity pickItem = new WrapperPlayClientPickItemFromEntity(event);
                    Player player = event.getPlayer();

                    Entity entity = EntityUtil.getNearEntity(player.getLocation(), pickItem.getEntityId());

                    if (entity == null) {
                        return;
                    }

                    handleSync(event, () -> customBlockService.getCustomBlock(entity), entity.getPersistentDataContainer());

                } else if (packetType == PacketType.Play.Client.PICK_ITEM_FROM_BLOCK) {

                    WrapperPlayClientPickItemFromBlock pickItemFromBlock = new WrapperPlayClientPickItemFromBlock(event);
                    Player player = event.getPlayer();

                    Location blockLocation = LocationUtil.toLocation(pickItemFromBlock.getBlockPos(), player.getWorld());

                    handleSync(event, () -> customBlockService.getCustomBlock(blockLocation), new CustomBlockData(blockLocation.getBlock(), BlockDisplayCreator.getInstance()));
                }
            }



            private void handleSync(PacketReceiveEvent event, Supplier<CustomBlock> blockSupplier, PersistentDataContainer blockContainer) {
                if (blockContainer.has(CustomBlockKey.NAME, PersistentDataType.STRING)) {

                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(BlockDisplayCreator.getInstance(), () -> {
                        CustomBlock block = blockSupplier.get();
                        selectCustomBlockItem(event.getPlayer(), block);
                    });
                }
            }
        };
    }


    private static void selectCustomBlockItem(Player player, CustomBlock block) {
        ItemStack blockItem = block.getItem();

        ItemUtil.selectItem(player, stack -> {

            if (stack.getType() != blockItem.getType())
                return false;

            ItemMeta meta = stack.getItemMeta();

            if (meta == null) {
                return false;
            }

            String name = CustomBlockKey.holder(meta).getName();

            return name != null && name.equals(block.getName());
        }, blockItem, DefaultPermissions.BDC.QUICKSELECT_CREATIVE);
    }
}
