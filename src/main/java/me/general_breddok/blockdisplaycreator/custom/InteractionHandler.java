package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.placeholder.universal.UniversalPlaceholder;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface InteractionHandler {
    CommandBundle getCommandBundle();
    long getCooldown();
    /**
     * Handles the click interaction.
     *
     * @param interaction The interaction that was clicked.
     * @param player      The player who clicked the interaction.
     */
    void handleClick(@NotNull Interaction interaction, @NotNull Player player, UniversalPlaceholder<?>... placeholders);
}
