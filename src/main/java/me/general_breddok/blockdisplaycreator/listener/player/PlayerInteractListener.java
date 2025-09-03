package me.general_breddok.blockdisplaycreator.listener.player;

import com.jeff_media.customblockdata.CustomBlockData;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.common.BDCDependentPluginsManager;
import me.general_breddok.blockdisplaycreator.custom.AutomaticCommandDisplaySummoner;
import me.general_breddok.blockdisplaycreator.custom.block.*;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.file.config.value.BooleanConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.LongConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.StringMessagesValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.rotation.BlockRotation;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import me.general_breddok.blockdisplaycreator.world.guard.WGRegionFlags;
import me.general_breddok.blockdisplaycreator.world.guard.WGRegionAccessChecker;
import me.general_breddok.blockdisplaycreator.world.skyblock.superior.SSBIslandPrivileges;
import me.general_breddok.blockdisplaycreator.world.skyblock.superior.SSBIslandAccessChecker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerInteractListener implements Listener {

    private ServiceManager<String, CustomBlockService> serviceManager;

    public PlayerInteractListener(ServiceManager<String, CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        BlockFace blockFace = event.getBlockFace();
        Block clickedBlock = event.getClickedBlock();

        if (player.getGameMode() == GameMode.ADVENTURE)
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || clickedBlock == null)
            return;

        if (item == null || item.getItemMeta() == null)
            return;

        Location blockLocation = clickedBlock.getLocation()
                .add(0.5, 0, 0.5);

        if (!(WorldSelection.isEphemeral(clickedBlock) || WorldSelection.isSingleLayerSnow(clickedBlock))) {
            blockLocation.add(blockFace.getDirection());
        }

        if (WorldSelection.isInteractable(clickedBlock) && !player.isSneaking())
            return;


        String blockName = CustomBlockKey.holder(item.getItemMeta()).getName();

        if (blockName == null)
            return;

        event.setCancelled(true);

        if (LongConfigValue.MAX_ENTITIES_PER_CHUNK > 0) {
            if (blockLocation.getChunk().getEntities().length > LongConfigValue.MAX_ENTITIES_PER_CHUNK) {
                ChatUtil.sendMessage(player, StringMessagesValue.MAX_ENTITIES_PER_CHUNK);
                return;
            }
        }

        if (!BooleanConfigValue.BLOCKS_PLACEMENT) {
            if (!player.hasPermission(DefaultPermissions.BDC.CustomBlock.BLOCKS_PLACEMENT)) {
                ChatUtil.sendMessage(player, StringMessagesValue.PERMISSION_DENIED_PLACE);
                return;
            }
        }


        CustomBlockService customBlockService = CustomBlockService.getService(serviceManager, item.getItemMeta());

        AbstractCustomBlock abstractCustomBlock = customBlockService.getStorage().getAbstractCustomBlock(blockName);

        if (abstractCustomBlock == null) {
            ChatUtil.sendMessage(player, StringMessagesValue.CUSTOM_BLOCK_NOT_FOUND.replace("%customblock_name%", blockName));
            return;
        }


        if (!checkAccess(blockLocation, player, abstractCustomBlock)) {
            return;
        }

        CustomBlockStageSettings stageSettings = abstractCustomBlock.getStageSettings();
        if (stageSettings != null) {
            CustomBlockPlaceSettings.PlacementMode placementMode = stageSettings.getPlaceSettings().getPlacementMode();
            blockFace = resolvePlacementFace(placementMode, blockFace, player);

            if (blockFace == null) {
                return;
            }
        }

        if (abstractCustomBlock.getSaveSystem().equals("item")) {
            configureDataFromItem(item, blockLocation, abstractCustomBlock);
        }

        CustomBlockRotation customBlockRotation = new BDCCustomBlockRotation(blockFace, EntityRotation.toOppositeYaw(player.getLocation().getYaw()));
        CustomBlock customBlock = null;
        try {
            customBlock = customBlockService.placeBlock(abstractCustomBlock, blockLocation, customBlockRotation, player);
        } catch (IllegalArgumentException ignore) {
        }

        if (customBlock != null && !(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
            item.setAmount(item.getAmount() - 1);
        }
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
        BDCDependentPluginsManager dependentPluginManager = BlockDisplayCreator.getInstance().getDependentPluginsManager();

        if (dependentPluginManager.isWorldGuardAvailable()) {
            if (!WGRegionAccessChecker.checkRegionAccess(blockLocation, WGRegionFlags.PLACE_CB, player)) {
                ChatUtil.sendMessage(player, StringMessagesValue.REGION_DENIED_PLACE);
                return false;
            }
        }

        if (dependentPluginManager.isSuperiorSkyblockAvailable()) {
            if (!SSBIslandAccessChecker.checkIslandAccess(blockLocation, SSBIslandPrivileges.PLACE_CB, player)) {
                ChatUtil.sendMessage(player, StringMessagesValue.ISLAND_DENIED_PLACE);
                return false;
            }
        }

        CustomBlockPermissions permissions = abstractCustomBlock.getPermissions();

        if (permissions != null && !permissions.hasPermissions(player, CustomBlockPermissions.Type.PLACE)) {
            ChatUtil.sendMessage(player, StringMessagesValue.PERMISSION_DENIED_PLACE);
            return false;
        }
        return true;
    }

    private BlockFace resolvePlacementFace(CustomBlockPlaceSettings.PlacementMode placementMode, BlockFace blockFace, Player player) {
        if (placementMode == null) {
            return blockFace;
        }

        switch (placementMode) {
            case HORIZONTAL_ONLY:
                if (BlockRotation.isVerticalFace(blockFace)) {
                    return null;
                }
                break;

            case VERTICAL_ONLY:
                if (!BlockRotation.isVerticalFace(blockFace)) {
                    return null;
                }
                break;

            case VERTICAL_WITH_HORIZONTAL:
                if (!BlockRotation.isVerticalFace(blockFace)) {
                    return BlockFace.UP;
                }
                break;
            case HORIZONTAL_WITH_VERTICAL:
                if (BlockRotation.isVerticalFace(blockFace)) {
                    return BlockRotation.getCardinalFaceFromYaw(
                            EntityRotation.toOppositeYaw(
                                    player.getLocation().getYaw()
                            )
                    );
                }
                break;
            default:
                break;
        }

        return blockFace;
    }
}
