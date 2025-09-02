package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredCollision;
import me.general_breddok.blockdisplaycreator.custom.ConfiguredInteraction;
import me.general_breddok.blockdisplaycreator.entity.GroupSummoner;
import me.general_breddok.blockdisplaycreator.sound.SimplePlayableSound;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BDCAbstractCustomBlock implements AbstractCustomBlock {
    String name;
    String serviceClassName;
    GroupSummoner<Display> displaySummoner;
    List<ConfiguredInteraction> configuredInteractions;
    List<ConfiguredCollision> configuredCollisions;
    ItemStack item;
    Material centralMaterial;
    short sidesCount;
    @Nullable
    CustomBlockPermissions permissions;
    @Nullable
    CustomBlockSoundGroup soundGroup;
    @Nullable
    CustomBlockStageSettings stageSettings;
    String saveSystem;
    @Nullable
    CustomBlockPlacementMode placementMode;

    public BDCAbstractCustomBlock(String name, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions) {
        this(name, displaySummoner, configuredInteractions, List.of());
    }

    public BDCAbstractCustomBlock(String name, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions, List<ConfiguredCollision> configuredCollisions) {
        this(name, BDCCustomBlockService.class.getName(), displaySummoner, configuredInteractions, configuredCollisions,null);
    }

    public BDCAbstractCustomBlock(String name, String serviceClassName, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions, List<ConfiguredCollision> configuredCollisions) {
        this(name, serviceClassName, displaySummoner, configuredInteractions, configuredCollisions,null);
    }

    public BDCAbstractCustomBlock(String name, String serviceClassName, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions, List<ConfiguredCollision> configuredCollisions, ItemStack item) {
        this(name,
                serviceClassName,
                displaySummoner,
                configuredInteractions,
                configuredCollisions,
                item,
                Material.BARRIER,
                (short) 4,
                null,
                new BDCCustomBlockSoundGroup(new SimplePlayableSound(Sound.BLOCK_LODESTONE_PLACE), new SimplePlayableSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)),
                null
        );
    }

    public BDCAbstractCustomBlock(String name, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions, List<ConfiguredCollision> configuredCollisions, ItemStack item) {
        this(name,
                BDCCustomBlockService.class.getName(),
                displaySummoner,
                configuredInteractions,
                configuredCollisions,
                item,
                Material.BARRIER,
                (short) 4,
                null,
                new BDCCustomBlockSoundGroup(new SimplePlayableSound(Sound.BLOCK_LODESTONE_PLACE), new SimplePlayableSound(Sound.ITEM_LODESTONE_COMPASS_LOCK)),
                null
        );
    }

    public BDCAbstractCustomBlock(String name, String serviceClassName, GroupSummoner<Display> displaySummoner, List<ConfiguredInteraction> configuredInteractions, List<ConfiguredCollision> configuredCollisions, ItemStack item, Material centralMaterial, short sidesCount, CustomBlockPermissions permissions, CustomBlockSoundGroup soundGroup, CustomBlockStageSettings stageSettings) {
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
                "yaml-file",
                CustomBlockPlacementMode.DEFAULT
        );
    }



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
                this.saveSystem,
                this.placementMode
        );
    }
}