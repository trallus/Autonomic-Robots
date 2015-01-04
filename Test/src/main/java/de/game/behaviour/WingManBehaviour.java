package de.game.behaviour;
import java.util.List;

import de.game.Battle;
import de.game.Robot;
import de.httpServer.User;
import de.math.Vector2D;

/**
 * Follow next Robot implementation of Behaviour for Robot
 * 
 * @author christian
 * @version 0.1
 */
public class WingManBehaviour extends Behaviour {

	/**
	 * The Robots of the own Team
	 */
	private List<Robot> ownTeam;

	/**
	 * Initializes this DefaultBehaviour with the given robot and name
	 * 
	 * @param robot
	 *            The robot that should be controlled by this behaviour
	 * @param name
	 *            The name of this behaviour
	 */
	public WingManBehaviour(final Robot robot, final String name) {
		super(robot, name);
	}

	@Override
	public void onTick(final Battle battle, final double elapsedTime) {
		/*
		if (robot.getID() % 3 == 0 || robot.getID() % 4 == 0 ) {
			robot.accelerate();
			if (tmp) {
				tmp = false;
				robot.turnLeft();
			} else {
				tmp = true;
				robot.turnRight();
			}
			if (robot.getMoveVector().getMagnitude() > 10) {
				robot.breack();
			}
			return;
		}
		*/
		
		if (ownTeam == null) {
			initialOwnTeam(battle.getUsers());
			robot.accelerate();
			return;
		}
		
		/*
		if (ownTeam.size() < 2) {
			robot.accelerate();
			if (robot.getMoveVector().getMagnitude() > 10) {
				robot.breack();
			}
			return;
		}
		*/

		final double[] distances = new double[ownTeam.size()];
		
		for(int i=0; i<distances.length; i++) {
			distances[i] = ownTeam.get(i).getPosition().substraction(robot.getPosition()).getMagnitude();
		}
		
		// find closest robot
		int closest = 0;
		
		for(int i=1; i<distances.length; i++) {
			if ((distances[closest] < 0.0000001 || distances[i] < distances[closest])
					&& distances[i] > 0.0000001) {
				closest = i;
			}
		}
		
		navigateTo(ownTeam.get(closest));
	}

	/**
	 * This robot navigate to parm Robot
	 * @param robotDest this robot should navigate to robotDest
	 */
	private void navigateTo(Robot robotDest) {
		final Vector2D direction = robotDest.getPosition().substraction(robot.getPosition());
		final double scalar = direction.dot(robot.getMoveVector());
		
		if (scalar < 0) robot.breack();
		else robot.accelerate();
		
		final double kreuz = direction.kreuz(robot.getMoveVector());
		
		if (kreuz > 0) robot.turnLeft();
		else robot.turnRight();
	}

	private void initialOwnTeam(List<User> users) {
		for (final User u : users) {
			for (final Robot r : u.getBattleRobots()) {
				if (r.getID() == this.robot.getID()) {
					ownTeam = u.getBattleRobots();
				}
			}
		}
	}
}
