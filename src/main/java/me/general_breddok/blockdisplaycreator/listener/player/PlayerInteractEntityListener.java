package me.general_breddok.blockdisplaycreator.listener.player;

import com.google.common.base.Preconditions;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredInteraction;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockKey;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockPermissions;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlockService;
import me.general_breddok.blockdisplaycreator.event.custom.block.CustomBlockInteractEvent;
import me.general_breddok.blockdisplaycreator.file.config.value.BooleanConfigValue;
import me.general_breddok.blockdisplaycreator.file.config.value.StringConfigValue;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.placeholder.universal.CustomBlockPlaceholder;
import me.general_breddok.blockdisplaycreator.placeholder.universal.InteractionPlaceholder;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.EventUtil;
import me.general_breddok.blockdisplaycreator.world.guard.BDCRegionFlags;
import me.general_breddok.blockdisplaycreator.world.guard.WorldGuardChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.function.Predicate;

public class PlayerInteractEntityListener implements Listener {

    private ServiceManager<String, CustomBlockService> serviceManager;

    public PlayerInteractEntityListener(ServiceManager<String, CustomBlockService> serviceManager) {
        this.serviceManager = serviceManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();


        if (!(event.getRightClicked() instanceof Interaction interaction))
            return;

        String blockName = CustomBlockKey.holder(interaction).getName();

        if (blockName == null)
            return;

        if (!BooleanConfigValue.BLOCKS_INTERACTION) {
            if (!player.hasPermission(DefaultPermissions.BDC.CustomBlock.BLOCKS_INTERACTION)) {
                ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_INTERACT);
                return;
            }
        }

        String serviceClassName = CustomBlockKey.holder(interaction).getServiceClass();

        serviceClassName = DeprecatedFeatureAdapter.checkMissingServiceClass(serviceClassName);

        CustomBlockService customBlockService = serviceManager.getService(serviceClassName);

        if (customBlockService == null) {
            throw new UnregisteredServiceException("Service " + serviceClassName + " is not registered", serviceClassName);
        }

        CustomBlock customBlock = customBlockService.getCustomBlock(interaction);

        if (customBlock == null) {
            ChatUtil.sendMessage(player, "&cError, block %s not found.", blockName);
            return;
        }

        event.setCancelled(true);

        if (checkAccess(customBlock, player))
            return;

        ConfiguredInteraction configuredInteraction = customBlock.getConfiguredInteraction(interaction);

        CustomBlockInteractEvent customBlockInteractEvent = new CustomBlockInteractEvent(customBlock, player, interaction, configuredInteraction, hand);

        if (!EventUtil.call(customBlockInteractEvent)) {
            return;
        }

        if (configuredInteraction == null) {
            return;
        }

        customBlock.handleClick(configuredInteraction, interaction, player, new CustomBlockPlaceholder(customBlock), new InteractionPlaceholder(interaction));


        /*
        RayTraceResult rayTraceResult = world.rayTraceEntities(
                eyeLocation,
                direction,
                6,
                entity -> !entity.getUniqueId().equals(player.getUniqueId())
        );

        ChatUtil.sendMessage(player, "Is rayTraceResult null? - " + (rayTraceResult == null));

        if (rayTraceResult == null) {
            return;
        }

        Vector hitPosition = rayTraceResult.getHitPosition();

        BlockFace blockFace = determineEntityFace(interaction, hitPosition);

        ChatUtil.sendMessage(player, "&1hitPosition is " + hitPosition);
        ChatUtil.sendMessage(player, "&2blockFace is " + blockFace);
        ChatUtil.sendMessage(player, "&3hitEntity is " + rayTraceResult.getHitEntity().getType());
        ChatUtil.sendMessage(player, "&4BoundingBox: " + interaction.getBoundingBox());


        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, player.getInventory().getItem(hand), world.getBlockAt(hitPosition.getBlockX(), hitPosition.getBlockY(), hitPosition.getBlockZ()), blockFace, hand);
        EventUtil.call(playerInteractEvent);*/
    }


    private static boolean checkAccess(CustomBlock customBlock, Player player) {
        if (BlockDisplayCreator.getWorldGuard() != null) {
            if (!WorldGuardChecker.checkWGAccess(customBlock.getLocation(), BDCRegionFlags.INTERACT_CB, player)) {
                ChatUtil.sendMessage(player, StringConfigValue.REGION_DENIED_INTERACT);
                return true;
            }
        }

        CustomBlockPermissions permissions = customBlock.getPermissions();

        if (permissions != null && !permissions.hasPermissions(player, CustomBlockPermissions.Type.INTERACT)) {
            ChatUtil.sendMessage(player, StringConfigValue.PERMISSION_DENIED_INTERACT);
            return true;
        }

        return false;
    }


    public BlockFace determineEntityFace(Interaction entity, Vector hitPos) {
        BoundingBox box = entity.getBoundingBox();

        double dx = hitPos.getX() - box.getCenterX();
        double dy = hitPos.getY() - box.getCenterY();
        double dz = hitPos.getZ() - box.getCenterZ();

        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        double absDz = Math.abs(dz);

        if (absDy > absDx && absDy > absDz) {
            return dy > 0 ? BlockFace.UP : BlockFace.DOWN;
        } else if (absDx > absDz) {
            return dx > 0 ? BlockFace.EAST : BlockFace.WEST;
        } else {
            return dz > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
        }
    }

    private RayTraceResult rayTraceEntities(World world, Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(world.equals(start.getWorld()), "Location start cannot be in a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared());

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector startPos = start.toVector();
        Vector dir = direction.clone().normalize().multiply(maxDistance);
        BoundingBox aabb = BoundingBox.of(startPos, startPos).expandDirectional(dir).expand(raySize);
        Collection<Entity> entities = world.getNearbyEntities(aabb, filter);

        Entity nearestHitEntity = null;
        RayTraceResult nearestHitResult = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        ChatUtil.log("1");
        for (Entity entity : entities) {
            BoundingBox boundingBox = entity.getBoundingBox().expand(raySize);
            RayTraceResult hitResult = boundingBox.rayTrace(startPos, direction, maxDistance);
            ChatUtil.log("2");
            if (hitResult != null) {
                double distanceSq = startPos.distanceSquared(hitResult.getHitPosition());
                ChatUtil.log("3");
                if (distanceSq < nearestDistanceSq) {
                    ChatUtil.log("4");
                    nearestHitEntity = entity;
                    nearestHitResult = hitResult;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return (nearestHitEntity == null) ? null : new RayTraceResult(nearestHitResult.getHitPosition(), nearestHitEntity, nearestHitResult.getHitBlockFace());
    }
}
