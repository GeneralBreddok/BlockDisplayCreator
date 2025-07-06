package me.general_breddok.blockdisplaycreator.custom.block;

import com.jeff_media.customblockdata.CustomBlockData;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockChangeNameOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockPlaceOption;
import me.general_breddok.blockdisplaycreator.data.manager.PersistentDataTypes;
import me.general_breddok.blockdisplaycreator.rotation.BlockRotation;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import me.general_breddok.blockdisplaycreator.util.EntityUtil;
import me.general_breddok.blockdisplaycreator.world.TransformationBuilder;
import me.general_breddok.blockdisplaycreator.world.WorldSelection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Shulker;
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

        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());
        UUID[] uuids = new UUID[interactions.size()];

        for (int i = 0; i < interactions.size(); i++) {
            Interaction interaction = interactions.get(i);

            interaction.setPersistent(true);
            interaction.setInvulnerable(true);

            interaction.teleport(location.clone().add(0, -0.001, 0));

            CustomBlockKey.DataHolder cbDataHolder = CustomBlockKey.holder(interaction);

            cbDataHolder.setServiceClass(BDCCustomBlockService.class.getName());
            cbDataHolder.setName(name);
            cbDataHolder.setLocation(location);

            Vector offset = interaction.getLocation().toVector().subtract(this.location.toVector());

            rotation.adjustInteractionRotation(interaction, offset, sidesCount);

            uuids[i] = interaction.getUniqueId();
        }

        customBlockData.set(CustomBlockKey.INTERACTION_UUID, PersistentDataTypes.UUID_ARRAY, uuids);
    }

    @Override
    public void setCollisions(@NotNull List<Shulker> collisions, CustomBlockOption... options) {

    }

    public void setName(@NotNull String name, CustomBlockOption... options) {

        boolean saveToStorage = false;

        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockChangeNameOption.SAVE_TO_STORAGE) {
                saveToStorage = true;
                break;
            }
        }

        CustomBlockData customBlockData = new CustomBlockData(this.location.getBlock(), BlockDisplayCreator.getInstance());

        customBlockData.set(CustomBlockKey.NAME, PersistentDataType.STRING, name);

        this.displays.forEach(display -> CustomBlockKey.holder(display).setName(name));
        this.interactions.forEach(display -> CustomBlockKey.holder(display).setName(name));

        if (saveToStorage) {
            //BlockDisplayCreator.getInstance().getCustomBlockService().getStorage().saveToStorage(this);
        }
    }

    @Override
    public void setDisplays(@NotNull List<Display> displays, CustomBlockOption... options) {
        this.displays.forEach(Entity::remove);
        this.displays = displays;

        List<UUID> vehicleUuids = new ArrayList<>();

        for (Display display : displays) {
            if (!display.isInsideVehicle()) {
                EntityUtil.teleportEntityWithPassengers(display, location);
                vehicleUuids.add(display.getUniqueId());
            }
            display.setInvulnerable(true);
            display.setPersistent(true);

            display.setTransformation(new TransformationBuilder(display).addTranslation(-0.5f, 0, -0.5f).build());

            rotation.adjustDisplayRotation(display, sidesCount);

            CustomBlockKey.DataHolder cbDataHolder = CustomBlockKey.holder(display);
            cbDataHolder.setName(name);
            cbDataHolder.setLocation(location);
            cbDataHolder.setServiceClass(BDCCustomBlockService.class.getName());
        }


        CustomBlockData customBlockData = new CustomBlockData(location.getBlock(), BlockDisplayCreator.getInstance());
        customBlockData.set(CustomBlockKey.DISPLAY_UUID, PersistentDataTypes.UUID_ARRAY, vehicleUuids.toArray(UUID[]::new));
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

        if (!location.getChunk().isLoaded()) {
            return;
        }

        boolean replaceCustomBlock = false;
        boolean replaceIndestructible = false;

        for (CustomBlockOption option : options) {

            if (option == null) {
                continue;
            }

            if (option == CustomBlockPlaceOption.REPLACE_CUSTOM_BLOCK) {
                replaceCustomBlock = true;
            } else if (option == CustomBlockPlaceOption.BREAK_INDESTRUCTIBLE_MATERIAL) {
                replaceIndestructible = true;
            }
        }

        if (!WorldSelection.isDestructible(location.getBlock())) {
            if (replaceIndestructible) {
                location.getBlock().setType(centralMaterial);
            } else {
                return;
            }
        }

        ServiceManager<String, CustomBlockService> servicesManager = BlockDisplayCreator.getInstance().getServicesManager();
        CustomBlockService service = servicesManager.getService(serviceClassName);

        if (service == null) {
            throw new UnregisteredServiceException("Service " + serviceClassName + " is not registered", serviceClassName);
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


        this.interactions.forEach(interaction -> {
            interaction.teleport(location.clone().add(0, -0.001, 0));
            interaction.getPersistentDataContainer().set(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION, location);
        });

        List<UUID> vehicleUuids = new ArrayList<>();

        this.displays.forEach(display -> {

            display.getPersistentDataContainer().set(CustomBlockKey.LOCATION, PersistentDataTypes.LOCATION, location);

            if (!display.isInsideVehicle()) {
                vehicleUuids.add(display.getUniqueId());

                EntityUtil.teleportEntityWithPassengers(display, location);
            }
        });

        BDCCustomBlockService.createCustomBlockData(this, vehicleUuids);
        location.getBlock().setType(centralMaterial);
    }
}
