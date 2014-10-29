package de.game.behaviour;

import de.game.Robot;
import de.game.Tick;

/**
 * Abstract Class for every Behaviour the Robots can have
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public abstract class Behaviour implements Tick {
    
    protected final Robot robot;
    
    public Behaviour(Robot robot) {
	this.robot = robot;
    }
    
    /**
     * @return The Name of the Behaviour
     */
    public abstract String getName();
}
