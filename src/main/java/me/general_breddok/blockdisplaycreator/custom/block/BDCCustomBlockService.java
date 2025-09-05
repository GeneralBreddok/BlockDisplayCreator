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
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.entity.display.TranslationVectorAdjustable;
import me.general_breddok.blockdisplaycreator.event.custom.block.CustomBlockBreakEvent;
import me.general_breddok.blockdisplaycreator.event.custom.block.CustomBlockPlaceEvent;
import me.general_breddok.blockdisplaycreator.file.config.value.StringMessagesValue;
import me.general_breddok.blockdisplaycreator.placeholder.universal.CustomBlockPlaceholder;
import me.general_breddok.blockdisplaycreator.placeholder.universal.LocationPlaceholder;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.rotation.EntityRotation;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.EventUtil;
import me.general_breddok.blockdisplaycreator.util.ItemUtil;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implementation of {@link CustomBlockService} for managing custom blocks.
 * <p>
 * Handles storage and retrieval of custom block data using {@link CustomBlockData},
 * allowing block data to be stored per location without relying on a database.
 * </p>
 */
@Getter
public class BDCCustomBlockService implements CustomBlockService {

    /**
     * Storage for all custom blocks managed by this service.
     */
    CustomBlockStorage storage;

    /**
     * Creates a new instance of the service with the specified storage.
     *
     * @param storage the storage instance to use for custom blocks
     */
    public BDCCustomBlockService(CustomBlockStorage storage) {
        this.storage = storage;
    }

    public static void applyDisplayTranslationRotation(@NotNull GroupSummoner<Display> displaySummoner, Display display) {
        if (displaySummoner instanceof TranslationVectorAdjustable translationVectorAdjustable) {
            DirectedVector translation = translationVectorAdjustable.getTranslation();
            if (translation != null) {
                EntityRotation displayRotation = new EntityRotation(display);
                displayRotation.addRotation(translation.getYaw(), translation.getPitch());
                displayRotation.applyToEntity(display);
            }
        }
    }

    /**
     * Creates a {@link CustomBlockData} from its location and stores all block data there
     *
     * @param customBlock         the custom block to create data for
     * @param displayVehicleUuids list of UUIDs of display vehicles associated with the custom block
     * @return a {@link CustomBlockData} instance containing the custom block's data
     */
    public static CustomBlockData createCustomBlockData(CustomBlock customBlock, List<UUID> displayVehicleUuids) {
        CustomBlockData customBlockData = new CustomBlockData(customBlock.getLocation().getBlock(), BlockDisplayCreator.getInstance());
        customBlockData.set(CustomBlockKey.NAME, PersistentDataType.STRING, customBlock.getName());
        customBlockData.set(CustomBlockKey.SERVICE_CLASS, PersistentDataType.STRING, BDCCustomBlockService.class.getName());
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

    /**
     * Triggers the {@link CustomBlockPlaceEvent} and handles cancellation
     *
     * @param customBlock     the custom block that was placed
     * @param player          the player who placed the block (nullable)
     * @param customBlockData the custom block data associated with the block's location
     * @return true if the event was not cancelled, false if it was
     */
    private static boolean triggerCustomBlockPlaceEvent(CustomBlock customBlock, Player player, CustomBlockData customBlockData) {
        CustomBlockPlaceEvent customBlockPlaceEvent = new CustomBlockPlaceEvent(customBlock, player);
        // If the event is canceled, remove all entities and clear the block data
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

    /**
     * Triggers the {@link CustomBlockBreakEvent} and handles cancellation
     *
     * @param customBlock the custom block that is being broken
     * @param player      the player who is breaking the block (nullable)
     * @return true if the event was not cancelled, false if it was
     */
    private static boolean triggerCustomBlockBreakEvent(CustomBlock customBlock, Player player) {
        CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(customBlock, player);
        return EventUtil.call(customBlockBreakEvent);
    }

    /**
     * Clears all custom block data from the provided {@link CustomBlockData} instance
     *
     * @param customBlockData the custom block data to clear
     */
    public static void clearCustomBlockData(CustomBlockData customBlockData) {
        customBlockData.remove(CustomBlockKey.NAME);
        customBlockData.remove(CustomBlockKey.SERVICE_CLASS);
        customBlockData.remove(CustomBlockKey.DISPLAY_UUID);
        customBlockData.remove(CustomBlockKey.INTERACTION_UUID);
        customBlockData.remove(CustomBlockKey.COLLISION_UUID);
        customBlockData.remove(CustomBlockKey.BLOCK_ROTATION);
        customBlockData.remove(CustomBlockKey.CUSTOM_BLOCK_UUID);
        customBlockData.remove(CustomBlockKey.DISPLAY_SPAWN_COMMAND);

        customBlockData.remove(CustomBlockKey.ITEM);
        customBlockData.remove(CustomBlockKey.DISPLAY_SPAWN_COMMAND);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public CustomBlockStorage getStorage() {
        return storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomBlockOnLocation(@NotNull Location location, Object... data) {
        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        return customBlockData.has(CustomBlockKey.NAME)
                && customBlockData.has(CustomBlockKey.BLOCK_ROTATION)
                && customBlockData.has(CustomBlockKey.DISPLAY_UUID)
                && customBlockData.has(CustomBlockKey.CUSTOM_BLOCK_UUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Location location, Object... data) {
        if (!isCustomBlockOnLocation(location)) {
            return null;
        }

        // Fetch the abstract custom block from storage
        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        String blockName = customBlockData.get(CustomBlockKey.NAME, PersistentDataType.STRING);

        AbstractCustomBlock abstractCustomBlock = storage.getAbstractCustomBlock(blockName);

        if (abstractCustomBlock == null) {
            return null;
        }

        List<Display> displays = new ArrayList<>();
        List<Interaction> interactions = new ArrayList<>();
        List<Shulker> collisions = new ArrayList<>();


        // Fetch display entity vehicles and their passengers
        UUID[] vehiclesUuids = customBlockData.get(CustomBlockKey.DISPLAY_UUID, PersistentDataTypes.UUID_ARRAY);

        for (UUID vehiclesUuid : vehiclesUuids) {
            Entity displayEntity = Bukkit.getEntity(vehiclesUuid);

            // If display entity was killed, or something else happened - skip it
            if (displayEntity == null) {
                continue;
            }

            Display display = (Display) displayEntity;
            displays.addAll(display.getPassengers().stream().map(Display.class::cast).collect(OperationUtil.toArrayList()));
            displays.add(display);
        }


        // Fetch interaction entities
        UUID[] interactionUuids = customBlockData.get(CustomBlockKey.INTERACTION_UUID, PersistentDataTypes.UUID_ARRAY);

        if (interactionUuids != null) {
            for (UUID interactionUuid : interactionUuids) {
                Entity interactionEntity = Bukkit.getEntity(interactionUuid);

                // If interaction entity was killed, or something else happened - skip it
                if (interactionEntity == null) {
                    continue;
                }

                interactions.add((Interaction) interactionEntity);
            }
        }


        // Fetch collision entities
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
                    List<CommandLine> commandLines = Arrays.stream(customBlockData.get(CustomBlockKey.DISPLAY_SPAWN_COMMAND, PersistentDataTypes.COMMAND_ARRAY)).collect(OperationUtil.toArrayList());
                    automaticCommandDisplaySummoner.setCommands(commandLines);
                }
            }

            if (customBlockData.has(CustomBlockKey.ITEM)) {
                ItemStack item = customBlockData.get(CustomBlockKey.ITEM, PersistentDataTypes.ITEM);
                abstractCustomBlock.setItem(item);
            }
        }


        // Fetch rotation
        CustomBlockRotation rotation = customBlockData.get(CustomBlockKey.BLOCK_ROTATION, PersistentDataTypes.CUSTOM_BLOCK_ROTATION);
        // Fetch UUID
        UUID uuid = customBlockData.get(CustomBlockKey.CUSTOM_BLOCK_UUID, PersistentDataTypes.UUID);

        uuid = DeprecatedFeatureAdapter.checkMissingCustomBlockUUID(uuid);

        // Construct and return the custom block
        return new BDCCustomBlock(abstractCustomBlock, location, rotation, displays, interactions, collisions, uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public CustomBlock getCustomBlock(@NotNull Entity entity, Object... data) {
        PersistentDataContainer interactionPDC = entity.getPersistentDataContainer();
        // Check if the entity has the necessary custom block data (location)
        if (!interactionPDC.has(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION)) {
            return null;
        }

        Location blockLocation = interactionPDC.get(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION);

        return getCustomBlock(blockLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomBlock placeBlock(@NotNull AbstractCustomBlock abstractCustomBlock, @NotNull Location location, @NotNull CustomBlockRotation rotation, @Nullable Player player, CustomBlockOption... options) throws IllegalArgumentException {
        Material centralMaterial = abstractCustomBlock.getCentralMaterial();
        World world = location.getWorld();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;
        LocationPlaceholder locationPlaceholder = new LocationPlaceholder(location);

        // Checking if a location has a world
        if (world == null) {
            throw new IllegalArgumentException("Location's world cannot be null");
        }

        // Parsing options
        boolean replaceCustomBlock = false;
        boolean breakSolidMaterial = false;
        boolean silentPlace = false;
        boolean loadChunk = false;

        // Iterate through provided options and set flags accordingly
        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockPlaceOption.REPLACE_CUSTOM_BLOCK) {
                replaceCustomBlock = true;
            } else if (option == CustomBlockPlaceOption.BREAK_SOLID_MATERIAL) {
                breakSolidMaterial = true;
            } else if (option == CustomBlockPlaceOption.SILENT_PLACE) {
                silentPlace = true;
            } else if (option == CustomBlockPlaceOption.LOAD_CHUNK) {
                loadChunk = true;
            }
        }

        // Check if the chunk is loaded on the location
        if (!world.isChunkLoaded(chunkX, chunkZ)) {
            // If the option is set, load the chunk, otherwise throw an error
            if (loadChunk) {
                location.getChunk().load();
            } else {
                throw new IllegalArgumentException(locationPlaceholder.apply(StringMessagesValue.CUSTOM_BLOCK_PLACE_FAIL_REASON_CHUNK_NOT_LOADED));
            }
        }

        Block block = location.getBlock();
        Material originalMaterial = block.getType();

        // Check if a solid material is present at the location
        if (!(WorldSelection.isEphemeral(originalMaterial) || WorldSelection.isSingleLayerSnow(block))) {
            // If the option is set, break the solid material, otherwise throw an error
            if (breakSolidMaterial) {
                block.setType(centralMaterial);
            } else {
                throw new IllegalArgumentException(locationPlaceholder.apply(StringMessagesValue.CUSTOM_BLOCK_PLACE_FAIL_REASON_SOLID_BLOCK));
            }
        } else {
            // Set the central block to the required material
            block.setType(centralMaterial);
        }


        // Check if a custom block is already present at the location
        if (this.isCustomBlockOnLocation(location)) {
            // If the option is set, break the existing custom block, otherwise throw an error
            if (replaceCustomBlock) {
                this.breakBlock(this.getCustomBlock(location), player);
            } else {
                throw new IllegalArgumentException(locationPlaceholder.apply(StringMessagesValue.CUSTOM_BLOCK_PLACE_FAIL_REASON_CUSTOM_BLOCK_PRESENT));
            }
        }


        // Summon a temporary interaction entity to serve as the center for display entities
        Interaction centerEntity = world.spawn(location, Interaction.class);

        List<UUID> displayVehicleUuids = new ArrayList<>();

        // Summon display entities and iterate through them to apply the necessary settings
        List<Display> displays = abstractCustomBlock.spawnDisplay(location, centerEntity, display -> {
            // Store the UUID of the display vehicle if it's not on another vehicle.
            if (!display.isInsideVehicle()) {
                displayVehicleUuids.add(display.getUniqueId());
            }

            // Set persistent and invulnerable so they don't despawn or die
            display.setPersistent(true);
            display.setInvulnerable(true);

            // Adjust display rotation and translation depending on the settings selected
            rotation.adjustDisplayRotation(display, abstractCustomBlock.getSidesCount());
            applyDisplayTranslationRotation(abstractCustomBlock.getDisplaySummoner(), display);

            // Set custom block data for the display entity
            CustomBlockKey.holder(display)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location);

            // Add scoreboard tags for easy identification and use in data packs
            display.addScoreboardTag("custom-block");
            display.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            display.addScoreboardTag("custom-block-display");
            display.addScoreboardTag("custom-block-location:" + location.toVector());
            return true;
        });

        centerEntity.remove();

        // If no display entities were summoned, something went wrong - revert the block change and log an error
        if (displays.isEmpty()) {
            block.setType(originalMaterial);
            ChatUtil.log("&6[BlockDisplayCreator] &4Something went wrong, custom block at location " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " in %s world was not placed due to missing display entities. You may have specified the spawn command incorrectly.", world.getName());
            return null;
        }


        // Summon interaction entities and iterate through them to apply the necessary settings
        List<Interaction> interactions = abstractCustomBlock.getConfiguredInteractions().stream().map(configuredInteraction -> {
            Interaction interaction = configuredInteraction.summon(location);

            // If the interaction failed to summon, skip it
            if (interaction == null) {
                return null;
            }
            // Adjusts the rotation and offset of the interaction depending on the settings selected
            rotation.adjustInteractionRotation(interaction, configuredInteraction.getOffset(), abstractCustomBlock.getSidesCount());

            // Get the interaction identifier that was specified in the block file as its section name.
            String configuredInteractionIdentifier = configuredInteraction.getIdentifier();

            // Set custom block data for the interaction entity
            CustomBlockKey.holder(interaction)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location)
                    .setInteractionIdentifier(configuredInteractionIdentifier);

            // Add scoreboard tags for easy identification and use in data packs
            interaction.addScoreboardTag("custom-block");
            interaction.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            interaction.addScoreboardTag("custom-block-interaction");
            interaction.addScoreboardTag("custom-block-interaction-id:" + configuredInteractionIdentifier);
            interaction.addScoreboardTag("custom-block-location:" + location.toVector());

            return interaction;
        }).filter(Objects::nonNull).collect(OperationUtil.toArrayList());


        // Summon collision entities and iterate through them to apply the necessary settings
        List<Shulker> collisions = abstractCustomBlock.getConfiguredCollisions().stream().map(configuredCollision -> {
            Shulker collision = configuredCollision.summon(location);

            // If the collision failed to summon, skip it
            if (collision == null) {
                return null;
            }

            // Get the collision identifier that was specified in the block file as its section name.
            String configuredCollisionIdentifier = configuredCollision.getIdentifier();

            // Adjusts the rotation and offset of the collision depending on the settings selected
            rotation.adjustCollisionRotation(collision, configuredCollision.getOffset(), abstractCustomBlock.getSidesCount());

            // Set custom block data for the collision entity
            CustomBlockKey.holder(collision)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(abstractCustomBlock.getName())
                    .setLocation(location)
                    .setCollisionIdentifier(configuredCollisionIdentifier);

            // Add scoreboard tags for easy identification and use in data packs
            collision.addScoreboardTag("custom-block");
            collision.addScoreboardTag("custom-block-name:" + abstractCustomBlock.getName());
            collision.addScoreboardTag("custom-block-collision");
            collision.addScoreboardTag("custom-block-collision-id:" + configuredCollisionIdentifier);
            collision.addScoreboardTag("custom-block-location:" + location.toVector());

            return collision;
        }).filter(Objects::nonNull).collect(OperationUtil.toArrayList());


        // Construct the raw custom block object
        CustomBlock rawCustomBlock = new BDCCustomBlock(abstractCustomBlock, location, rotation, displays, interactions, collisions, UUID.randomUUID());

        // Create and store custom block data on the location of placement
        CustomBlockData customBlockData = createCustomBlockData(rawCustomBlock, displayVehicleUuids);

        // Trigger the custom block place event and handle cancellation
        if (!triggerCustomBlockPlaceEvent(rawCustomBlock, player, customBlockData)) {
            return null;
        }

        // Play placement sound if the option is not set to silent
        if (!silentPlace) {
            abstractCustomBlock.playPlaceSound(location);
        }

        // Execute place commands if any are set
        CustomBlockStageSettings stageSettings = abstractCustomBlock.getStageSettings();
        if (stageSettings != null) {
            CommandBundle placeCommandBundle = stageSettings.getPlaceSettings().getCommandBundle();
            if (placeCommandBundle != null) {
                CommandBundle.CommandSource commandSource = placeCommandBundle.getCommandSource();
                if (commandSource == CommandBundle.CommandSource.PLAYER && player != null) {
                    placeCommandBundle.execute(player, new CustomBlockPlaceholder(rawCustomBlock));
                } else if (commandSource == CommandBundle.CommandSource.CONSOLE) {
                    placeCommandBundle.execute(ChatUtil.CONSOLE, new CustomBlockPlaceholder(rawCustomBlock));
                }
            }
        }

        return rawCustomBlock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean breakBlock(@NotNull CustomBlock customBlock, @Nullable Player player, CustomBlockOption... options) throws IllegalArgumentException {
        Location location = customBlock.getLocation();
        World world = location.getWorld();
        LocationPlaceholder locationPlaceholder = new LocationPlaceholder(location);

        // Checking if a location has a world
        if (world == null) {
            throw new IllegalArgumentException("Location's world cannot be null");
        }

        // Check if a custom block is present at the location
        if (!isCustomBlockOnLocation(location)) {
            throw new IllegalArgumentException(locationPlaceholder.apply(StringMessagesValue.CUSTOM_BLOCK_BREAK_FAIL_REASON_NO_CUSTOM_BLOCK));
        }

        // Parsing options
        boolean dropItem = false;
        boolean silentBreak = false;

        // Iterate through provided options and set flags accordingly
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

        // Execute break commands if any are set (before the block is actually broken)
        CustomBlockStageSettings stageSettings = customBlock.getStageSettings();
        if (stageSettings != null) {
            CommandBundle breakCommandBundle = stageSettings.getBreakSettings().getCommandBundle();
            if (breakCommandBundle != null) {
                breakCommandBundle.execute(player, new CustomBlockPlaceholder[]{new CustomBlockPlaceholder(customBlock)});
            }
        }

        // Create custom block data instance for clearing later
        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());

        // Trigger the custom block break event and handle cancellation
        if (!triggerCustomBlockBreakEvent(customBlock, player)) {
            return false;
        }

        // Remove all entities associated with the custom block
        customBlock.getDisplays().forEach(Entity::remove);
        customBlock.getInteractions().forEach(Entity::remove);
        customBlock.getCollisions().forEach(collision -> {
            Entity vehicle = collision.getVehicle();
            collision.remove();

            if (vehicle != null) {
                vehicle.remove();
            }
        });

        // Set the block to air to "break" it
        location.getBlock().setType(Material.AIR);

        // Clear all custom block data from the block
        clearCustomBlockData(customBlockData);

        // Drop the item if the option is set
        if (dropItem) {
            ItemStack customBlockItem = customBlock.getItem();
            // Ensure only one item is dropped
            customBlockItem.setAmount(1);

            // If stage settings are defined, use them to determine drop behavior
            if (stageSettings != null) {
                CustomBlockBreakSettings.DropMode dropMode = stageSettings.getBreakSettings().getDropMode();

                if (dropMode == CustomBlockBreakSettings.DropMode.INVENTORY && player != null) {

                    // Try to add the item to the player's inventory, and if full, drop it on the ground
                    ItemUtil.distributeItem(player, customBlockItem);
                } else if (dropMode == CustomBlockBreakSettings.DropMode.ON_GROUND) {

                    // Drop the item naturally on the ground
                    world.dropItemNaturally(location, customBlockItem);
                }
            } else {
                // Drop the item naturally on the ground
                world.dropItemNaturally(location, customBlockItem);
            }
        }

        // Play break sound if the option is not set to silent
        if (!silentBreak) {
            customBlock.playBreakSound(location);
        }

        return true;
    }
}
