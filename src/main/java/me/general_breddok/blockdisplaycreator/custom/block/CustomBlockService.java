package me.general_breddok.blockdisplaycreator.custom.block;

import me.general_breddok.blockdisplaycreator.custom.block.option.CustomBlockOption;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomBlockService {
    @NotNull
    CustomBlockStorage getStorage();
    boolean isCustomBlockOnLocation(@NotNull Location location, Object... data);
    @Nullable
    CustomBlock getCustomBlock(@NotNull Location location, Object... data);
    @Nullable
    CustomBlock getCustomBlock(@NotNull Interaction interaction, Object... data);
    @Nullable
    CustomBlock getCustomBlock(@NotNull Display display, Object... data);
    @Nullable
    CustomBlock getCustomBlock(@NotNull Shulker collision, Object... data);

    CustomBlock placeBlock(@NotNull AbstractCustomBlock abstractCustomBlock, @NotNull Location location, @NotNull CustomBlockRotation rotation, @Nullable Player player, CustomBlockOption... options);
    boolean breakBlock(@NotNull CustomBlock customBlock, @Nullable Player player, CustomBlockOption... options);
}
