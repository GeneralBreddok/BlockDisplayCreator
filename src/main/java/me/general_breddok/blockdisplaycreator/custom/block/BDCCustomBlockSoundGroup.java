package me.general_breddok.blockdisplaycreator.custom.block;

import lombok.*;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.sound.PlayableSound;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BDCCustomBlockSoundGroup implements CustomBlockSoundGroup, DeepCloneable<BDCCustomBlockSoundGroup> {
    PlayableSound placeSound;
    PlayableSound breakSound;


    @Override
    public BDCCustomBlockSoundGroup clone() {
        return new BDCCustomBlockSoundGroup(this.placeSound, this.breakSound);
    }
}
