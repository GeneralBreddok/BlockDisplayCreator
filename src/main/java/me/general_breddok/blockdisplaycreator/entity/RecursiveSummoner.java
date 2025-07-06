package me.general_breddok.blockdisplaycreator.entity;

import java.util.List;

public interface RecursiveSummoner<E> extends GroupSummoner<E> {
    List<Summoner<E>> getSummoners();

    void setSummoners(List<Summoner<E>> summoners);
}
