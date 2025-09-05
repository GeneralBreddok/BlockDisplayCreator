package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredCollision;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredInteraction;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.sound.SimplePlayableSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Default implementation of {@link AbstractCustomBlock} used by the BlockDisplayCreator plugin.
 * <p>
 * This class encapsulates all data and behavior required for representing a custom block,
 * including its name, display summoners, interaction summoners, collision summoners, sounds, permissions,
 * and stage settings.
 * </p>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BDCAbstractCustomBlock implements AbstractCustomBlock {
    /**
     * Unique name of the custom block.
     */
    String name;
    /**
     * Fully-qualified class name of the service handling this block.
     */
    String serviceClassName;
    /**
     * Summoner responsible for spawning block-related {@link Display} entities.
     */
    GroupSummoner<Display> displaySummoner;
    /**
     * List of configured interactions supported by the block.
     */
    List<ConfiguredInteraction> configuredInteractions;
    /**
     * List of configured collisions supported by the block.
     */
    List<ConfiguredCollision> configuredCollisions;
    /**
     * Item representation of this custom block.
     */
    ItemStack item;
    /**
     * Central material used for the block's core representation.
     */
    Material centralMaterial;
    /**
     * Number of sides used by this block (e.g., for polygonal models).
     */
    short sidesCount;
    /**
     * Optional permissions controlling block usage.
     */
    @Nullable
    CustomBlockPermissions permissions;
    /**
     * Optional sound group defining place/break sounds for the block.
     */
    @Nullable
    CustomBlockSoundGroup soundGroup;
    /**
     * Optional stage settings for staged block behavior.
     */
    @Nullable
    CustomBlockStageSettings stageSettings;
    /**
     * Save system identifier (e.g., "yaml-file").
     */
    String saveSystem;


    /**
     * Creates a new custom block with the given name, display summoner,
     * and configured interactions. Collisions are set to an empty list.
     *
     * @param name                   block name
     * @param displaySummoner        display summoner for block entities
     * @param configuredInteractions list of interactions
     */
    public BDCAbstractCustomBlock(String name,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions) {
        this(name, displaySummoner, configuredInteractions, List.of());
    }

    /**
     * Creates a new custom block with the given name, display summoner,
     * interactions, and collisions. Uses default service class.
     *
     * @param name                   block name
     * @param displaySummoner        display summoner
     * @param configuredInteractions list of interactions
     * @param configuredCollisions   list of collisions
     */
    public BDCAbstractCustomBlock(String name,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions,
                                  List<ConfiguredCollision> configuredCollisions) {
        this(name, BDCCustomBlockService.class.getName(), displaySummoner, configuredInteractions, configuredCollisions, null);
    }

    /**
     * Creates a new custom block with the specified service class name.
     *
     * @param name                   block name
     * @param serviceClassName       class name of the service
     * @param displaySummoner        display summoner
     * @param configuredInteractions list of interactions
     * @param configuredCollisions   list of collisions
     */
    public BDCAbstractCustomBlock(String name,
                                  String serviceClassName,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions,
                                  List<ConfiguredCollision> configuredCollisions) {
        this(name, serviceClassName, displaySummoner, configuredInteractions, configuredCollisions, null);
    }

    /**
     * Creates a new custom block with an item representation.
     *
     * @param name                   block name
     * @param serviceClassName       service class name
     * @param displaySummoner        display summoner
     * @param configuredInteractions list of interactions
     * @param configuredCollisions   list of collisions
     * @param item                   item representation
     */
    public BDCAbstractCustomBlock(String name,
                                  String serviceClassName,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions,
                                  List<ConfiguredCollision> configuredCollisions,
                                  ItemStack item) {
        this(name,
                serviceClassName,
                displaySummoner,
                configuredInteractions,
                configuredCollisions,
                item,
                Material.BARRIER,
                (short) 4,
                null,
                new BDCCustomBlockSoundGroup(
                        new SimplePlayableSound(Sound.BLOCK_LODESTONE_PLACE),
                        new SimplePlayableSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)),
                null
        );
    }

    /**
     * Creates a new custom block with default service class and sounds.
     *
     * @param name                   block name
     * @param displaySummoner        display summoner
     * @param configuredInteractions list of interactions
     * @param configuredCollisions   list of collisions
     * @param item                   item representation
     */
    public BDCAbstractCustomBlock(String name,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions,
                                  List<ConfiguredCollision> configuredCollisions,
                                  ItemStack item) {
        this(name,
                BDCCustomBlockService.class.getName(),
                displaySummoner,
                configuredInteractions,
                configuredCollisions,
                item,
                Material.BARRIER,
                (short) 4,
                null,
                new BDCCustomBlockSoundGroup(
                        new SimplePlayableSound(Sound.BLOCK_LODESTONE_PLACE),
                        new SimplePlayableSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)),
                null
        );
    }

    /**
     * Creates a fully initialized custom block with all available properties.
     *
     * @param name                   block name
     * @param serviceClassName       service class name
     * @param displaySummoner        display summoner
     * @param configuredInteractions list of interactions
     * @param configuredCollisions   list of collisions
     * @param item                   item representation
     * @param centralMaterial        central material
     * @param sidesCount             number of sides
     * @param permissions            permissions
     * @param soundGroup             sound group
     * @param stageSettings          stage settings
     */
    public BDCAbstractCustomBlock(String name,
                                  String serviceClassName,
                                  GroupSummoner<Display> displaySummoner,
                                  List<ConfiguredInteraction> configuredInteractions,
                                  List<ConfiguredCollision> configuredCollisions,
                                  ItemStack item,
                                  Material centralMaterial,
                                  short sidesCount,
                                  CustomBlockPermissions permissions,
                                  CustomBlockSoundGroup soundGroup,
                                  CustomBlockStageSettings stageSettings) {
        this(name,
                serviceClassName,
                displaySummoner,
                configuredInteractions,
                configuredCollisions,
                item,
                centralMaterial,
                sidesCount,
                permissions,
                soundGroup,
                stageSettings,
                "yaml-file"
        );
    }


    /**
     * Creates a clone of this custom block, ensuring
     * interactions, collisions, and summoners are cloned.
     *
     * @return a new {@link BDCAbstractCustomBlock} with the same configuration
     */
    @Override
    public BDCAbstractCustomBlock clone() {
        List<ConfiguredInteraction> clonedConfiguredInteractions = DeepCloneable.tryCloneList(this.configuredInteractions);
        List<ConfiguredCollision> clonedConfiguredCollisions = DeepCloneable.tryCloneList(this.configuredCollisions);
        return new BDCAbstractCustomBlock(
                this.name,
                this.serviceClassName,
                DeepCloneable.tryClone(this.displaySummoner),
                clonedConfiguredInteractions,
                clonedConfiguredCollisions,
                this.item,
                this.centralMaterial,
                this.sidesCount,
                this.permissions,
                this.soundGroup,
                this.stageSettings,
                this.saveSystem
        );
    }
}
