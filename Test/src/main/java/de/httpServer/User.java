package de.httpServer;

import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.game.Battle;
import de.game.Robot;
import de.game.RobotPrototype;
import de.math.Vector2D;

/**
 * Represents a user in the Server
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 *
 */
public class User {
	/**
	 * the database user
	 */
	private DBUser dbUser;
	/**
	 * the session id
	 */
	public final String sessionID;
	/**
	 * the login flag
	 */
	private boolean logedIn = false;
	/**
	 * the prototype for the next robot in the battle
	 */
	private RobotPrototype nextBattleRobot;
	/**
	 * all robots in the battle
	 */
	private final List<Robot> battleRobots;
	/**
	 * the user stay in this battle
	 */
	private Battle battle;
	/**
	 * the startpoint on the battle map
	 */
	private Vector2D startPoint;

	/**
	 * @param sessionID the session id of this user
	 */
	public User(final String sessionID) {
		this.sessionID = sessionID;
		battleRobots = new ArrayList<Robot>();
	}

	/**
	 * @param dbUser
	 */
	public void setDBUser(DBUser dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * @param dbUser
	 */
	public void logIn(DBUser dbUser) {
		this.dbUser = dbUser;
		logedIn = true;
	}

	/**
	 * log out the user from the server
	 */
	public void logOut() {
		logedIn = false;
	}

	/**
	 * @return logedIn flag
	 */
	public boolean isLogedIn() {
		return logedIn;
	}

	/**
	 * @return dbUser
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
	 * @param prototype for the next battle robot
	 */
	public void setNextRobot (final RobotPrototype rp) {
		nextBattleRobot = rp;
	}
	
	/**
	 * @return nextBattleRobot
	 */
	public RobotPrototype getNextRobot () {
		return nextBattleRobot;
	}
	
	/**
	 * @return battleRobots
	 */
	public List<Robot> getBattleRobots () {
		return battleRobots;
	}
	
	/**
	 * @param robot
	 */
	public void addBattleRobot (final Robot robot) {
		battleRobots.add(robot);
	}
	
	/**
	 * @param robot
	 */
	public void removeBattleRobot (final Robot robot) {
		battleRobots.remove(robot);
	}
	
	/**
	 * @return battle
	 */
	public Battle getBattle () {
		return battle;
	}
	
	/**
	 * Set the battle and the startPoint
	 * @param battle
	 * @param startPoint
	 */
	public void setBattle (final Battle battle, final Vector2D startPoint) {
		this.battle = battle;
		this.startPoint = startPoint;
	}
	
	/**
	 * @return startPoint
	 */
	public Vector2D getStartPoint () {
		return startPoint;
	}
	
	/**
	 * Set the behavior for a robot
	 * @param robotID
	 * @param behaviour
	 */
	public void setBehavior (long robotID, String behaviour) {
		for (Robot r : battleRobots) {
			if (r.getID() == robotID) {
				r.setBehaviour(behaviour);
			}
		}
	}
}
