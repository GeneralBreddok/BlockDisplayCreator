package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import me.general_breddok.blockdisplaycreator.service.ServiceManager;
import me.general_breddok.blockdisplaycreator.service.exception.UnregisteredServiceException;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service interface for managing custom blocks in Minecraft.
 */
public interface CustomBlockService {

    /**
     * Gets the storage for custom blocks.
     *
     * @return the {@link CustomBlockStorage} instance representing the custom block storage.
     */
    @NotNull
    CustomBlockStorage getStorage();

    /**
     * Checks if a custom block is present at the specified location.
     *
     * @param location the location to check.
     * @param data     additional data for the check.
     * @return true if a custom block is present at the location, false otherwise.
     */
    boolean isCustomBlockOnLocation(@NotNull Location location, Object... data);

    /**
     * Gets the custom block at the specified location.
     *
     * @param location the location to search for the block.
     * @param data     additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Location location, Object... data);

    /**
     * Gets the custom block associated with the specified entity.
     *
     * @param entity the entity to check for a custom block.
     * @param data   additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Entity entity, Object... data);

    /**
     * Places a custom block at the specified location.
     *
     * @param abstractCustomBlock the abstract custom block to place.
     * @param location            the location to place the block.
     * @param rotation            the rotation of the block.
     * @param player              the player placing the block (optional).
     * @param options             additional placement options.
     * @return the placed {@link CustomBlock} if successful, otherwise null.
     */
    @Nullable
    CustomBlock placeBlock(@NotNull AbstractCustomBlock abstractCustomBlock, @NotNull Location location, @NotNull CustomBlockRotation rotation, @Nullable Player player, CustomBlockOption... options) throws IllegalArgumentException;

    /**
     * Breaks the specified custom block.
     *
     * @param customBlock the custom block to break.
     * @param player      the player breaking the block (nullable).
     * @param options     additional breaking options.
     * @return true if the block was successfully broken, false otherwise.
     */
    boolean breakBlock(@NotNull CustomBlock customBlock, @Nullable Player player, CustomBlockOption... options) throws IllegalArgumentException;

    /**
     * Retrieves the appropriate CustomBlockService based on the provided item.
     * <p>
     * Uses the {@link PersistentDataHolder} persistent data to identify and retrieve the registered service.
     *
     * @param serviceManager the service manager to retrieve services from.
     * @param holder         the PersistentDataHolder (e.g., ItemMeta) containing persistent data to identify the service.
     * @return the corresponding CustomBlockService.
     * @throws UnregisteredServiceException if the service is not registered.
     */
    @NotNull
    static CustomBlockService getService(ServiceManager<String, CustomBlockService> serviceManager, PersistentDataHolder holder) throws UnregisteredServiceException {
        String serviceClassName = CustomBlockKey.holder(holder).getServiceClass();

        serviceClassName = DeprecatedFeatureAdapter.checkMissingServiceClass(serviceClassName);

        CustomBlockService customBlockService = serviceManager.getService(serviceClassName);

        return customBlockService;
    }

    /**
     * Creates an item associated with a custom block.
     *
     * @param itemStack          the item stack to associate.
     * @param customBlockService the custom block service.
     * @param customBlockName    the name of the custom block.
     */
    static void createCBItem(ItemStack itemStack, CustomBlockService customBlockService, String customBlockName) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        CustomBlockKey.holder(itemMeta)
                .setServiceClass(customBlockService.getClass().getName())
                .setName(customBlockName);

        itemStack.setItemMeta(itemMeta);
    }
}
