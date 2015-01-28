package de.game;

import java.util.HashMap;
import java.util.List;

import de.game.behaviour.Behaviour;
import de.game.behaviour.BehaviourFactory;
import de.game.weapon.Weapon;
import de.httpServer.User;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.logger.LoggerIF;
import de.math.Vector2D;
import de.physicEngine.PhysikObject;

public class Robot extends PhysikObject implements Tick {
    /**
     * the identifier of
     */
    private final long id;
    /**
     * the turning speed in rad per second
     */
    private final double turningSpeed;
    /**
     * the abstract engine power
     */
    private final double enginePower;
    /**
     * the hit points
     */
    private final double armor;
    /**
     * the acceleration per second
     */
    private final double acceleration;
    /**
     * if should accelerate or break
     */
    private boolean accelerate;
    /**
     * if turn left or turn right
     */
    private boolean turnLeft;
    /**
     * the weapon
     */
    private final Weapon weapon;
    /**
     * to control the robot
     */
    private Behaviour behaviour;
    /**
     * the known robots
     */
    private HashMap<User, List<Robot>> knownRobots;
    /**
     * the owner
     */
    private final User user;
    /**
     * to create a behavior
     */
    private final BehaviourFactory behaviourFactory;
    /**
     * the logger
     */
    private final LoggerIF log;
    /**
     * the death time point
     */
    private long deathTime = 0;
    /**
     * the top speed
     */
    private final double topSpeet;

    /**
     * @param id
     * @param position
     * @param rb
     * @param weapon
     * @param user
     * @param behaviourFactory
     * @param behaviour
     * @param logFacade
     */
    public Robot(final long id, final Vector2D position,
	    final RobotPrototype rb, final Weapon weapon, final User user, final BehaviourFactory behaviourFactory, final String behaviour, final LoggerAndExceptionHandlerFacadeIF logFacade) {
	super(0, 0); // start direction 0°, speed 0 pixel/sec
	this.accelerate = true;
	this.turnLeft = true;
	this.id = id;
	setPosition(position);
	this.enginePower = rb.getEnginePower();
	this.armor = rb.getArmor();
	this.turningSpeed = .01 * enginePower;
	this.acceleration = .01 * enginePower;
	this.topSpeet = acceleration * 5; //10 sec beschleunigungsphase
	this.weapon = weapon;
	this.behaviour = behaviourFactory.getInstanceOfBehaviour(behaviour, this);
	this.user = user;
	this.behaviourFactory = behaviourFactory;
	this.log = logFacade.getLoggerInstance();
	setHitPoints(armor);
    }

    /**
     * @return long the identifire
     */
    public long getID() {
	return id;
    }
    
    /**
     * @return boolean if accelerate or break
     */
    public boolean getAccelerate () {
    	return accelerate;
    }
    
    /**
     * @return boolean if turn left or right
     */
    public boolean getTurnLeft () {
    	return turnLeft;
    }
    
    /**
     * @return the owner
     */
    public User getUser(){
    	return user;
    }

    /**
     * Gets the Behaviour with this Name and set it in the local vairable
     * 
     * @param name
     *            the name of the Behaviour ask BehaviourFactory for a list of
     *            all Behaviours
     * @see de.game.behaviour.BehaviourFactory#getBehaviours()
     */
    public void setBehaviour(final String name) {
	behaviour = behaviourFactory.getInstanceOfBehaviour(name, this);
    }

    /**
     * Calls the shoot methode of weapon
     * @param targetPosition the postion of the target
     * @param battle the battle in which this robot is
     */
    public void shoot(final Vector2D targetPosition, final Battle battle, final double elapsedTime) {
	weapon.shoot(targetPosition, battle, elapsedTime, this.getPosition());
    }
    
    /**
     * @return Weapon the weapon
     */
    public Weapon getWeapon () {
    	return weapon;
    }

    /* (non-Javadoc)
     * @see de.physicEngine.PhysikObject#onHit(de.physicEngine.PhysikObject)
     */
    @Override
    public void onHit(final PhysikObject po) {
	// TODO noch keine physik, nur ph abzug durch kollision
	final double power = po.getMoveVector().getMagnitude() * po.getMass();
	final double hp = getHitPoints() - power;
	if (hp < 0) {
	    die();
	}
	else {
	    setHitPoints(hp);
	}
    }
    
    /**
     * @param power
     */
    public void laserHit (final int power) {
    	double hp = getHitPoints() - power;
    	if (hp < 0) hp = 0;
    	setHitPoints(hp);
    	if (hp == 0) die();
    }

    /**
     * the robot turns left
     */
    public void turnLeft() {
	turnLeft = true;
    }

    /**
     * the robot turns right
     */
    public void turnRight() {
	turnLeft = false;
    }

    /**
     * the robot move faster
     */
    public void accelerate() {
	accelerate = true;
    }

    /**
     * the robot move slower
     */
    public void breack() {
	accelerate = false;
    }

    /**
     *  kill the robot
     */
    private void die() {
		log.log("Robot die: " + id, this.getClass().getName(), LogLevel.DEBUG);
		behaviour = null; // Break the bidirectional association so that gc can
				  // clean both, needs confirmation of necessarity
		deathTime = System.currentTimeMillis();
    }
    
    /**
     * @return true if the robot is dead
     */
    public boolean isDead () {
    	if (deathTime == 0) return false;
    	
    	final long liveDeathTime = 3000; //ist noch 3 sekunden nach tot zu sehen
    	final long timeLeft = (liveDeathTime + deathTime) - System.currentTimeMillis();
    	
    	if (timeLeft > 0) return false;
    	
    	return true;
    }

    /* (non-Javadoc)
     * @see de.game.Tick#onTick(de.game.Battle, double)
     */
    @Override
    public void onTick(final Battle battle, final double elapsedTime) {
    	// roboter is dead
    	if (behaviour != null) {
	    	lookAround(battle);
			behaviour.onTick(battle, elapsedTime); // Must be first so the Behaviour
							       // can say what to do
			if (turnLeft)
			    turn(turningSpeed * elapsedTime);
			else
			    turn(-turningSpeed * elapsedTime);
			
			if (accelerate && getSpeed() < topSpeet)
			    accelerate(acceleration * elapsedTime);
			else
			    accelerate(-acceleration * elapsedTime);
    	} else {
    		accelerate(-acceleration * elapsedTime * 5);
    	}
	    	
    	move(elapsedTime);
    }
    
    /**
     * @return all the known Robots
     */
    public HashMap<User, List<Robot>> getKnownRobots () {
    	return knownRobots;
    }
    
    /**
     * @param battle
     */
    private void lookAround (final Battle battle) {
    	knownRobots = new HashMap<User, List<Robot>>();
    	for (final User u : battle.getUsers()) {
    		final List<Robot> robots = u.getBattleRobots();
    		knownRobots.put(u, robots);
    	}
    }
    
    /**
     * @return the armor value of this robot
     */
    public double getArmor(){
	return armor;
    }
}
