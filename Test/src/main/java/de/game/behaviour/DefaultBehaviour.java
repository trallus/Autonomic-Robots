package de.game.behaviour;

import de.game.Battle;
import de.game.Robot;

/**
 * A default implementation of Behaviour for Robot
 * @author mike
 * @version 0.1
 */
public class DefaultBehaviour extends Behaviour {
    
    private double elapsedTimeSinceLastBehaviourChange;

    public DefaultBehaviour(final Robot robot, final String name) {
	super(robot,name);
    }

    /**
     * @see de.game.Tick#onTick(de.game.Battle, double)
     */
    @Override
    public void onTick(final Battle battle, final double elapsedTime) {
	elapsedTimeSinceLastBehaviourChange += elapsedTime;
	if(elapsedTimeSinceLastBehaviourChange <4){
	    robot.accelerate();
	}
	else if(elapsedTimeSinceLastBehaviourChange <8){
	    robot.turnLeft();
	}
	else if(elapsedTimeSinceLastBehaviourChange < 12){
	    robot.breack();
	}
	else{
	    elapsedTimeSinceLastBehaviourChange = 0;
	}
    }
}
