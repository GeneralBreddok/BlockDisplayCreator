package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a custom event that is fired when a player places a custom block.
 * It extends {@link CustomBlockPlayerEvent} and implements {@link Cancellable}.
 */
public class CustomBlockPlaceEvent extends CustomBlockPlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    @Setter
    private boolean cancelled = false;

    /**
     * Constructs a new CustomBlockPlaceEvent.
     *
     * @param player The player who placed the custom block.
     * @param block  The custom block that was placed.
     */
    public CustomBlockPlaceEvent(@NonNull CustomBlock block, @Nullable Player player) {
        super(block, player);
    }

    /**
     * Returns the list of handlers for this event.
     *
     * @return The list of handlers for this event.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
