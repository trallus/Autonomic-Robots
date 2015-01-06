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
public class AttacBehaviour extends Behaviour {

	/**
	 * The Robots of the own Team
	 */
	private List<Robot> anamyTeam;
	/**
	 * The attac target
	 */
	private Robot target;

	/**
	 * Initializes this DefaultBehaviour with the given robot and name
	 * 
	 * @param robot
	 *            The robot that should be controlled by this behaviour
	 * @param name
	 *            The name of this behaviour
	 */
	public AttacBehaviour(final Robot robot, final String name) {
		super(robot, name);
	}

	@Override
	public void onTick(final Battle battle, final double elapsedTime) {
		
		if (anamyTeam == null) {
			initialAnamyTeam(battle.getUsers());
			//robot.accelerate();
			//return;
		}
		
		if (anamyTeam.size() < 1) return;
		
		// search first target
		if (target == null) {
			target = getNextTarget();
			navigateTo(target);
			return;
		}
		
		// if target alive
		for (final Robot r : anamyTeam) {
			if (r == target) {
				navigateTo(target);
				return;
			}
		}
		// if not get new target
		target = getNextTarget();
		navigateTo(target);
	}
	
	private Robot getNextTarget () {

		final double[] distances = new double[anamyTeam.size()];
		
		for(int i=0; i<distances.length; i++) {
			distances[i] = anamyTeam.get(i).getPosition().substraction(robot.getPosition()).getMagnitude();
		}
		
		// find closest anamy robot
		int closest = 0;
		
		for(int i=1; i<distances.length; i++) {
			if ((distances[closest] < 0.0000001 || distances[i] < distances[closest])
					&& distances[i] > 0.0000001) {
				closest = i;
			}
		}
		
		return anamyTeam.get(closest);
	}

	/**
	 * This robot navigate to parm Robot
	 * @param robotDest this robot should navigate to robotDest
	 */
	private void navigateTo(Robot robotDest) {
		final Vector2D direction = robotDest.getPosition().substraction(robot.getPosition());
		final double scalar = direction.dot(robot.getMoveVector());
		double kreuz = direction.kreuz(robot.getMoveVector());
		
		if (scalar < 0) {
			robot.breack();
		}
		else {
			robot.accelerate();
			//if (kreuz > 0)kreuz *= -1;
		}
		
		if (kreuz > 0) robot.turnLeft();
		else if (kreuz < 0) robot.turnRight();
		/*
		System.out.println(robot.getID());
		System.out.println("s:" + scalar);
		System.out.println("k:" + kreuz);
		System.out.println("d:" + robot.getDirection());
		System.out.println(robot.getMoveVector());
		System.out.println(direction);
		*/
	}

	private void initialAnamyTeam(List<User> users) {
		for (final User u : users) {
			boolean notMyTeam = true;
			for (final Robot r : u.getBattleRobots()) {
				if (r.getID() == this.robot.getID()) {
					notMyTeam = false;
					break;
				}
			}
			if (notMyTeam) {
				anamyTeam = u.getBattleRobots();
				return;
			}
		}
	}
}
