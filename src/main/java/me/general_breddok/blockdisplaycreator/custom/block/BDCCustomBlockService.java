package me.general_breddok.blockdisplaycreator.custom.block;

import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
import me.general_breddok.blockdisplaycreator.custom.AutomaticCommandDisplaySummoner;
import me.general_breddok.blockdisplaycreator.custom.CommandBundle;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockBreakOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockPlaceOption;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.data.manager.TypeTokens;
import me.general_breddok.blockdisplaycreator.data.persistent.PersistentData;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.event.custom.block.CustomBlockBreakEvent;
import me.general_breddok.blockdisplaycreator.event.custom.block.CustomBlockPlaceEvent;
import me.general_breddok.blockdisplaycreator.placeholder.universal.CustomBlockPlaceholder;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.EventUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class BDCCustomBlockService implements CustomBlockService {
    CustomBlockStorage storage;

    public BDCCustomBlockService(CustomBlockStorage storage) {
        this.storage = storage;
    }

    @Override
    @NotNull
    public CustomBlockStorage getStorage() {
        return storage;
    }

    @Override
    public boolean isCustomBlockOnLocation(@NotNull Location location, Object... data) {
        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        return customBlockData.has(CustomBlockKey.NAME) && customBlockData.has(CustomBlockKey.BLOCK_ROTATION) && customBlockData.has(CustomBlockKey.DISPLAY_UUID) && customBlockData.has(CustomBlockKey.CUSTOM_BLOCK_UUID);
    }

    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Location location, Object... data) {
        if (!isCustomBlockOnLocation(location)) {
            return null;
        }

        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        String blockName = customBlockData.get(CustomBlockKey.NAME, PersistentDataType.STRING);

        AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(blockName);

        if (abstractCustomBlock == null) {
            return null;
        }

        List<Display> displays = new ArrayList<>();
        List<Interaction> interactions = new ArrayList<>();
        List<Shulker> collisions = new ArrayList<>();


        UUID[] vehiclesUuids = customBlockData.get(CustomBlockKey.DISPLAY_UUID, PersistentDataTypes.UUID_ARRAY);

        for (UUID vehiclesUuid : vehiclesUuids) {
            Entity displayEntity = Bukkit.getEntity(vehiclesUuid);

            if (displayEntity == null) {
                continue;
            }

            Display display = (Display) displayEntity;
            displays.addAll(display.getPassengers().stream().map(Display.class::cast).toList());
            displays.add(display);
        }


        UUID[] interactionUuids = customBlockData.get(CustomBlockKey.INTERACTION_UUID, PersistentDataTypes.UUID_ARRAY);

        if (interactionUuids != null) {
            for (UUID interactionUuid : interactionUuids) {
                Entity interactionEntity = Bukkit.getEntity(interactionUuid);

                if (interactionEntity != null) {
                    interactions.add((Interaction) interactionEntity);
                }
            }
        }



        UUID[] collisionUuids = customBlockData.get(CustomBlockKey.COLLISION_UUID, PersistentDataTypes.UUID_ARRAY);

        if (collisionUuids != null) {
            for (UUID collisionUuid : collisionUuids) {
                Entity colisionEntity = Bukkit.getEntity(collisionUuid);

                if (colisionEntity != null) {
                    collisions.add((Shulker) colisionEntity);
                }
            }
        }


        if (abstractCustomBlock.getSaveSystem().equals("item")) {

            GroupSummoner<Display> displaySummoner = abstractCustomBlock.getDisplaySummoner();
            if (displaySummoner instanceof AutomaticCommandDisplaySummoner automaticCommandDisplaySummoner) {
                if (customBlockData.has(CustomBlockKey.DISPLAY_SPAWN_COMMAND)) {
                    List<CommandLine> commandLines = Arrays.stream(customBlockData.get(CustomBlockKey.DISPLAY_SPAWN_COMMAND, PersistentDataTypes.COMMAND_ARRAY)).toList();
                    automaticCommandDisplaySummoner.setCommands(commandLines);
                }
            }

            if (customBlockData.has(CustomBlockKey.ITEM)) {
                ItemStack item = customBlockData.get(CustomBlockKey.ITEM, PersistentDataTypes.ITEM);
                abstractCustomBlock.setItem(item);
            }
        }



        CustomBlockRotation rotation = customBlockData.get(CustomBlockKey.BLOCK_ROTATION, PersistentDataTypes.CUSTOM_BLOCK_ROTATION);
        UUID uuid = customBlockData.get(CustomBlockKey.CUSTOM_BLOCK_UUID, PersistentDataTypes.UUID);

        uuid = DeprecatedFeatureAdapter.checkMissingCustomBlockUUID(uuid);

        return new BDCCustomBlock(abstractCustomBlock, location, rotation, displays, interactions, collisions, uuid);
    }

    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Interaction interaction, Object... data) {
        PersistentDataContainer interactionPDC = interaction.getPersistentDataContainer();
        if (!interactionPDC.has(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION)) {
            return null;
        }

        Location blockLocation = interactionPDC.get(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION);

        CustomBlock customBlock = getCustomBlock(blockLocation);

        return customBlock;
    }

    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Display display, Object... data) {
        PersistentDataContainer interactionPDC = display.getPersistentDataContainer();
        if (!interactionPDC.has(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION)) {
            return null;
        }

        Location blockLocation = interactionPDC.get(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION);

        return getCustomBlock(blockLocation);
    }

    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Shulker collision, Object... data) {
        PersistentDataContainer collisionPDC = collision.getPersistentDataContainer();
        if (!collisionPDC.has(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION)) {
            return null;
        }

        Location blockLocation = collisionPDC.get(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION);

        return getCustomBlock(blockLocation);
    }


    @Override
    public CustomBlock placeBlock(@NotNull AbstractCustomBlock abstractCustomBlock, @NotNull Location location, @NotNull CustomBlockRotation rotation, @Nullable Player player, CustomBlockOption... options) {
        Material centralMaterial = abstractCustomBlock.getCentralMaterial();
        Block block = location.getBlock();
        Material originalType = block.getType();

        boolean replaceCustomBlock = false;
        boolean replaceIndestructible = false;
        boolean silentPlace = false;
        boolean loadChunk = false;

        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockPlaceOption.REPLACE_CUSTOM_BLOCK) {
                replaceCustomBlock = true;
            } else if (option == CustomBlockPlaceOption.BREAK_INDESTRUCTIBLE_MATERIAL) {
                replaceIndestructible = true;
            } else if (option == CustomBlockPlaceOption.SILENT_PLACE) {
                silentPlace = true;
            } else if (option == CustomBlockPlaceOption.LOAD_CHUNK) {
                loadChunk = true;
            }
        }


        if (!location.getChunk().isLoaded()) {
            if (loadChunk) {
                location.getChunk().load();
            } else {
                return null;
            }
        }


        if (!WorldSelection.isDestructible(originalType)) {
            if (replaceIndestructible) {
                block.setType(centralMaterial);
            } else {
                return null;
            }
        } else {
            block.setType(centralMaterial);
        }


        if (this.isCustomBlockOnLocation(location)) {
            if (replaceCustomBlock) {
                this.breakBlock(this.getCustomBlock(location), player);
            } else {
                return null;
            }
        }



        List<UUID> displayVehicleUuids = new ArrayList<>();

        BlockDisplay centerEntity = location.getWorld().spawn(location, BlockDisplay.class);

        List<Display> displays = abstractCustomBlock.spawnDisplay(location, centerEntity, display -> {
            if (!display.isInsideVehicle()) {
                displayVehicleUuids.add(display.getUniqueId());
            }


            display.setPersistent(true);
            display.setInvulnerable(true);


            rotation.adjustDisplayRotation(display, abstractCustomBlock.getSidesCount());

            CustomBlockKey.holder(display)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location);


            display.addScoreboardTag("custom-block");
            display.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            display.addScoreboardTag("custom-block-display");
            display.addScoreboardTag("custom-block-location:" + location.toVector());
            return true;
        });

        centerEntity.remove();


        if (displays.isEmpty()) {
            block.setType(originalType);
            ChatUtil.log("&6[BlockDisplayCreator] &4Something went wrong, custom block at location " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " in %s world was not placed due to missing display entities. You may have specified the spawn command incorrectly.", location.getWorld().getName());
            return null;
        }




        List<Interaction> interactions = abstractCustomBlock.getConfiguredInteractions().stream().map(configuredInteraction -> {
            Interaction interaction = configuredInteraction.summon(location);
            String configuredInteractionIdentifier = configuredInteraction.getIdentifier();


            rotation.adjustInteractionRotation(interaction, configuredInteraction.getOffset(), abstractCustomBlock.getSidesCount());

            CustomBlockKey.DataHolder dataHolder = CustomBlockKey.holder(interaction);

            dataHolder.setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location);


            if (!configuredInteractionIdentifier.isEmpty()) {
                dataHolder.setInteractionIdentifier(configuredInteractionIdentifier);
            }

            interaction.addScoreboardTag("custom-block");
            interaction.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            interaction.addScoreboardTag("custom-block-interaction");
            interaction.addScoreboardTag("custom-block-interaction-id:" + configuredInteractionIdentifier);
            interaction.addScoreboardTag("custom-block-location:" + location.toVector());

            return interaction;
        }).toList();



        List<Shulker> collisions = abstractCustomBlock.getConfiguredCollisions().stream().map(configuredCollision -> {
            Shulker collision = configuredCollision.summon(location);
            String configuredCollisionIdentifier = configuredCollision.getIdentifier();

            rotation.adjustCollisionRotation(collision, configuredCollision.getOffset(), abstractCustomBlock.getSidesCount());

            CustomBlockKey.DataHolder dataHolder = CustomBlockKey.holder(collision);

            dataHolder.setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location);


            if (!configuredCollisionIdentifier.isEmpty()) {
                dataHolder.setCollisionIdentifier(configuredCollisionIdentifier);
            }


            collision.addScoreboardTag("custom-block");
            collision.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            collision.addScoreboardTag("custom-block-collision");
            collision.addScoreboardTag("custom-block-collision-id:" + configuredCollisionIdentifier);
            collision.addScoreboardTag("custom-block-location:" + location.toVector());

            return collision;
        }).toList();




        CustomBlock rawCustomBlock = new BDCCustomBlock(abstractCustomBlock, location, rotation, displays, interactions, collisions, UUID.randomUUID());

        CustomBlockData customBlockData = createCustomBlockData(rawCustomBlock, displayVehicleUuids);

        if (!triggerCustomBlockPlaceEvent(rawCustomBlock, player, customBlockData)) {
            return null;
        }

        if (!silentPlace) {
            abstractCustomBlock.playPlaceSound(location);
        }

        CustomBlockStageSettings stageSettings = abstractCustomBlock.getStageSettings();
        if (stageSettings != null) {
            CommandBundle placeCommandBundle = stageSettings.getPlaceCommandBundle();
            if (placeCommandBundle != null) {
                placeCommandBundle.execute(player, new CustomBlockPlaceholder(rawCustomBlock));
            }
        }

        return rawCustomBlock;
    }

    @Override
    public boolean breakBlock(@NotNull CustomBlock customBlock, @Nullable Player player, CustomBlockOption... options) {
        Location location = customBlock.getLocation();

        if (!isCustomBlockOnLocation(location)) {
            return false;
        }

        boolean dropItem = false;
        boolean silentBreak = false;

        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockBreakOption.DROP_ITEM) {
                dropItem = true;
            } else if (option == CustomBlockBreakOption.SILENT_BREAK) {
                silentBreak = true;
            }
        }

        CustomBlockStageSettings stageSettings = customBlock.getStageSettings();
        if (stageSettings != null) {
            CommandBundle breakCommandBundle = stageSettings.getBreakCommandBundle();
            if (breakCommandBundle != null) {
                breakCommandBundle.execute(player, new CustomBlockPlaceholder[]{new CustomBlockPlaceholder(customBlock)});
            }
        }


        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        if (!triggerCustomBlockBreakEvent(customBlock, player)) {
            return false;
        }

        customBlock.getDisplays().forEach(Entity::remove);
        customBlock.getInteractions().forEach(Entity::remove);
        customBlock.getCollisions().forEach(collision -> {
            Entity vehicle = collision.getVehicle();
            collision.remove();

            if (vehicle != null) {
                vehicle.remove();
            }
        });

        location.getBlock().setType(Material.AIR);

        clearCustomBlockData(customBlockData);

        if (dropItem) {
            location.getWorld().dropItemNaturally(location, customBlock.getItem());
        }

        if (!silentBreak) {
            customBlock.playBreakSound(location);
        }

        return true;
    }

    protected static CustomBlockData createCustomBlockData(CustomBlock customBlock, List<UUID> displayVehicleUuids) {
        CustomBlockData customBlockData = new CustomBlockData(customBlock.getLocation().getBlock(), BlockDisplayCreator.getInstance());
        customBlockData.set(CustomBlockKey.NAME, PersistentDataType.STRING, customBlock.getName());
        customBlockData.set(CustomBlockKey.DISPLAY_UUID, PersistentDataTypes.UUID_ARRAY, displayVehicleUuids.toArray(UUID[]::new));
        customBlockData.set(CustomBlockKey.INTERACTION_UUID, PersistentDataTypes.UUID_ARRAY, customBlock.getInteractions().stream().map(Entity::getUniqueId).toList().toArray(UUID[]::new));
        customBlockData.set(CustomBlockKey.COLLISION_UUID, PersistentDataTypes.UUID_ARRAY, customBlock.getCollisions().stream().map(Entity::getUniqueId).toList().toArray(UUID[]::new));
        customBlockData.set(CustomBlockKey.BLOCK_ROTATION, PersistentDataTypes.CUSTOM_BLOCK_ROTATION, customBlock.getRotation());
        customBlockData.set(CustomBlockKey.CUSTOM_BLOCK_UUID, PersistentDataTypes.UUID, customBlock.getUuid());
        if (customBlock.getSaveSystem().equals("item")) {
            GroupSummoner<Display> displaySummoner = customBlock.getDisplaySummoner();
            if (displaySummoner instanceof AutomaticCommandDisplaySummoner automaticCommandDisplaySummoner) {
                customBlockData.set(CustomBlockKey.DISPLAY_SPAWN_COMMAND, PersistentDataTypes.COMMAND_ARRAY, automaticCommandDisplaySummoner.getCommands().toArray(CommandLine[]::new));
            }
        }
        return customBlockData;
    }

    private static boolean triggerCustomBlockPlaceEvent(CustomBlock customBlock, Player player, CustomBlockData customBlockData) {
        CustomBlockPlaceEvent customBlockPlaceEvent = new CustomBlockPlaceEvent(customBlock, player);
        if (!EventUtil.call(customBlockPlaceEvent)) {
            customBlock.getDisplays().forEach(Entity::remove);
            customBlock.getInteractions().forEach(Entity::remove);
            customBlock.getCollisions().forEach(Entity::remove);
            clearCustomBlockData(customBlockData);
            customBlock.getLocation().getBlock().setType(Material.AIR);
            return false;
        }
        return true;
    }

    private static boolean triggerCustomBlockBreakEvent(CustomBlock customBlock, Player player) {
        CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(customBlock, player);
        return EventUtil.call(customBlockBreakEvent);
    }

    protected static void clearCustomBlockData(CustomBlockData customBlockData) {
        customBlockData.remove(CustomBlockKey.NAME);
        customBlockData.remove(CustomBlockKey.DISPLAY_UUID);
        customBlockData.remove(CustomBlockKey.INTERACTION_UUID);
        customBlockData.remove(CustomBlockKey.COLLISION_UUID);
        customBlockData.remove(CustomBlockKey.BLOCK_ROTATION);
        customBlockData.remove(CustomBlockKey.CUSTOM_BLOCK_UUID);
    }
}
