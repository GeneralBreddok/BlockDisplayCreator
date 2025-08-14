package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.event.Event;

/**
 * This abstract class represents an event related to a custom block.
 * It extends the {@link org.bukkit.event.Event} class and provides access to the custom block involved in the event.
 */
@Getter
public abstract class CustomBlockEvent extends Event {
    private final CustomBlock customBlock;

    /**
     * Constructs a new instance of CustomBlockEvent.
     *
     * @param customBlock The custom block involved in this event.
     */
    protected CustomBlockEvent(CustomBlock customBlock) {
        this.customBlock = customBlock;
    }
}
