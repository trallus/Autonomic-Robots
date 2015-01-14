package de.game.weapon;

import java.util.ArrayList;
import java.util.List;

import de.game.Robot;
import de.httpServer.User;

public class LaserShot {
	public final List<User> haveSeenTheShot;
	public final Robot shooter;
	public final Robot target;
	
	public LaserShot (final Robot shooter, final Robot target) {
		haveSeenTheShot = new ArrayList<User>();
		this.shooter = shooter;
		this.target = target;
	}
}
