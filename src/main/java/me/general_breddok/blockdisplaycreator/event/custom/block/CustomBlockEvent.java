package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.Getter;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.event.Event;

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
