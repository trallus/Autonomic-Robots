package de.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.game.behaviour.Behaviour;
import de.game.behaviour.BehaviourFactory;
import de.game.behaviour.DefaultBehaviour;
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
	this.weapon = weapon;
	this.behaviour = behaviourFactory.getInstanceOfBehaviour(behaviour, this);
	this.user = user;
	this.behaviourFactory = behaviourFactory;
	this.log = logFacade.getLoggerInstance();
    }

    public long getID() {
	return id;
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

    public void shoot(final Vector2D targetPosition) {
	// TODO Behaviour will invoke this methode so the robot fires its weapon
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
    }

    @Override
    public void onTick(final Battle battle, final double elapsedTime) {
    	lookAround(battle);
	behaviour.onTick(battle, elapsedTime); // Must be first so the Behaviour
					       // can say what to do
	if (turnLeft)
	    turn(turningSpeed * elapsedTime);
	else
	    turn(-turningSpeed * elapsedTime);
	
	if (accelerate)
	    accelerate(acceleration * elapsedTime);
	else
	    accelerate(-acceleration * elapsedTime);
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
    
    
}
