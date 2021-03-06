package de.game.behaviour;
import java.util.List;

import de.game.Battle;
import de.game.Robot;
import de.game.weapon.WeaponLaser;
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
		
		if (ownTeam == null) {
			initialOwnTeam(battle.getUsers());
			robot.accelerate();
			return;
		}

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
		
		shootOnClosestAnemy(battle);
	}

	private void shootOnClosestAnemy(final Battle battle) {
		//find closest anamy
		Robot closestAnamyRobot = null;
		double distance = Double.MAX_VALUE;
		
		for (final User u : battle.getUsers()) {
			if (u.getBattleRobots() == ownTeam) continue;
			for (final Robot r : u.getBattleRobots()){
				final double nextDistance = robot.getPosition().substraction(r.getPosition()).getMagnitude();
				if (closestAnamyRobot == null || nextDistance < distance){
					closestAnamyRobot = r;
					distance = nextDistance;
				}
			}
		}
		
		if (closestAnamyRobot == null) return;
		
		final int range = robot.getWeapon().getRange();
		
		if (distance <= range) {
			final WeaponLaser wp = (WeaponLaser) robot.getWeapon();
			wp.laserShoot(robot, closestAnamyRobot, battle);
		}
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
