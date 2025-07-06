package me.general_breddok.blockdisplaycreator.custom;

import me.general_breddok.blockdisplaycreator.entity.Summoner;
import org.bukkit.util.Vector;

public interface ConfiguredEntity<E> extends Summoner<E>  {
    Summoner<E> getSummoner();
    void setSummoner(Summoner<E> summoner);
    String getIdentifier();
    void setIdentifier(String identifier);
    Vector getOffset();
    void setOffset(Vector offset);
}
