package me.general_breddok.blockdisplaycreator.sound;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface PlayableSound {
    void play(@NotNull Location location);
    void play(@NotNull Entity entity);
    void play(@NotNull List<Entity> entities);
    void play(@NotNull World world, @Nullable Predicate<Entity> filter);
}
