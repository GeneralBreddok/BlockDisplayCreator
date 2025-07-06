package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * This abstract class represents an event related to a custom block.
 * It extends the {@link org.bukkit.event.Event} class and provides methods to access the player and custom block involved.
 */
@Getter
public abstract class CustomBlockPlayerEvent extends CustomBlockEvent {
    private final Player player;

    /**
     * Constructs a new instance of CustomBlockEvent.
     *
     * @param customBlock  The custom customBlock involved in this event.
     * @param player The player involved in this event.
     */
    public CustomBlockPlayerEvent(@NotNull CustomBlock customBlock, Player player) {
        super(customBlock);
        this.player = player;
    }
}
