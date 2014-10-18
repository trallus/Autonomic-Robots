package de.game;

import java.util.List;

import de.httpServer.User;
import de.logger.Log;

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
    private long timeToNextRobot;
    private long robotIdCounter;
	
	public Battle (final long id, final List<User> users) {
		Log.log("Battle Create: "+id);
		this.id = id;
		this.users = users;
		robotIdCounter = 0;
		timeToNextRobot = System.nanoTime();
		startTimeMs = timeToNextRobot;
		timeLimitMs = 10000;	// Spiel dauert nun 10 sekunden
		
		// add battle to users
		for (User u : users) {
			u.setBattle(this);
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
		final long currentTime = System.currentTimeMillis();
		
		final long battleTimeLeft = startTimeMs + timeLimitMs - currentTime;
		if (battleTimeLeft < 0) {
			Log.log("Battle End: "+id);
			return;	// Battle End
		}
		
		if (timeToNextRobot < currentTime) {
			for (User u : users) {
				u.addBattleRobot(u.getNextRobot().getClone(robotIdCounter));
				robotIdCounter++;
			}
			timeToNextRobot += robotIntervall;
		}
		
		try {
			Thread.sleep(frameIntervall);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frameLoop();
	}
}
