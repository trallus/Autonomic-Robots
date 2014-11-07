package de.game.behaviour;

import de.game.Battle;
import de.game.Robot;

/**
 * A default implementation of Behaviour for Robot
 * @author mike
 * @version stub
 */
public class DefaultBehaviour extends Behaviour {

    public DefaultBehaviour(Robot robot) {
	super(robot);
    }

    /**
     * @see de.game.Tick#onTick(de.game.Battle, double)
     */
    @Override
    public void onTick(final Battle battle, final double elapsedTime) {
	// TODO Auto-generated method stub
    }

    /**
     * @see de.game.behaviour.Behaviour#getName()
     */
    @Override
    public String getName() {
	return getClass().getName();
    }

}
