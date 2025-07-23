package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
     * @param data additional data for the check.
     * @return true if a custom block is present at the location, false otherwise.
     */
    boolean isCustomBlockOnLocation(@NotNull Location location, Object... data);

    /**
     * Gets the custom block at the specified location.
     *
     * @param location the location to search for the block.
     * @param data additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Location location, Object... data);

    /**
     * Gets the custom block associated with the given interaction entity.
     *
     * @param interaction the interaction entity.
     * @param data additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Interaction interaction, Object... data);

    /**
     * Gets the custom block associated with the given display entity.
     *
     * @param display the display entity.
     * @param data additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Display display, Object... data);

    /**
     * Gets the custom block associated with the given shulker entity (collision).
     *
     * @param collision the shulker entity.
     * @param data additional data for the search.
     * @return the {@link CustomBlock} if found, otherwise null.
     */
    @Nullable
    CustomBlock getCustomBlock(@NotNull Shulker collision, Object... data);

    /**
     * Places a custom block at the specified location.
     *
     * @param abstractCustomBlock the abstract custom block to place.
     * @param location the location to place the block.
     * @param rotation the rotation of the block.
     * @param player the player placing the block (optional).
     * @param options additional placement options.
     * @return the placed {@link CustomBlock} if successful, otherwise null.
     */
    @Nullable
    CustomBlock placeBlock(@NotNull AbstractCustomBlock abstractCustomBlock, @NotNull Location location, @NotNull CustomBlockRotation rotation, @Nullable Player player, CustomBlockOption... options);

    /**
     * Breaks the specified custom block.
     *
     * @param customBlock the custom block to break.
     * @param player the player breaking the block (nullable).
     * @param options additional breaking options.
     * @return true if the block was successfully broken, false otherwise.
     */
    boolean breakBlock(@NotNull CustomBlock customBlock, @Nullable Player player, CustomBlockOption... options);

    /**
     * Creates an item associated with a custom block.
     *
     * @param itemStack the item stack to associate.
     * @param customBlockService the custom block service.
     * @param customBlockName the name of the custom block.
     */
    static void createCBItem(ItemStack itemStack, CustomBlockService customBlockService, String customBlockName) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        CustomBlockKey.holder(itemMeta)
                .setServiceClass(customBlockService.getClass().getName())
                .setName(customBlockName);

        itemStack.setItemMeta(itemMeta);
    }
}
