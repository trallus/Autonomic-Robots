package de.game.behaviour;

import de.game.Battle;
import de.game.Robot;

/**
 * A default implementation of Behaviour for Robot
 * 
 * @author mike
 * @version 0.1
 */
public class DefaultBehaviour extends Behaviour {

    /**
     * The time sind this behaviour lastly changed the behaviour of the
     * controlled robot
     */
    private double elapsedTimeSinceLastBehaviourChange;

    /**
     * Initializes this DefaultBehaviour with the given robot and name
     * @param robot The robot that should be controlled by this behaviour
     * @param name The name of this behaviour
     */
    public DefaultBehaviour(final Robot robot, final String name) {
	super(robot, name);
    }
    
    @Override
    public void onTick(final Battle battle, final double elapsedTime) {
	elapsedTimeSinceLastBehaviourChange += elapsedTime;
	if (elapsedTimeSinceLastBehaviourChange < 4) {
	    robot.accelerate();
	}
	else if (elapsedTimeSinceLastBehaviourChange < 8) {
	    robot.turnLeft();
	}
	else if (elapsedTimeSinceLastBehaviourChange < 12) {
	    robot.breack();
	}
	else {
	    elapsedTimeSinceLastBehaviourChange = 0;
	}
	//TODO weapon using after a weapon is useable
    }
}
