package de.game.behaviour;

import de.game.Robot;
import de.game.Tick;

/**
 * Abstract Class for every Behaviour the Robots can have
 * 
 * @author Mike Kiekebusch
 * @version 0.2
 * @since 07.10.2014
 */
public abstract class Behaviour implements Tick {

    protected final Robot robot;
    private final String name;

    /**
     * @param robot
     *            The Robot this Behaviour will controll
     * @param name
     *            The Name this Behaviour will identifie it's self with. This
     *            must be the one given from the BehaviourFactory.
     */
    public Behaviour(final Robot robot, final String name) {
	this.robot = robot;
	this.name = name;
    }

    /**
     * @return The Name of the Behaviour
     */
    public final String getName() {
	return name;
    }
}
