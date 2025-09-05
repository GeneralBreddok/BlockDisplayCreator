package me.general_breddok.blockdisplaycreator.event.custom.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredInteraction;
import me.general_breddok.blockdisplaycreator.custom.block.CustomBlock;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a custom event that is fired when a player interacts with a custom block (interaction entity).
 * It extends the {@link CustomBlockPlayerEvent} and implements the Cancellable interface.
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomBlockInteractEvent extends CustomBlockPlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    Interaction interactionEntity;
    ConfiguredInteraction configuredInteraction;
    EquipmentSlot hand;
    @Setter
    @NonFinal
    boolean cancelled = false;


    /**
     * Constructs a new CustomBlockInteractEvent.
     *
     * @param block                 The custom block that was interacted with.
     * @param player                The player who interacted with the custom block.
     * @param interactionEntity     The interaction entity that was used to interact with the custom block.
     * @param configuredInteraction The configured interaction associated with this event.
     * @param hand                  The hand used for the interaction (main or off).
     */

    public CustomBlockInteractEvent(@NotNull CustomBlock block, @NotNull Player player,
                                    @NotNull Interaction interactionEntity,
                                    @NotNull ConfiguredInteraction configuredInteraction,
                                    @NotNull EquipmentSlot hand) {
        super(block, player);
        this.interactionEntity = interactionEntity;
        this.configuredInteraction = configuredInteraction;
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
}
