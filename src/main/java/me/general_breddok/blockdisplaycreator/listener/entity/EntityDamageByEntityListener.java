package me.general_breddok.blockdisplaycreator.listener.entity;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class EntityDamageByEntityListener implements Listener {

    private ServiceManager<String, CustomBlockService> serviceManager;

    public EntityDamageByEntityListener(ServiceManager<String, CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;

        if (player.getGameMode() == GameMode.ADVENTURE)
            return;

        Entity entity = event.getEntity();

        if (!(entity instanceof Interaction) && !(entity instanceof Shulker))
            return;

        String blockName = CustomBlockKey.holder(entity).getName();

        if (blockName == null)
            return;

        if (!BooleanConfigValue.BLOCKS_DESTRUCTION) {
            if (!player.hasPermission(DefaultPermissions.BDC.CustomBlock.BLOCKS_DESTRUCTION)) {
                ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_BREAK);
                return;
            }
        }

        String serviceClassName = CustomBlockKey.holder(entity).getServiceClass();

        serviceClassName = DeprecatedFeatureAdapter.checkMissingServiceClass(serviceClassName);

        CustomBlockService customBlockService = serviceManager.getService(serviceClassName);

        if (customBlockService == null) {
            throw new UnregisteredServiceException("Service " + serviceClassName + " is not registered", serviceClassName);
        }


        CustomBlock customBlock;

        if (entity instanceof Interaction interaction) {
            customBlock = customBlockService.getCustomBlock(interaction);
        } else {
            Shulker collision = (Shulker) entity;
            customBlock = customBlockService.getCustomBlock(collision);
        }


        if (customBlock == null) {
            ChatUtil.sendMessage(player, "&cError, block %s not found.", blockName);
            return;
        }

        if (BlockDisplayCreator.getWorldGuard() != null) {
            if (!WorldGuardChecker.checkWGAccess(customBlock.getLocation(), BDCRegionFlags.BREAK_CB, player)) {
                ChatUtil.sendMessage(player, StringConfigValue.REGION_DENIED_BREAK);
                return;
            }
        }


        CustomBlockPermissions permissions = customBlock.getPermissions();

        if (permissions != null) {
            if (!permissions.hasPermissions(player, CustomBlockPermissions.Type.BREAK)) {
                ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_BREAK);
                return;
            }
        }


        CustomBlockOption[] options = new CustomBlockOption[5];

        if (!(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
            options[0] = CustomBlockBreakOption.DROP_ITEM;
        }

        customBlockService.breakBlock(customBlock, player, options);
    }
}
