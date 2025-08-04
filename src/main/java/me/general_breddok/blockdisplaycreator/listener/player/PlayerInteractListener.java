package me.general_breddok.blockdisplaycreator.listener.player;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
import me.general_breddok.blockdisplaycreator.custom.AutomaticCommandDisplaySummoner;
import me.general_breddok.blockdisplaycreator.custom.block.*;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.file.config.value.BooleanConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.LongConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.StringConfigValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.PlayerSkinBase64Placeholder;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.world.guard.BDCRegionFlags;
import me.general_breddok.blockdisplaycreator.world.guard.WorldGuardChecker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerInteractListener implements Listener {

    private ServiceManager<String, CustomBlockService> serviceManager;

    public PlayerInteractListener(ServiceManager<String, CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }

    @EventHandler
    public void onPlayerCommandSend(AsyncPlayerChatEvent event) {
        event.setMessage(new PlayerSkinBase64Placeholder(event.getPlayer()).applyPlaceholders(event.getMessage()));
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        BlockFace blockFace = event.getBlockFace();
        Block clickedBlock = event.getClickedBlock();
        CustomBlockRotation customBlockRotation = new BDCCustomBlockRotation(blockFace, EntityRotation.toOppositeYaw(player.getLocation().getYaw()));

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || clickedBlock == null)
            return;

        if (player.getGameMode() == GameMode.ADVENTURE)
            return;

        Location blockLocation = clickedBlock.getLocation()
                .add(blockFace.getDirection())
                .add(0.5, 0, 0.5);

        if (clickedBlock.getType().isInteractable() && !player.isSneaking())
            return;

        if (item == null || item.getItemMeta() == null)
            return;

        String blockName = CustomBlockKey.holder(item.getItemMeta()).getName();

        if (blockName == null)
            return;

        if (LongConfigValue.MAX_ENTITIES_PER_CHUNK > 0) {
            if (blockLocation.getChunk().getEntities().length > LongConfigValue.MAX_ENTITIES_PER_CHUNK) {
                ChatUtil.sendMessage(player, StringConfigValue.MAX_ENTITIES_PER_CHUNK);
                return;
            }
        }

        if (!BooleanConfigValue.BLOCKS_PLACEMENT) {
            if (!player.hasPermission(DefaultPermissions.BDC.CustomBlock.BLOCKS_PLACEMENT)) {
                ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_PLACE);
                return;
            }
        }


        CustomBlockService customBlockService = getService(item);

        AbstractCustomBlock abstractCustomBlock = customBlockService.getStorage().getAbstractCustomBlock(blockName);

        if (abstractCustomBlock == null) {
            ChatUtil.sendMessage(player, "&cError, no block %s in storage.", blockName);
            return;
        }


        if (checkAccess(blockLocation, player, abstractCustomBlock))
            return;

        if (abstractCustomBlock.getSaveSystem().equals("item")) {
            configureDataFromItem(item, blockLocation, abstractCustomBlock);
        }


        CustomBlock customBlock = null;
        try {
            customBlock = customBlockService.placeBlock(abstractCustomBlock, blockLocation, customBlockRotation, player);
        } catch (IllegalArgumentException ignore) {
        }

        if (customBlock != null && !(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    @NotNull
    private CustomBlockService getService(ItemStack item) {
        String serviceClassName = CustomBlockKey.holder(item.getItemMeta()).getServiceClass();

        serviceClassName = DeprecatedFeatureAdapter.checkMissingServiceClass(serviceClassName);

        CustomBlockService customBlockService = serviceManager.getService(serviceClassName);

        if (customBlockService == null) {
            throw new UnregisteredServiceException("Service " + serviceClassName + " is not registered", serviceClassName);
        }
        return customBlockService;
    }

    private static void configureDataFromItem(ItemStack item, Location blockLocation, AbstractCustomBlock abstractCustomBlock) {
        PersistentData<CommandLine[]> commandListPD = new PersistentData<>(item.getItemMeta(), TypeTokens.COMMAND_ARRAY);

        CustomBlockData customBlockData = new CustomBlockData(blockLocation.getBlock(), BlockDisplayCreator.getInstance());
        ItemStack cloned = item.clone();
        cloned.setAmount(1);
        customBlockData.set(CustomBlockKey.ITEM, PersistentDataTypes.ITEM, cloned);


        if (commandListPD.has(CustomBlockKey.DISPLAY_SPAWN_COMMAND)) {
            CommandLine[] commandLines = commandListPD.get(CustomBlockKey.DISPLAY_SPAWN_COMMAND);

            customBlockData.set(CustomBlockKey.DISPLAY_SPAWN_COMMAND, PersistentDataTypes.COMMAND_ARRAY, commandLines);

            GroupSummoner<Display> displaySummoner = abstractCustomBlock.getDisplaySummoner();
            if (displaySummoner instanceof AutomaticCommandDisplaySummoner automaticCommandDisplaySummoner) {
                automaticCommandDisplaySummoner.setCommands(Arrays.stream(commandLines).collect(Collectors.toCollection(ArrayList::new)));
            }
        }
    }

    private static boolean checkAccess(Location blockLocation, Player player, AbstractCustomBlock abstractCustomBlock) {
        if (BlockDisplayCreator.getWorldGuard() != null) {
            if (!WorldGuardChecker.checkWGAccess(blockLocation, BDCRegionFlags.PLACE_CB, player)) {
                ChatUtil.sendMessage(player, StringConfigValue.REGION_DENIED_PLACE);
                return true;
            }
        }

        CustomBlockPermissions permissions = abstractCustomBlock.getPermissions();

        if (permissions != null && !permissions.hasPermissions(player, CustomBlockPermissions.Type.PLACE)) {
            ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_PLACE);
            return true;
        }
        return false;
    }
}
