package de.game.weapon;

import java.util.ArrayList;
import java.util.List;

import de.game.Robot;
import de.httpServer.User;

/**
 * @author ko
 * represents a laser shot in a battle
 */
public class LaserShot {
	/**
	 * how client has registered this shot
	 */
	public final List<User> haveSeenTheShot;
	/**
	 * represents the shooter
	 */
	public final Robot shooter;
	/**
	 * represents the target
	 */
	public final Robot target;
	
	/**
	 * 
	 * @param shooter fire the shot
	 * @param target is the target of the shot
	 */
	public LaserShot (final Robot shooter, final Robot target) {
		haveSeenTheShot = new ArrayList<User>();
		this.shooter = shooter;
		this.target = target;
	}
}
