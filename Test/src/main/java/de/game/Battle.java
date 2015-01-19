package de.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.game.behaviour.BehaviourFactory;
import de.game.weapon.Bullet;
import de.game.weapon.LaserShot;
import de.httpServer.User;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.logger.LoggerIF;
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
    /**
     * The playing users
     */
    private final List<User> users;
    /**
     * All Physic Objects in the "map"
     */
    private final List<PhysikObject> physicObjects;
    /**
     * Time betwen create new robots
     */
    private long timeToNextRobot;
    /**
     * To be sure that all Robots has unique id
     */
    private long robotIdCounter;
    /**
     * The BehaviourFactory that is used to create the Behaviours for the Robots
     */
    private final BehaviourFactory behaviourFactory;
    
    private final LoggerIF log;
    
    private final List<LaserShot> laserShotList;
	
	/**
	 * Creates and starts a battle. every battle has an unique id and a pair of users
	 * @param id
	 * @param users
	 * @param behaviourFactory 
	 */
	public Battle (final long id, final List<User> users, final BehaviourFactory behaviourFactory, final LoggerAndExceptionHandlerFacadeIF logFacade) {
	    	this.log = logFacade.getLoggerInstance();
		log.log("Battle Create: "+id, this.getClass().getName(), LogLevel.DEBUG);
		this.id = id;
		this.users = users;
		this.physicObjects = new ArrayList<PhysikObject>();
		this.laserShotList = new ArrayList<LaserShot>();
		robotIdCounter = 0;
		timeToNextRobot = System.currentTimeMillis();
		startTimeMs = timeToNextRobot;
		timeLimitMs = 60000;	// Spiel dauert nun 60 sekunden
		this.behaviourFactory = behaviourFactory;
		// add battle to users
		final List<Vector2D> startPoints = calculateStartpoints(users.size());
		for (int i=0; i<users.size(); i++) {
			users.get(i).setBattle(this, startPoints.get(i));
		}
	}
	
	/**
	 * Get the users in the battle
	 * @return
	 */
	public List<User> getUsers () {
		return users;
	}
	
	/**
	 * get the id of the battle
	 * @return
	 */
	public long getID () {
		return id;
	}

	// start the battle
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		log.log("Battle Start: "+id, this.getClass().getName(), LogLevel.DEBUG);
		frameLoop();
	}
	
	/**
	 * Calculate the battle situation per frame
	 */
	private void frameLoop () {
		final long robotIntervall = 5000;
		final double elapsedTime = .1;
		final long currentTime = System.currentTimeMillis();
		
		// if the battle is over?
		final long battleTimeLeft = startTimeMs + timeLimitMs - currentTime;
		if (battleTimeLeft < 0) {
			for (User u : users) {
				while (u.getBattleRobots().size() > 0) {
					u.getBattleRobots().remove(0);
				}
			}
			while (users.size() > 0) {
				users.remove(0);
			}
			log.log("Battle End: "+id, this.getClass().getName(), LogLevel.DEBUG);
			return;	// Battle End
		}
		
		// set next robot
		if (timeToNextRobot < currentTime) {
			for (final User u : users) {
				if (u.getNextRobot() == null) {
					nextFrame();
					return;
				}
				final RobotPrototype rp = u.getNextRobot();
				final Vector2D sp = u.getStartPoint();
				final Robot nextRobot = rp.generateRobot(robotIdCounter, sp,u,behaviourFactory);
				
				u.addBattleRobot(nextRobot);
				physicObjects.add(nextRobot);
				robotIdCounter++;
				log.log("Robot"+robotIdCounter, this.getClass().getName(), LogLevel.DEBUG);
			}
			timeToNextRobot += robotIntervall;
		}
		
		// call onTick methods
		// move all physical objects
		for (final PhysikObject p : physicObjects) {
			p.move(elapsedTime);
		}
		// handle robots
		for (final User u : users) {
			final Iterator<Robot> rli = u.getBattleRobots().iterator();
			
			while (rli.hasNext()) {
				final Robot r = rli.next();
				if (r.isDead()){
					//cleane up dead robots
					rli.remove();
					continue;
				}
				r.onTick(this, elapsedTime);
			}
		}
		
		nextFrame();
	}
	
	private void nextFrame () {
		final long frameIntervall = 100;	// 1/10 sec
		try {
			Thread.sleep(frameIntervall);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		frameLoop();
	}
	
	public void addLaserShot (Robot shooter, Robot target) {
		laserShotList.add(new LaserShot(shooter, target));
	}
	
	public List<LaserShot> getShotMap () {
		return laserShotList;
	}
	
	public void addBullet(final Bullet bullet){
	    physicObjects.add(bullet);
	}
	
	/**
	 * Calculate the start points of the users on the battle field
	 * @param int numberOfUsers
	 * @return List<Vector2D>
	 */
	private List<Vector2D> calculateStartpoints (final int numberOfUsers) {
		final long angle = (long) (2 * Math.PI / numberOfUsers); // angle between players
		final long distance = 100; //distance from the center of the battle field
		final List<Vector2D> vectorList = new ArrayList<Vector2D>();
		
		for (int i=0; i<numberOfUsers; i++) {
			final long actAngle = i*angle;
			vectorList.add(new Vector2D(Math.sin(actAngle)*distance, Math.cos(actAngle)*distance));
		}
		
		return vectorList;
	}
}
