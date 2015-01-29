package de.httpServer;

import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.game.Battle;
import de.game.Robot;
import de.game.RobotPrototype;
import de.math.Vector2D;

/**
 * @author ko
 * represents a user/client
 */
public class User {
	/**
	 * the user in the database
	 */
	private DBUser dbUser;
	/**
	 * the identification number for this session
	 */
	public final String sessionID;
	/**
	 * the login flag
	 */
	private boolean logedIn = false;
	/**
	 * the prototype of the next battle robot
	 */
	private RobotPrototype nextBattleRobot;
	/**
	 * all robots in battle
	 */
	private List<Robot> battleRobots;
	/**
	 * the actual battle
	 */
	private Battle battle;
	/**
	 * the start point on the battle map
	 */
	private Vector2D startPoint;

	/**
	 * @param sessionID the identification number for this session
	 */
	public User(String sessionID) {
		this.sessionID = sessionID;
		battleRobots = new ArrayList<Robot>();
	}

	/**
	 * set the batabase user
	 * @param dbUser the user in the database
	 */
	public void setDBUser(DBUser dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * log the user in
	 * @param dbUser the user in the database
	 */
	public void logIn(DBUser dbUser) {
		this.dbUser = dbUser;
		logedIn = true;
	}

	/**
	 * log the user out
	 */
	public void logOut() {
		logedIn = false;
	}

	/**
	 * get the logged in flag
	 * @return the logged in flag
	 */
	public boolean isLogedIn() {
		return logedIn;
	}

	/**
	 * @return the database user
	 */
	public DBUser getDBUser() {
		return (dbUser);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [sessionID=" + sessionID + ", logedIn=" + logedIn + "]";
	}
	
	/**
	 * set the next battle robot
	 * @param rp the prototype for the next robot
	 */
	public void setNextRobot (final RobotPrototype rp) {
		nextBattleRobot = rp;
	}
	
	/**
	 * get the prototype of the next battle robot
	 * @return the prototype of the next battle robot
	 */
	public RobotPrototype getNextRobot () {
		return nextBattleRobot;
	}
	
	/**
	 * @return all robots in the batlle from this user
	 */
	public List<Robot> getBattleRobots () {
		return battleRobots;
	}
	
	/**
	 * adds a new Robot for this User to the battle
	 * @param robot the new robot
	 */
	public void addBattleRobot (final Robot robot) {
		battleRobots.add(robot);
	}
	
	/**
	 * remove a Robot from the battle
	 * @param robot the remove robot
	 */
	public void removeBattleRobot (final Robot robot) {
		battleRobots.remove(robot);
	}
	
	/**
	 * @return the aktual Battle from this User
	 */
	public Battle getBattle () {
		return battle;
	}
	
	/**
	 * set a new Battle for this User
	 * @param battle the new Battle
	 * @param startPoint the start point of the battle map
	 */
	public void setBattle (final Battle battle, final Vector2D startPoint) {
		this.battle = battle;
		this.startPoint = startPoint;
	}
	
	/**
	 * @return the startpoint of the battle map
	 */
	public Vector2D getStartPoint () {
		return startPoint;
	}
	
	/**
	 * change the behavior of a robot
	 * @param robotID the identification number from the robot
	 * @param behaviour the name of the new behavior
	 */
	public void setBehavior (final long robotID, final String behaviour) {
		for (Robot r : battleRobots) {
			if (r.getID() == robotID) {
				r.setBehaviour(behaviour);
			}
		}
	}
}
