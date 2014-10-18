package de.httpServer;

import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.game.Battle;
import de.game.Robot;

public class User {
	private DBUser dbUser;
	public final String sessionID;
	private boolean logedIn = false;
	private Robot nextBattleRobot;
	private List<Robot> battleRobots;
	private Battle battle;

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
	
	public void setNextRobot (final Robot robot) {
		nextBattleRobot = robot;
	}
	
	public Robot getNextRobot () {
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
	
	public void setBattle (final Battle battle) {
		this.battle = battle;
	}
	
	public void setBehavior (long robotID, String behaviour) {
		for (Robot r : battleRobots) {
			if (r.getID() == robotID) {
				r.setBehaviour(behaviour);
			}
		}
	}
}
