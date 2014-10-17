package de.game;

import java.util.ArrayList;
import java.util.List;

import de.httpServer.User;

/**
 * The Battle that in which the Users will fight against each other
 * @author Mike Kiekebusch
 * @version class stub
 * @since 07.10.2014
 */
public class Battle extends Thread implements Runnable {
    /**
     * The radius (size) of the BattleField
     */
    private int fieldRadius;
    /**
     * The Timelimit in ms until the match ends
     */
    private long timeLimitMs;
    /**
     * The identification number of the battle
     */
    private final long id;
    
    private final List<User> users;
	
	public Battle (final long id, final List<User> users) {
		this.id = id;
		this.users = this.users;
	}
	
	public List<User> getUsers () {
		return users;
	}
	
	public long getID () {
		return id;
	}
	
	public void setNextRobot (final Robot robot, final User user) {
		user.setNextRobot(robot);
	}
	
	public void setBehaviour(final long robotID, final String behaviour) {
		final Robot r = searchRobot(robotID);
		r.setBehaviour(behaviour);
	}

	// start the battle
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	private Robot searchRobot (final long robotID) {
		for (User u : users) {
			final List<Robot> robots = u.getBattleRobots();
			for (Robot r : robots) {
				if (r.getID() == robotID) return r;
			}
		}
		return null;
	}
}
