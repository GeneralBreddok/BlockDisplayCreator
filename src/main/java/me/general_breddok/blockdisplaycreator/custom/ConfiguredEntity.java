package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.entity.Summoner;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfiguredEntity<E> extends Summoner<E>  {
    @NotNull
    Summoner<E> getSummoner();
    void setSummoner(@NotNull Summoner<E> summoner);
    String getIdentifier();
    void setIdentifier(String identifier);
    @Nullable
    Vector getOffset();
    void setOffset(Vector offset);
}
