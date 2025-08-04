package me.general_breddok.blockdisplaycreator.custom.block;

import com.jeff_media.customblockdata.CustomBlockData;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockChangeDataOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockPlaceOption;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.rotation.BlockRotation;
import me.general_breddok.blockdisplaycreator.util.EntityUtil;
import me.general_breddok.blockdisplaycreator.world.TransformationBuilder;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BDCCustomBlock extends BDCAbstractCustomBlock implements CustomBlock {
    List<Display> displays;
    List<Interaction> interactions;
    List<Shulker> collisions;
    Location location;
    CustomBlockRotation rotation;
    final UUID uuid;


    public BDCCustomBlock(AbstractCustomBlock acb, Location location, CustomBlockRotation rotation, List<Display> displays, List<Interaction> interactions, List<Shulker> collisions, UUID uuid) {
        super(acb.getName(), acb.getDisplaySummoner(), acb.getConfiguredInteractions(), acb.getConfiguredCollisions(), acb.getItem(), acb.getCentralMaterial(), acb.getSidesCount(), acb.getPermissions(), acb.getSoundGroup(), acb.getStageSettings(), acb.getServiceClassName(), acb.getSaveSystem());
        this.location = location;
        this.rotation = rotation;
        this.displays = displays;
        this.interactions = interactions;
        this.collisions = collisions;
        this.uuid = uuid;
    }


    @Override
    public void setInteractions(@NotNull List<Interaction> interactions, CustomBlockOption... options) {
        this.interactions.forEach(Entity::remove);
        this.interactions = interactions;

        CustomBlockData customBlockData = new CustomBlockData(this.location.getBlock(), BlockDisplayCreator.getInstance());
        UUID[] uuids = new UUID[interactions.size()];

        for (int i = 0; i < interactions.size(); i++) {
            Interaction interaction = interactions.get(i);

            if (interaction == null) {
                continue;
            }

            String interactionIdentifier = CustomBlockKey.holder(interaction).getInteractionIdentifier();
            if (interactionIdentifier == null) {
                continue;
            }

            interaction.setPersistent(true);
            interaction.setInvulnerable(true);

            interaction.teleport(this.location.clone());
            Vector offset = interaction.getLocation().toVector().subtract(this.location.toVector());
            rotation.adjustInteractionRotation(interaction, offset, sidesCount);

            CustomBlockKey.holder(interaction)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(this.name)
                    .setLocation(this.location);

            interaction.addScoreboardTag("custom-block");
            interaction.addScoreboardTag("custom-block-name:" + this.name);
            interaction.addScoreboardTag("custom-block-interaction");
            interaction.addScoreboardTag("custom-block-interaction-id:" + interactionIdentifier);
            interaction.addScoreboardTag("custom-block-location:" + this.location.toVector());

            uuids[i] = interaction.getUniqueId();
        }

        customBlockData.set(CustomBlockKey.INTERACTION_UUID, PersistentDataTypes.UUID_ARRAY, uuids);
    }

    @Override
    public void setCollisions(@NotNull List<Shulker> collisions, CustomBlockOption... options) {
        this.collisions.forEach(collision -> {
            Entity vehicle = collision.getVehicle();
            collision.remove();

            if (vehicle != null) {
                vehicle.remove();
            }
        });;
        this.collisions = collisions;
        CustomBlockData customBlockData = new CustomBlockData(this.location.getBlock(), BlockDisplayCreator.getInstance());
        UUID[] uuids = new UUID[collisions.size()];
        for (int i = 0; i < collisions.size(); i++) {
            Shulker collision = collisions.get(i);

            if (collision == null) {
                continue;
            }

            String collisionIdentifier = CustomBlockKey.holder(collision).getCollisionIdentifier();

            if (collisionIdentifier == null) {
                continue;
            }

            BlockDisplay vehicle = location.getWorld().spawn(collision.getLocation(), BlockDisplay.class);

            collision.setRemoveWhenFarAway(false);
            collision.setAI(false);
            collision.setGravity(false);
            collision.setInvulnerable(true);
            collision.setPersistent(true);

            vehicle.teleport(this.location.clone());
            vehicle.addPassenger(collision);

            Vector offset = collision.getLocation().toVector().subtract(this.location.toVector());
            rotation.adjustCollisionRotation(collision, offset, sidesCount);

            CustomBlockKey.holder(collision)
                    .setServiceClass(BDCCustomBlockService.class.getName())
                    .setName(this.name)
                    .setLocation(this.location);

            collision.addScoreboardTag("custom-block");
            collision.addScoreboardTag("custom-block-name:" + this.name);
            collision.addScoreboardTag("custom-block-collision");
            collision.addScoreboardTag("custom-block-collision-id:" + collisionIdentifier);
            collision.addScoreboardTag("custom-block-location:" + this.location.toVector());

            uuids[i] = collision.getUniqueId();
        }
        customBlockData.set(CustomBlockKey.COLLISION_UUID, PersistentDataTypes.UUID_ARRAY, uuids);

    }

    public void setName(@NotNull String name, CustomBlockOption... options) {
        this.name = name;

        boolean saveToStorage = false;

        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockChangeDataOption.SAVE_TO_STORAGE) {
                saveToStorage = true;
            }
        }

        CustomBlockData customBlockData = new CustomBlockData(this.location.getBlock(), BlockDisplayCreator.getInstance());

        customBlockData.set(CustomBlockKey.NAME, PersistentDataType.STRING, name);

        this.displays.forEach(display -> CustomBlockKey.holder(display).setName(name));
        this.interactions.forEach(interaction -> CustomBlockKey.holder(interaction).setName(name));
        this.collisions.forEach(collision -> CustomBlockKey.holder(collision).setName(name));

        if (saveToStorage) {
            BlockDisplayCreator.getInstance().getCustomBlockService().getStorage().addAbstractCustomBlock(this);
        }
    }

    @Override
    public void setDisplays(@NotNull List<Display> displays, CustomBlockOption... options) {
        this.displays.forEach(Entity::remove);
        this.displays = displays;

        List<UUID> displayVehicleUuids = new ArrayList<>();

        for (Display display : displays) {
            if (!display.isInsideVehicle()) {
                EntityUtil.teleportEntityWithPassengers(display, location);
                displayVehicleUuids.add(display.getUniqueId());
            }
            display.setInvulnerable(true);
            display.setPersistent(true);

            rotation.adjustDisplayRotation(display, sidesCount);
            BDCCustomBlockService.applyDisplayTranslationRotation(this.displaySummoner, display);

            CustomBlockKey.holder(display)
                    .setName(name)
                    .setLocation(location)
                    .setServiceClass(BDCCustomBlockService.class.getName());

            display.addScoreboardTag("custom-block");
            display.addScoreboardTag("custom-block-name:" + this.name);
            display.addScoreboardTag("custom-block-display");
            display.addScoreboardTag("custom-block-location:" + location.toVector());
        }


        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());
        customBlockData.set(CustomBlockKey.DISPLAY_UUID, PersistentDataTypes.UUID_ARRAY, displayVehicleUuids.toArray(UUID[]::new));
    }

    @Override
    public void setRotation(@NotNull CustomBlockRotation rotation, CustomBlockOption... options) {
        boolean isSideFace = BlockRotation.isSideFace(this.rotation.getAttachedFace());
        this.rotation = rotation;

        this.interactions.forEach(interaction -> {
            Location interactionLocation = interaction.getLocation();
            Vector offset = interactionLocation.toVector().subtract(this.location.toVector());
            rotation.adjustInteractionRotation(interaction, offset, this.sidesCount);
        });

        this.collisions.forEach(collision -> {
            Location collisionLocation = collision.getLocation();
            Vector offset = collisionLocation.toVector().subtract(this.location.toVector());
            rotation.adjustCollisionRotation(collision, offset, this.sidesCount);
        });

        this.displays.forEach(display -> {

            if (isSideFace) {
                display.setTransformation(new TransformationBuilder(display).addTranslation(0, 0.5f, 0.5f).build());
            }
            rotation.adjustDisplayRotation(display, this.sidesCount);
        });


        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());
        customBlockData.set(CustomBlockKey.BLOCK_ROTATION, PersistentDataTypes.CUSTOM_BLOCK_ROTATION, rotation);
    }

    @Override
    public void setLocation(@NotNull Location location, CustomBlockOption... options) {
        CustomBlockService service = BlockDisplayCreator.getInstance().getCustomBlockService();

        boolean loadChunk = false;
        boolean replaceCustomBlock = false;
        boolean breakSolidMaterial = false;

        for (CustomBlockOption option : options) {
            if (option == null) {
                continue;
            }

            if (option == CustomBlockPlaceOption.REPLACE_CUSTOM_BLOCK) {
                replaceCustomBlock = true;
            } else if (option == CustomBlockPlaceOption.BREAK_SOLID_MATERIAL) {
                breakSolidMaterial = true;
            } else if (option == CustomBlockPlaceOption.LOAD_CHUNK) {
                loadChunk = true;
            }
        }

        if (!location.getChunk().isLoaded()) {
            if (loadChunk) {
                location.getChunk().load();
            } else {
                return;
            }
        }

        if (!WorldSelection.isEphemeral(location.getBlock())) {
            if (breakSolidMaterial) {
                location.getBlock().setType(centralMaterial);
            } else {
                return;
            }
        }

        if (service.isCustomBlockOnLocation(location)) {
            if (replaceCustomBlock) {
                service.breakBlock(service.getCustomBlock(location), null);
            } else {
                return;
            }
        }


        this.location.getBlock().setType(Material.AIR);
        BDCCustomBlockService.clearCustomBlockData(new CustomBlockData(this.location.getBlock(), BlockDisplayCreator.getInstance()));
        this.location = location;

        List<UUID> displayVehicleUuids = new ArrayList<>();

        this.displays.forEach(display -> {

            display.getPersistentDataContainer().set(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION, location);

            if (!display.isInsideVehicle()) {
                displayVehicleUuids.add(display.getUniqueId());

                EntityUtil.teleportEntityWithPassengers(display, location);
            }
        });

        this.interactions.forEach(interaction -> {
            interaction.teleport(location);
            interaction.getPersistentDataContainer().set(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION, location);
            interaction.getScoreboardTags().forEach(tag -> {
                if (tag.startsWith("custom-block-location:")) {
                    interaction.removeScoreboardTag(tag);
                }
            });
            interaction.addScoreboardTag("custom-block-location:" + location.toVector());
        });

        this.collisions.forEach(collision -> {
            collision.teleport(location);
            collision.getPersistentDataContainer().set(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION, location);
            collision.getScoreboardTags().forEach(tag -> {
                if (tag.startsWith("custom-block-location:")) {
                    collision.removeScoreboardTag(tag);
                }
            });
            collision.addScoreboardTag("custom-block-location:" + location.toVector());
        });

        BDCCustomBlockService.createCustomBlockData(this, displayVehicleUuids);
        location.getBlock().setType(centralMaterial);
    }
}
