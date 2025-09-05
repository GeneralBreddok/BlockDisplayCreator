package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredCollision;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredInteraction;
import me.general_breddok.blockdisplaycreator.custom.InteractionHandler;
import me.general_breddok.blockdisplaycreator.entity.CommandSummoner;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.placeholder.universal.UniversalPlaceholder;
import me.general_breddok.blockdisplaycreator.sound.PlayableSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents the base contract for a custom block in BlockDisplayCreator.
 * <p>
 * Defines all essential properties such as name, service class, display summoner,
 * interaction summoners, collision summoners, permissions, sounds, and saving system.
 * Also provides default helper methods for spawning displays, handling interactions,
 * and playing block sounds.
 * </p>
 */
public interface AbstractCustomBlock extends DeepCloneable<AbstractCustomBlock> {

    /**
     * Gets the unique name of the custom block.
     *
     * @return the block's name
     */
    @NotNull
    String getName();

    /**
     * Sets the name of the custom block.
     *
     * @param name the new name
     */
    void setName(String name);

    /**
     * Gets the class name of the service that handles this custom block's behavior.
     * <p>
     * Note: {@link Class#getName()} is used to obtain the class name.
     * </p>
     *
     * @return the service class name
     */
    @NotNull
    String getServiceClassName();

    /**
     * Sets the service class name.
     *
     * @param serviceClassName the service class name
     */
    void setServiceClassName(String serviceClassName);

    /**
     * Gets the summoner responsible for spawning display entities of the block.
     *
     * @return the display summoner
     */
    @NotNull
    GroupSummoner<Display> getDisplaySummoner();

    /**
     * Sets the summoner for display entities.
     *
     * @param displaySummoner the display summoner
     */
    void setDisplaySummoner(GroupSummoner<Display> displaySummoner);

    /**
     * Gets the configured interactions of the block.
     *
     * @return list of interactions
     */
    @NotNull
    List<ConfiguredInteraction> getConfiguredInteractions();

    /**
     * Sets the configured interactions of the block.
     *
     * @param configuredInteractions list of interactions
     */
    void setConfiguredInteractions(List<ConfiguredInteraction> configuredInteractions);

    /**
     * Gets the configured collisions of the block.
     *
     * @return list of collisions
     */
    @NotNull
    List<ConfiguredCollision> getConfiguredCollisions();

    /**
     * Sets the configured collisions of the block.
     *
     * @param configuredCollisions list of collisions
     */
    void setConfiguredCollisions(List<ConfiguredCollision> configuredCollisions);

    /**
     * Gets the item representation of this block.
     *
     * @return the item
     */
    ItemStack getItem();

    /**
     * Sets the item representation of this block.
     *
     * @param item the item
     */
    void setItem(ItemStack item);

    /**
     * Gets the central material of this block.
     *
     * @return the material
     */
    Material getCentralMaterial();

    /**
     * Sets the central material of this block.
     *
     * @param centralMaterial the material
     */
    void setCentralMaterial(Material centralMaterial);

    /**
     * Gets the number of sides for this block.
     *
     * @return sides count
     */
    short getSidesCount();

    /**
     * Sets the number of sides for this block.
     *
     * @param sidesCount sides count
     */
    void setSidesCount(short sidesCount);

    /**
     * Gets the permissions associated with this block.
     *
     * @return permissions or null
     */
    @Nullable
    CustomBlockPermissions getPermissions();

    /**
     * Sets the permissions for this block.
     *
     * @param permissions the permissions
     */
    void setPermissions(CustomBlockPermissions permissions);

    /**
     * Gets the sound group for this block.
     *
     * @return sound group or null
     */
    @Nullable
    CustomBlockSoundGroup getSoundGroup();

    /**
     * Sets the sound group for this block.
     *
     * @param soundGroup the sound group
     */
    void setSoundGroup(CustomBlockSoundGroup soundGroup);

    /**
     * Gets the stage settings of this block.
     *
     * @return stage settings or null
     */
    @Nullable
    CustomBlockStageSettings getStageSettings();

    /**
     * Sets the stage settings of this block.
     *
     * @param stageSettings the stage settings
     */
    void setStageSettings(CustomBlockStageSettings stageSettings);

    /**
     * Gets the save system identifier.
     *
     * @return save system identifier
     */
    String getSaveSystem();

    /**
     * Sets the save system identifier.
     *
     * @param saveSystem save system identifier
     */
    void setSaveSystem(String saveSystem);


    // ---------- Default methods ----------

    /**
     * Spawns the display entities at the specified location.
     *
     * @param location  the location
     * @param iteration optional filter for spawned displays
     * @return list of spawned displays
     */
    default List<Display> spawnDisplay(@NotNull Location location, @Nullable Predicate<Display> iteration) {
        return getDisplaySummoner().summon(location, iteration);
    }

    /**
     * Spawns the display entities with an entity context.
     *
     * @param location      the location
     * @param commandSender the entity providing context
     * @param iteration     optional filter
     * @return list of spawned displays
     */
    default List<Display> spawnDisplay(@NotNull Location location, @NotNull Entity commandSender, Predicate<Display> iteration) {
        if (getDisplaySummoner() instanceof CommandSummoner<Display> commandSummoner) {
            return commandSummoner.summon(location, commandSender, iteration);
        }
        return spawnDisplay(location, iteration);
    }

    /**
     * Handles a configured interaction click event.
     *
     * @param configuredInteraction the configured interaction
     * @param interaction           the interaction entity
     * @param sender                the player triggering the interaction
     * @param placeholders          placeholders for the action
     */
    default void handleClick(@NotNull ConfiguredInteraction configuredInteraction, @NotNull Interaction interaction, @NotNull Player sender, UniversalPlaceholder<?>... placeholders) {
        InteractionHandler interactionHandler = configuredInteraction.getInteractionHandler();

        if (interactionHandler == null) {
            return;
        }

        interactionHandler.handleClick(interaction, sender, placeholders);
    }

    /**
     * Finds a configured interaction by interaction entity.
     *
     * @param interaction the interaction entity
     * @return configured interaction or null
     */
    @Nullable
    default ConfiguredInteraction getConfiguredInteraction(@NotNull Interaction interaction) {
        String interactionIdentifier = getInteractionIdentifier(interaction);
        return getConfiguredInteraction(interactionIdentifier);
    }

    /**
     * Finds a configured interaction by its identifier.
     *
     * @param identifier the identifier
     * @return configured interaction or null
     */
    @Nullable
    default ConfiguredInteraction getConfiguredInteraction(@NotNull String identifier) {
        return getConfiguredInteractions().stream()
                .filter(configuredInteraction -> configuredInteraction.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    /**
     * Plays the break sound at the specified location.
     *
     * @param location the location
     */
    default void playBreakSound(@NotNull Location location) {
        playSound("break", location);
    }

    /**
     * Plays the place sound at the specified location.
     *
     * @param location the location
     */
    default void playPlaceSound(@NotNull Location location) {
        playSound("place", location);
    }

    /**
     * Plays a sound from the sound group by name.
     *
     * @param name     "place" or "break"
     * @param location the location
     */
    private void playSound(String name, Location location) {
        if (getSoundGroup() == null)
            return;

        PlayableSound placeSound = name.equals("place") ? getSoundGroup().getPlaceSound() : getSoundGroup().getBreakSound();

        if (placeSound == null)
            return;

        placeSound.play(location);
    }

    /**
     * Extracts the identifier of the given interaction.
     *
     * @param interaction the interaction
     * @return identifier string
     */
    private String getInteractionIdentifier(@NotNull Interaction interaction) {
        return CustomBlockKey.holder(interaction).getInteractionIdentifier();
    }
}
