package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class CustomBlockBreakEvent extends CustomBlockPlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;

    /**
     * Constructs a new CustomBlockBreakEvent.
     *
     * @param player      The player who broke the custom block.
     * @param block       The custom block that was broken.
     */
    public CustomBlockBreakEvent(@NotNull CustomBlock block, @Nullable Player player) {
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

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
