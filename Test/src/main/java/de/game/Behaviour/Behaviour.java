package de.game.Behaviour;

import de.game.Tick;

/**
 * A Interface for every Behaviour the Robots can have
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public interface Behaviour extends Tick {
    
    /**
     * @return The Name of the Behaviour
     */
    String getName();
}
