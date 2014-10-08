package de.game;

/**
 * Gives the Time Based methode neede for everything that takes actively Part in a Battle
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public interface Tick {

    /**
     * Action that should be performed for a time "tick" in a Battle
     * @param battle The Battle which send the tick
     */
    void onTick(Battle battle);
}
