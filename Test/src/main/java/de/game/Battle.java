package de.game;

import java.util.ArrayList;
import java.util.List;

import de.httpServer.User;
import de.logger.Log;
import de.math.Vector2D;
import de.physicEngine.PhysikObject;

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
    private final long timeLimitMs;
    /**
     * The start Time of the Game
     */
    private final long startTimeMs;
    /**
     * The identification number of the battle
     */
    private final long id;
    
    private final List<User> users;
    private final List<PhysikObject> physicObjects;
    private long timeToNextRobot;
    private long robotIdCounter;
	
	public Battle (final long id, final List<User> users) {
		Log.log("Battle Create: "+id);
		this.id = id;
		this.users = users;
		this.physicObjects = new ArrayList<PhysikObject>();
		robotIdCounter = 0;
		timeToNextRobot = System.nanoTime();
		startTimeMs = timeToNextRobot;
		timeLimitMs = 10000;	// Spiel dauert nun 10 sekunden
		
		// add battle to users
		final List<Vector2D> startPoints = calculateStartpoints(users.size());
		for (int i=0; i<users.size(); i++) {
			users.get(i).setBattle(this, startPoints.get(i));
		}
	}
	
	public List<User> getUsers () {
		return users;
	}
	
	public long getID () {
		return id;
	}

	// start the battle
	@Override
	public void run() {
		Log.log("Battle Start: "+id);
		frameLoop();
	}
	
	private void frameLoop () {
		final long robotIntervall = 10000;
		final long frameIntervall = 100;	// 1/10 sec
		final long elapsedTime = 1/frameIntervall;
		final long currentTime = System.currentTimeMillis();
		
		final long battleTimeLeft = startTimeMs + timeLimitMs - currentTime;
		if (battleTimeLeft < 0) {
			Log.log("Battle End: "+id);
			return;	// Battle End
		}
		
		// set next robot
		if (timeToNextRobot < currentTime) {
			for (User u : users) {
				final Robot nextRobot = u.getNextRobot().generateRobot(robotIdCounter, u.getStartPoint());
				u.addBattleRobot(nextRobot);
				physicObjects.add(nextRobot);
				robotIdCounter++;
			}
			timeToNextRobot += robotIntervall;
		}
		
		// call onTick methods
		// move all physical objects
		for (PhysikObject p : physicObjects) {
			p.move(elapsedTime);
		}
		// handle robots
		for (User u : users) {
			for (Robot r : u.getBattleRobots()) {
				r.onTick(this, elapsedTime);
			}
		}
		
		try {
			Thread.sleep(frameIntervall);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frameLoop();
	}
	
	private List<Vector2D> calculateStartpoints (final int numberOfUsers) {
		final long angle = (long) (Math.PI / numberOfUsers); // angle between players
		final long distance = 100; //distance from the center of the battle field
		final List<Vector2D> vectorList = new ArrayList<Vector2D>();
		
		for (int i=0; i<numberOfUsers; i++) {
			final long actAngle = i*angle;
			vectorList.add(new Vector2D(Math.sin(actAngle)*distance, Math.cos(actAngle)*distance));
		}
		
		return vectorList;
	}
}
