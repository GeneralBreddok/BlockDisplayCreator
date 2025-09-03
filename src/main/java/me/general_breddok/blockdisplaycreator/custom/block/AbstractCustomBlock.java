package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.common.DeprecatedFeatureAdapter;
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
 * Represents an abstract custom block with various properties and behaviors.
 * This interface defines methods to manage the block's name, service class,
 * display summoner, interactions, collisions, item representation, material,
 * sound group, stage settings, and permissions.
 */
public interface AbstractCustomBlock extends DeepCloneable<AbstractCustomBlock> {
    @NotNull
    String getName();

    void setName(String name);

    /**
     * Gets the class name of the service that handles the custom block's behavior.
     * <p>
     * <b> Note: </b> {@link Class#getName()} is used to get the class name of the service
     * @return Class name of the service as a String.
     */
    @NotNull
    String getServiceClassName();

    void setServiceClassName(String serviceClassName);

    @NotNull
    GroupSummoner<Display> getDisplaySummoner();

    void setDisplaySummoner(GroupSummoner<Display> displaySummoner);

    @NotNull
    List<ConfiguredInteraction> getConfiguredInteractions();

    void setConfiguredInteractions(List<ConfiguredInteraction> configuredInteractions);

    @NotNull
    List<ConfiguredCollision> getConfiguredCollisions();

    void setConfiguredCollisions(List<ConfiguredCollision> configuredCollisions);

    ItemStack getItem();

    void setItem(ItemStack item);

    Material getCentralMaterial();

    void setCentralMaterial(Material centralMaterial);

    short getSidesCount();

    void setSidesCount(short sidesCount);

    @Nullable
    CustomBlockPermissions getPermissions();

    void setPermissions(CustomBlockPermissions permissions);

    @Nullable
    CustomBlockSoundGroup getSoundGroup();

    void setSoundGroup(CustomBlockSoundGroup soundGroup);

    @Nullable
    CustomBlockStageSettings getStageSettings();

    void setStageSettings(CustomBlockStageSettings stageSettings);

    String getSaveSystem();

    void setSaveSystem(String saveSystem);



    default List<Display> spawnDisplay(@NotNull Location location, @Nullable Predicate<Display> iteration) {
        return getDisplaySummoner().summon(location, iteration);
    }

    default List<Display> spawnDisplay(@NotNull Location location, @NotNull Entity commandSender, Predicate<Display> iteration) {
        if (getDisplaySummoner() instanceof CommandSummoner<Display> commandSummoner) {
            return commandSummoner.summon(location, commandSender, iteration);
        }
        return spawnDisplay(location, iteration);
    }

    default void handleClick(@NotNull ConfiguredInteraction configuredInteraction, @NotNull Interaction interaction, @NotNull Player sender, UniversalPlaceholder<?>... placeholders) {
        InteractionHandler interactionHandler = configuredInteraction.getInteractionHandler();

        if (interactionHandler == null) {
            return;
        }

        interactionHandler.handleClick(interaction, sender, placeholders);
    }

    @Nullable
    default ConfiguredInteraction getConfiguredInteraction(@NotNull Interaction interaction) {
        String interactionIdentifier = getInteractionIdentifier(interaction);

        return getConfiguredInteraction(interactionIdentifier);
    }

    @Nullable
    default ConfiguredInteraction getConfiguredInteraction(@NotNull String identifier) {
        return getConfiguredInteractions().stream()
                .filter(configuredInteraction -> configuredInteraction.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    private String getInteractionIdentifier(@NotNull Interaction interaction) {
        String identifier = CustomBlockKey.holder(interaction).getInteractionIdentifier();

        return identifier;
    }

    default void playBreakSound(@NotNull Location location) {
        playSound("break", location);
    }

    default void playPlaceSound(@NotNull Location location) {
        playSound("place", location);
    }

    private void playSound(String name, Location location) {
        if (getSoundGroup() == null)
            return;

        PlayableSound placeSound = name.equals("place") ? getSoundGroup().getPlaceSound() : getSoundGroup().getBreakSound();

        if (placeSound == null)
            return;

        placeSound.play(location);
    }
}
