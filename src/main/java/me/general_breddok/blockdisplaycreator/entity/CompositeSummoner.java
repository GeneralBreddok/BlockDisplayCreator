package me.general_breddok.blockdisplaycreator.entity;

import java.util.List;

/**
 * Represents a summoner that composes multiple other {@link Summoner} instances.
 * <p>
 * This summoner delegates entity spawning to its internal summoners, allowing
 * for composite or hierarchical summoning behavior. Each internal summoner can
 * produce entities independently, and the results are aggregated.
 * </p>
 *
 * @param <E> the type of entities being summoned
 */
public interface CompositeSummoner<E> extends GroupSummoner<E> {

    /**
     * Gets the list of internal summoners used by this composite summoner.
     *
     * @return list of {@link Summoner} instances
     */
    List<Summoner<E>> getSummoners();

    /**
     * Sets the list of internal summoners for this composite summoner.
     *
     * @param summoners list of {@link Summoner} instances
     */
    void setSummoners(List<Summoner<E>> summoners);
}
