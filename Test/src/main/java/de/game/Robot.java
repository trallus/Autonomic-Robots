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
    private final long id;
    private final double turningSpeed;
    private final double enginePower;
    private final double armor;
    private final double acceleration;
    private boolean accelerate;
    private boolean turnLeft;
    private final Weapon weapon;
    private Behaviour behaviour;
    private HashMap<User, List<Robot>> knownRobots;
    private final User user;
    private final BehaviourFactory behaviourFactory;
    private final LoggerIF log;
    private long deathTime = 0;
    private final double topSpeet;

    public Robot(final long id, final Vector2D position,
	    final RobotPrototype rb, final Weapon weapon, final User user, final BehaviourFactory behaviourFactory, final String behaviour, final LoggerAndExceptionHandlerFacadeIF logFacade) {
	super(0, 0); // start direction 0°, speed 0 pixel/sec
	this.accelerate = true;
	this.turnLeft = true;
	this.id = id;
	setPosition(position);
	this.enginePower = rb.getEnginePower();
	this.armor = rb.getArmor();
	this.turningSpeed = 1 / armor * enginePower;
	this.acceleration = 1 / armor * enginePower;
	this.topSpeet = acceleration * 5; //10 sec beschleunigungsphase
	this.weapon = weapon;
	this.behaviour = behaviourFactory.getInstanceOfBehaviour(behaviour, this);
	this.user = user;
	this.behaviourFactory = behaviourFactory;
	this.log = logFacade.getLoggerInstance();
	setHitPoints(armor);
    }

    public long getID() {
	return id;
    }
    
    public boolean getAccelerate () {
    	return accelerate;
    }
    
    public boolean getTurnLeft () {
    	return turnLeft;
    }
    
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
     * @see Weapon#shoot(Vector2D, Battle)
     */
    public void shoot(final Vector2D targetPosition, final Battle battle, final double elapsedTime) {
	weapon.shoot(targetPosition, battle, elapsedTime, this.getPosition());
    }
    
    public Weapon getWeapon () {
    	return weapon;
    }

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
    
    public void laserHit (final int power) {
    	double hp = getHitPoints() - power;
    	if (hp < 0) hp = 0;
    	setHitPoints(hp);
    	if (hp == 0) die();
    }

    public void turnLeft() {
	turnLeft = true;
    }

    public void turnRight() {
	turnLeft = false;
    }

    public void accelerate() {
	accelerate = true;
    }

    public void breack() {
	accelerate = false;
    }

    private void die() {
		log.log("Robot die: " + id, this.getClass().getName(), LogLevel.DEBUG);
		behaviour = null; // Break the bidirectional association so that gc can
				  // clean both, needs confirmation of necessarity
		deathTime = System.currentTimeMillis();
    }
    
    public boolean isDead () {
    	if (deathTime == 0) return false;
    	
    	final long liveDeathTime = 3000; //ist noch 3 sekunden nach tot zu sehen
    	final long timeLeft = (liveDeathTime + deathTime) - System.currentTimeMillis();
    	
    	if (timeLeft > 0) return false;
    	
    	return true;
    }

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
    
    public HashMap<User, List<Robot>> getKnownRobots () {
    	return knownRobots;
    }
    
    private void lookAround (final Battle battle) {
    	knownRobots = new HashMap<User, List<Robot>>();
    	for (final User u : battle.getUsers()) {
    		final List<Robot> robots = u.getBattleRobots();
    		knownRobots.put(u, robots);
    	}
    }
    
    /**
     * Returns the armor value of this robot
     * @return the armor value of this robot
     */
    public double getArmor(){
	return armor;
    }
}
