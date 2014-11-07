package de.game.behaviour;

import de.game.Battle;
import de.game.Robot;

/**
 * A default implementation of Behaviour for Robot
 * @author mike
 * @version stub
 */
public class DefaultBehaviour extends Behaviour {
    
    private double elapsedTimeSinceLastBehaviourChange;

    public DefaultBehaviour(Robot robot) {
	super(robot);
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

    /**
     * @see de.game.behaviour.Behaviour#getName()
     */
    @Override
    public String getName() {
	return getClass().getName();
    }

}
