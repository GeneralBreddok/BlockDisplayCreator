package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a custom event that is fired when a player interacts with a custom block.
 * It extends the {@link CustomBlockPlayerEvent} and implements the Cancellable interface.
 */

public class CustomBlockInteractEvent extends CustomBlockPlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    @Getter
    private final EquipmentSlot hand;

    /**
     * Constructs a new CustomBlockInteractEvent.
     *
     * @param player      The player who interacted with the custom block.
     * @param block       The custom block that was interacted with.
     * @param hand        The hand in which the interaction occurred.
     */
    public CustomBlockInteractEvent(@NotNull CustomBlock block, @NotNull Player player, @NotNull EquipmentSlot hand) {
        super(block, player);
        this.hand = hand;
    }

    /**
     * Returns the handler list for this event.
     *
     * @return The handler list for this event.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
