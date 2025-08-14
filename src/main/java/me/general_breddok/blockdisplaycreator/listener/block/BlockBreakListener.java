package me.general_breddok.blockdisplaycreator.listener.block;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockPermissions;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockBreakOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import me.general_breddok.blockdisplaycreator.file.config.value.BooleanConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.StringConfigValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.world.guard.BDCRegionFlags;
import me.general_breddok.blockdisplaycreator.world.guard.WorldGuardChecker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataType;

public class BlockBreakListener implements Listener {

    private ServiceManager<String, CustomBlockService> serviceManager;

    public BlockBreakListener(ServiceManager<String, CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        CustomBlockData customBlockData = new CustomBlockData(block, BlockDisplayCreator.getInstance());
        String blockName = customBlockData.get(CustomBlockKey.NAME, PersistentDataType.STRING);

        if (blockName == null)
            return;

        if (!BooleanConfigValue.BLOCKS_DESTRUCTION) {
            if (!player.hasPermission(DefaultPermissions.BDC.CustomBlock.BLOCKS_DESTRUCTION)) {
                ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_BREAK);
                return;
            }
        }

        String serviceClassName = customBlockData.get(CustomBlockKey.SERVICE_CLASS, PersistentDataType.STRING);

        serviceClassName = DeprecatedFeatureAdapter.checkMissingServiceClass(serviceClassName);

        CustomBlockService customBlockService = serviceManager.getService(serviceClassName);

        if (customBlockService == null) {
            throw new UnregisteredServiceException("Custom block service " + serviceClassName + " is not registered", serviceClassName);
        }

        CustomBlock customBlock = customBlockService.getCustomBlock(blockLocation);

        if (customBlock == null) {
            ChatUtil.sendMessage(player, "&cError, custom block %s not found.", blockName);
            event.setCancelled(true);
            return;
        }

        if (checkAccess(player, customBlock)) return;


        event.setDropItems(false);

        CustomBlockOption[] options = new CustomBlockOption[5];

        if (player.getGameMode() != GameMode.CREATIVE) {
            options[0] = CustomBlockBreakOption.DROP_ITEM;
        }

        try {
            customBlockService.breakBlock(customBlock, player, options);
        } catch (IllegalArgumentException ignore) {
        }
    }

    private static boolean checkAccess(Player player, CustomBlock customBlock) {
        if (BlockDisplayCreator.getWorldGuard() != null) {
            if (!WorldGuardChecker.checkWGAccess(customBlock.getLocation(), BDCRegionFlags.BREAK_CB, player)) {
                ChatUtil.sendMessage(player, StringConfigValue.REGION_DENIED_BREAK);
                return true;
            }
        }

        CustomBlockPermissions permissions = customBlock.getPermissions();

        if (permissions != null && !permissions.hasPermissions(player, CustomBlockPermissions.Type.BREAK)) {
            ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_BREAK);
            return true;
        }
        return false;
    }
}
