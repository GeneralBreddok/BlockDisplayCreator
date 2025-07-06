package me.general_breddok.blockdisplaycreator.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Summoner<E> {
    E summon(@NotNull Location location);
}
