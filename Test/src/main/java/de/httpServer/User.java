package de.httpServer;

import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.game.Battle;
import de.game.Robot;
import de.game.RobotPrototype;
import de.math.Vector2D;

public class User {
	private DBUser dbUser;
	public final String sessionID;
	private boolean logedIn = false;
	private RobotPrototype nextBattleRobot;
	private List<Robot> battleRobots;
	private Battle battle;
	private Vector2D startPoint;

	public User(String sessionID) {
		this.sessionID = sessionID;
		battleRobots = new ArrayList<Robot>();
	}

	public void setDBUser(DBUser dbUser) {
		this.dbUser = dbUser;
	}

	public void logIn(DBUser dbUser) {
		this.dbUser = dbUser;
		logedIn = true;
	}

	public void logOut() {
		logedIn = false;
	}

	public boolean isLogedIn() {
		return logedIn;
	}

	public DBUser getDBUser() {
		return (dbUser);
	}

	@Override
	public String toString() {
		return "User [sessionID=" + sessionID + ", logedIn=" + logedIn + "]";
	}
	
	public void setNextRobot (final RobotPrototype rp) {
		nextBattleRobot = rp;
	}
	
	public RobotPrototype getNextRobot () {
		return nextBattleRobot;
	}
	
	public List<Robot> getBattleRobots () {
		return battleRobots;
	}
	
	public void addBattleRobot (final Robot robot) {
		battleRobots.add(robot);
	}
	
	public void removeBattleRobot (final Robot robot) {
		battleRobots.remove(robot);
	}
	
	public Battle getBattle () {
		return battle;
	}
	
	public void setBattle (final Battle battle, final Vector2D startPoint) {
		this.battle = battle;
		this.startPoint = startPoint;
	}
	
	public Vector2D getStartPoint () {
		return startPoint;
	}
	
	public void setBehavior (long robotID, String behaviour) {
		for (Robot r : battleRobots) {
			if (r.getID() == robotID) {
				r.setBehaviour(behaviour);
			}
		}
	}
}
